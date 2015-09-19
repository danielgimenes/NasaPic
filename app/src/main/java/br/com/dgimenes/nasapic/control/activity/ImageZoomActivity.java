package br.com.dgimenes.nasapic.control.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.dgimenes.nasapic.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageZoomActivity extends AppCompatActivity {

    public static final String IMAGE_PATH_PARAM = "IMAGE_PATH_PARAM";

    @Bind(R.id.image_view)
    ImageView imageView;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        String imagePath = getIntent().getExtras().getString(ImageZoomActivity.IMAGE_PATH_PARAM);
        loadImageAsync(imagePath);
    }

    private void loadImageAsync(String imagePath) {
        new AsyncTask<String, Void, BitmapDrawable>() {

            @Override
            protected BitmapDrawable doInBackground(String... params) {
                try {
                    if (ImageZoomActivity.this != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(ImageZoomActivity.this.openFileInput(params[0]));
                        return new BitmapDrawable(getResources(), bitmap);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(BitmapDrawable drawable) {
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }.execute(imagePath);

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

    public static String saveImageOnDiskTemporarily(Context context, Bitmap bitmap) throws IOException {
        String fileName = "tmp_image";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        fo.write(bytes.toByteArray());
        fo.close();
        return fileName;
    }
}
