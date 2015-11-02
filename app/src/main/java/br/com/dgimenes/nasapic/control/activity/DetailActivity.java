package br.com.dgimenes.nasapic.control.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.model.APOD;
import br.com.dgimenes.nasapic.service.DefaultPicasso;
import br.com.dgimenes.nasapic.service.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.util.DateUtils;
import br.com.dgimenes.nasapic.view.LoadingDialog;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String APOD_OBJECT_PARAM = "APOD_OBJECT_PARAM";

    @Bind(R.id.image_view)
    ImageView imageView;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.explanation)
    public TextView explanationTextView;

    @Bind(R.id.open_explanation_button)
    Button openExplanationButton;

    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;

    @Bind(R.id.set_wallpaper_button)
    Button setWallpaperButton;

    private APOD apod;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        apod = getIntent().getExtras().getParcelable(DetailActivity.APOD_OBJECT_PARAM);
        setupUI();
        loadImageAsync();
    }

    private void setupUI() {
        this.setTitle(apod.getTitle());
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingDialog = new LoadingDialog(this);
        explanationTextView.setText(DateUtils.friendlyDateString(this, apod.getDate()) + " - \"" +
                apod.getExplanation() + "\" " + getResources().getString(R.string.text_origin));

        openExplanationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (explanationTextView.getVisibility() == View.GONE) {
                    explanationTextView.setVisibility(View.VISIBLE);
                    openExplanationButton.setText(
                            getResources().getString(R.string.close_description_button));
                } else {
                    explanationTextView.setVisibility(View.GONE);
                    openExplanationButton.setText(
                            getResources().getString(R.string.open_description_button));
                }
            }
        });

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                new ApodInteractor(DetailActivity.this).setWallpaper(apod.getUrl(), new OnFinishListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        loadingDialog.dismiss();
                        String errorMessage = getResources().getString(R.string.success_setting_wallpaper);
                        Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loadingDialog.dismiss();
                        String errorMessage = getResources().getString(R.string.error_setting_wallpaper);
                        Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadImageAsync() {
        Picasso picasso = DefaultPicasso.get(this, null);
        picasso.load(apod.getUrl()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                String errorMessage = getResources().getString(R.string.error_downloading_apod);
                Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
