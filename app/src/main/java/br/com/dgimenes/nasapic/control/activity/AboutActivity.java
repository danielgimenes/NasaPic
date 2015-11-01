package br.com.dgimenes.nasapic.control.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.dgimenes.nasapic.BuildConfig;
import br.com.dgimenes.nasapic.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.version)
    TextView versionTextView;

    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String versionText = getResources().getString(R.string.app_name) + " - " +
                getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME +
                " (" + (BuildConfig.VERSION_CODE) + ")";
        versionTextView.setText(versionText);

//        String labelText = getResources().getString(R.string.about_message);
        //Spannable spannable = URLSpanNoUnderline.removeUrlUnderline(Html.fromHtml(labelText));
        //titleTextView.setMovementMethod(LinkMovementMethod.getInstance());
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
