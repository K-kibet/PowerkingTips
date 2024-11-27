package com.codesui.powerkingtips;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.codesui.powerkingtips.fragments.AboutFragment;
import com.codesui.powerkingtips.fragments.FreeFragment;
import com.codesui.powerkingtips.fragments.PremiumFragment;
import com.codesui.powerkingtips.fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, new FreeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_free);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_free:
                fragment = new FreeFragment();
                break;
            case R.id.nav_premium:
                fragment = new PremiumFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                break;
            case R.id.nav_share:
                drawer.closeDrawer(GravityCompat.START);
                ShareManager shareManager = new ShareManager(this);
                shareManager.shareApp();
                return false;
            case R.id.nav_rate:
                drawer.closeDrawer(GravityCompat.START);
                RateManager rateManager = new RateManager(this);
                rateManager.rate();
                return false;
        }
        assert fragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}