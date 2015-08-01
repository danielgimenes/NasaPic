package br.com.dgimenes.nasapic.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.adapter.APODPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;


public class PictureActivity extends ActionBarActivity {

    public static final int DAYS_TO_DISPLAY = 3;

    @Bind(R.id.set_wallpaper_button)
    Button setWallpaperButton;

    @Bind(R.id.title_text)
    TextView titleTextView;

    @Bind(R.id.apod_pager)
    ViewPager apodPager;

    private APODPagerAdapter apodPagerAdapter;

    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        apodPagerAdapter = new APODPagerAdapter(getSupportFragmentManager(), DAYS_TO_DISPLAY);
        apodPager.setAdapter(apodPagerAdapter);
        apodPager.setCurrentItem(DAYS_TO_DISPLAY);
        apodPager.setClipToPadding(false);
        apodPager.setPageMargin(-60);
        apodPager.setPadding(50, 0, 50, 0);
        apodPager.setOffscreenPageLimit(DAYS_TO_DISPLAY);
        setWallpaperButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingDialog();
                        new SetWallpaperAsyncTask().execute();
                    }
                }

        );

        titleTextView.setText(Html.fromHtml(getResources().getString(R.string.picture_screen_title)));
        titleTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void hideLoadingDialog() {
        loadingDialog.dismiss();
    }

    private void showLoadingDialog() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setIndeterminate(false);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage(getString(R.string.wait));
        loadingDialog.show();
    }

    private void displayToastMessage(String message) {
        Toast.makeText(PictureActivity.this, message, Toast.LENGTH_SHORT).show();
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

    class SetWallpaperAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                WallpaperManager wallpaperMgr = WallpaperManager.getInstance(PictureActivity.this);
                wallpaperMgr.setWallpaperOffsetSteps(0.5f, 1.0f);
                Bitmap bmp = apodPagerAdapter.getFragment(apodPager.getCurrentItem())
                        .getBitmap();
                wallpaperMgr.setBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                displayToastMessage(getString(R.string.error_setting_wallpaper));
            } else {
                displayToastMessage(getString(R.string.wallpaper_set));
            }
            hideLoadingDialog();
        }
    }
}