package br.com.dgimenes.nasapic.control.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.dgimenes.nasapic.R;
import br.com.dgimenes.nasapic.control.adapter.TabPagerAdapter;
import br.com.dgimenes.nasapic.service.PeriodicWallpaperChangeService;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends TrackedActivity {

    @Bind(R.id.tab_pager)
    ViewPager tabPager;

    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;

    private TabPagerAdapter tabPagerAdapter;

    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupUI();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            PeriodicWallpaperChangeService.updatePeriodicWallpaperChangeSetup(this);
        }
    }

    private void setupUI() {
        setSupportActionBar(mainToolbar);
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), this);
        tabPager.setAdapter(tabPagerAdapter);
        for (int i = 0; i < tabPagerAdapter.getCount(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabPagerAdapter.getPageTitle(i))
                    .setIcon(tabPagerAdapter.getPageIcon(i)));
        }
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}