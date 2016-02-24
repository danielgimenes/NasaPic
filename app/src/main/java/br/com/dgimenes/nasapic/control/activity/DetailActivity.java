package br.com.dgimenes.nasapic.control.activity;

import android.os.Bundle;
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
import br.com.dgimenes.nasapic.control.ErrorMessage;
import br.com.dgimenes.nasapic.model.SpacePic;
import br.com.dgimenes.nasapic.service.DefaultPicasso;
import br.com.dgimenes.nasapic.service.EventsLogger;
import br.com.dgimenes.nasapic.service.interactor.OnFinishListener;
import br.com.dgimenes.nasapic.service.interactor.SpacePicInteractor;
import br.com.dgimenes.nasapic.util.DateUtils;
import br.com.dgimenes.nasapic.view.LoadingDialog;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends TrackedActivity {

    public static final String SPACE_PIC_PARAM = "SPACE_PIC_PARAM";

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

    private SpacePic spacePic;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        spacePic = getIntent().getExtras().getParcelable(DetailActivity.SPACE_PIC_PARAM);
        setupUI();
        loadImageAsync();
    }

    private void setupUI() {
        this.setTitle(spacePic.getTitle());
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingDialog = new LoadingDialog(this);
        explanationTextView.setText(
                DateUtils.friendlyDateString(this, spacePic.getOriginallyPublishedAt()) + " - \"" +
                        spacePic.getDescription() + "\" " + getResources().getString(R.string.text_origin));

        openExplanationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (explanationTextView.getVisibility() == View.GONE) {
                    showExplanation();
                } else {
                    hideExplanation();
                }
            }
        });

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                new SpacePicInteractor(DetailActivity.this).setWallpaper(
                        spacePic.getHdImageUrl(), new OnFinishListener<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                loadingDialog.dismiss();
                                EventsLogger.logEvent("Wallpaper set manually");
                                String msg = getResources().getString(R.string.success_setting_wallpaper);
                                Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                loadingDialog.dismiss();
                                ErrorMessage error = ErrorMessage.SETTING_WALLPAPER;
                                EventsLogger.logError(error, throwable);
                                String errorMessage = getResources().getString(error.userMessageRes);
                                Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void loadImageAsync() {
        Picasso picasso = DefaultPicasso.get(this, null);
        picasso.load(spacePic.getHdImageUrl()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                ErrorMessage error = ErrorMessage.DOWNLOADING_IMAGE;
                EventsLogger.logError(error, null);
                String errorMessage = getResources().getString(error.userMessageRes);
                Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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

    private void hideExplanation() {
        explanationTextView.setVisibility(View.GONE);
        openExplanationButton.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_info_outline_white_36dp, 0);
    }

    private void showExplanation() {
        explanationTextView.setVisibility(View.VISIBLE);
        openExplanationButton.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_highlight_off_white_36dp, 0);
    }
}
