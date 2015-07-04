package br.com.dgimenes.nasapic;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import br.com.dgimenes.nasapic.interactor.ApodInteractor;
import br.com.dgimenes.nasapic.interactor.OnFinishListener;


public class PictureActivity extends ActionBarActivity {

    private Picasso picasso;

    private ImageView previewImageView;
    private Button setWallpaperButton;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        setupUI();
        setupPicasso();
        loadNasaAPOD();
    }

    private void setupUI() {
        previewImageView = (ImageView) findViewById(R.id.apod_preview_image);
        setWallpaperButton = (Button) findViewById(R.id.set_wallpaper_button);

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WallpaperManager wallpaperMgr = WallpaperManager.getInstance(PictureActivity.this);
                    wallpaperMgr.setWallpaperOffsetSteps(0.5f, 1.0f);
                    wallpaperMgr.setBitmap(((BitmapDrawable) previewImageView.getDrawable()).getBitmap());
                    displayMessageFromStringRes(R.string.wallpaper_set);
                } catch (IOException e) {
                    displayMessageFromStringRes(R.string.error_setting_wallpaper);
                    e.printStackTrace();
                }
            }
        });

        titleTextView = (TextView) findViewById(R.id.title_text);
        titleTextView.setText(Html.fromHtml(getResources().getString(R.string.picture_screen_title)));
        titleTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void loadNasaAPOD() {
        new ApodInteractor().getNasaApodURI(new OnFinishListener<String>() {

            @Override
            public void onSuccess(String pictureUrl) {
                downloadAndSetPicture(pictureUrl);
            }

            @Override
            public void onError(Throwable throwable) {
                displayMessageFromStringRes(R.string.error_loading_apod);
                throwable.printStackTrace();
            }
        });
    }

    private void downloadAndSetPicture(String pictureUrl) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ideal_height = size.y;
        picasso.load(pictureUrl).placeholder(R.drawable.loading).resize(0, ideal_height)
                .into(previewImageView);
    }

    private void setupPicasso() {
        picasso = new Picasso.Builder(this)
                .downloader(new OkHttpDownloader(new OkHttpClient()))
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        displayMessageFromStringRes(R.string.error_downloading_apod);
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.about_message)
                    .setTitle(R.string.action_about);
            builder.setNeutralButton(getResources().getString(R.string.about_close_button),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayMessageFromStringRes(int stringRes) {
        String message = getResources().getString(stringRes);
        Toast.makeText(PictureActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
