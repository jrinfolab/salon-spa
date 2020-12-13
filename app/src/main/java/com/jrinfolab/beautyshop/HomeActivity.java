package com.jrinfolab.beautyshop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jrinfolab.beautyshop.db.DbHelper;
import com.jrinfolab.beautyshop.pojo.Branch;
import com.jrinfolab.beautyshop.ui.AddBill;
import com.jrinfolab.beautyshop.ui.ListBranch;
import com.jrinfolab.beautyshop.ui.ListEmployee;
import com.jrinfolab.beautyshop.ui.ServiceList;
import com.jrinfolab.beautyshop.ui.account.LoginActivity;
import com.jrinfolab.beautyshop.ui.AddBranch;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RelativeLayout mRoolView;
    private ImageView mButtonSwitchNavDrawer;
    private ExtendedFloatingActionButton mFab;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        //   toolbar = findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mButtonSwitchNavDrawer = findViewById(R.id.nav_drawer_button);
        mFab = findViewById(R.id.action_add_bill);
        mRoolView = findViewById(R.id.root_layout);

        // setSupportActionBar(toolbar);

        // getSupportActionBar().setTitle("Saloon Spa");

    /*    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
*/
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        mButtonSwitchNavDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.openDrawer(Gravity.START);
                } else {
                    drawerLayout.closeDrawer(Gravity.END);
                }
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddBill.class);
                startActivity(intent);
            }
        });
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

                List<Branch> branches = DbHelper.getBranchList(mContext);
                if (branches != null && branches.size() > 0) {
                    Intent intent = new Intent(mContext, ListEmployee.class);
                    startActivity(intent);
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("No Branch Added");
                    builder.setMessage("Employee can be added, after adding branch, please add a brach first");
                    builder.setPositiveButton("Add Branch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(mContext, AddBranch.class));
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                    //showSnackBar("No branch added, Please add branch first");
                }

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

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(mRoolView, message, Snackbar.LENGTH_LONG);
             /*   .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });*/
        snackbar.show();
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