package br.com.dgimenes.nasapic;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private Bitmap apodBitmap;

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
                    wallpaperMgr.setBitmap(apodBitmap);
                    displayMessageFromStringRes(R.string.wallpaper_set);
                } catch (IOException e) {
                    displayMessageFromStringRes(R.string.error_setting_wallpaper);
                    e.printStackTrace();
                }
            }
        });
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
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                try {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int ideal_height = size.y;
                    apodBitmap = picasso.load(params[0]).placeholder(R.drawable.loading)
                            .resize(0, ideal_height).get();
                } catch (IOException e) {
                    displayMessageFromStringRes(R.string.error_downloading_apod);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                PictureActivity.this.previewImageView.setImageBitmap(apodBitmap);
            }
        }.execute(pictureUrl);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayMessageFromStringRes(int stringRes) {
        String message = getResources().getString(stringRes);
        Toast.makeText(PictureActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
