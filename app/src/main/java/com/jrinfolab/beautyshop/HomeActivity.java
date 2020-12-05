package com.jrinfolab.beautyshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.jrinfolab.beautyshop.ui.ListBranch;
import com.jrinfolab.beautyshop.ui.ListEmployee;
import com.jrinfolab.beautyshop.ui.ServiceList;
import com.jrinfolab.beautyshop.ui.account.LoginActivity;
import com.jrinfolab.beautyshop.ui.AddBranch;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Saloon Spa");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_settings:
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.logout: {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.nav_branch: {
                Intent intent = new Intent(mContext, ListBranch.class);
                startActivity(intent);
            }
            break;

            case R.id.nav_employee: {
                Intent intent = new Intent(mContext, ListEmployee.class);
                startActivity(intent);
            }
            break;

            case R.id.nav_service_menu: {
                Intent intent = new Intent(mContext, ServiceList.class);
                startActivity(intent);
            }
            break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        boolean isNavOpened = drawerLayout.isDrawerOpen(GravityCompat.START);
        if (isNavOpened) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }
}