package com.jrinfolab.beautyshop.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.pojo.Employee;

import java.util.ArrayList;

public class ListEmployee extends AppCompatActivity {

    private TextView mEmptyInfo;
    private Button mActionAdd;
    private RelativeLayout mLayoutEmpty;
    private ListView mListView;

    private ArrayList<Employee> mEmployeeList;

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

        mEmployeeList = new ArrayList<>();

        if (mEmployeeList.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mLayoutEmpty.setVisibility(View.VISIBLE);
        }

        mActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddEmployee.class));
            }
        });
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


    public class MyAdapter extends ArrayAdapter<Employee> {

        private ArrayList<Employee> data;
        private Context context;

        public MyAdapter(@NonNull Context context, int resource, ArrayList<Employee> data) {
            super(context, resource, data);
            this.context = context;
            this.data = data;
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            Employee dataModel = getItem(position);

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.add_branch, parent, false);
            }

            return view;
        }
    }
}