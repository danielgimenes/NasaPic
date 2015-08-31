package br.com.dgimenes.nasapic.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.adapter.TabPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    @Bind(R.id.tab_pager)
    ViewPager tabPager;

    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;

    private TabPagerAdapter tabPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        tabPager.setAdapter(tabPagerAdapter);
        int i = 0;
        tabLayout.addTab(tabLayout.newTab().setText(tabPagerAdapter.getPageTitle(i))
                .setIcon(tabPagerAdapter.getPageIcon(this, i++)));
        tabLayout.addTab(tabLayout.newTab().setText(tabPagerAdapter.getPageTitle(i))
                .setIcon(tabPagerAdapter.getPageIcon(this, i++)));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(tabPager));
        tabPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && getSupportActionBar() != null) {
            tabLayout.setElevation(this.getSupportActionBar().getElevation());
            this.getSupportActionBar().setElevation(0);
        }
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
            String labelText = getResources().getString(R.string.about_message);
            //Spannable spannable = URLSpanNoUnderline.removeUrlUnderline(Html.fromHtml(labelText));
            //titleTextView.setMovementMethod(LinkMovementMethod.getInstance());
            builder.setMessage(labelText)
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
}