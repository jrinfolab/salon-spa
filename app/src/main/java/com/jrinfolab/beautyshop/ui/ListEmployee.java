package com.jrinfolab.beautyshop.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.adapter.EmployeeListAdapter;
import com.jrinfolab.beautyshop.db.DbHelper;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.pojo.Employee;

import java.util.List;

public class ListEmployee extends AppCompatActivity {

    private static final String TAG = "ListEmployee";

    private TextView mEmptyInfo;
    private Button mActionAdd;
    private RelativeLayout mLayoutEmpty;
    private RecyclerView mListView;
    private ExtendedFloatingActionButton mFab;

    private List<Employee> mEmpList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_employee);

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee List");

        mEmptyInfo = findViewById(R.id.empty_info);
        mActionAdd = findViewById(R.id.action_add);
        mLayoutEmpty = findViewById(R.id.layout_empty);
        mListView = findViewById(R.id.listview);
        mFab = findViewById(R.id.fab);

        mActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddEmployee.class);
                intent.putExtra(Constant.IS_UPDATE, false);
                startActivity(intent);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddEmployee.class);
                intent.putExtra(Constant.IS_UPDATE, false);
                startActivity(intent);
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scroll Down
                    if (mFab.isShown()) {
                        mFab.hide();
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!mFab.isShown()) {
                        mFab.show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEmpListFromDb();
    }

    private void getEmpListFromDb() {

        mEmpList = DbHelper.getEmployeeList(mContext);

        if (mEmpList != null && mEmpList.size() > 0) {

            mListView.setVisibility(View.VISIBLE);
            mFab.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);

            EmployeeListAdapter adapter = new EmployeeListAdapter(mContext, mEmpList, clickListener);
            mListView.setAdapter(adapter);
            mListView.setLayoutManager(new LinearLayoutManager(this));

        } else {
            mListView.setVisibility(View.GONE);
            mFab.setVisibility(View.GONE);
            mLayoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    EmployeeListAdapter.ItemClickListener clickListener = new EmployeeListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            final Employee employee = mEmpList.get(position);
            if (view.getId() == R.id.action_delete) {

                String message = "You want to delete '"+employee.getName()+"' from the employee list";
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure ?");
                builder.setMessage(message);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DbHelper.deleteEmployee(mContext, employee.getId());
                        getEmpListFromDb();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            } else if (view.getId() == R.id.action_edit) {
                Intent intent = new Intent(mContext, AddEmployee.class);
                intent.putExtra(Constant.IS_UPDATE, true);
                intent.putExtra(Constant.DATA1, employee.getId());
                startActivity(intent);
            }else if (view.getId() == R.id.action_call) {
                Uri uri  = Uri.fromParts("tel", employee.getPhone(), null);
                Intent intent = new Intent(Intent.ACTION_DIAL,uri );
                startActivity(intent);
            }
        }
    };
}