package com.jrinfolab.beautyshop.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.adapter.BranchListAdapter;
import com.jrinfolab.beautyshop.db.DbHelper;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.pojo.Branch;
import com.jrinfolab.beautyshop.pojo.Employee;

import java.util.ArrayList;
import java.util.List;

public class ListBranch extends AppCompatActivity {

    private static final String TAG = "ListBranch";

    private TextView mEmptyInfo;
    private Button mActionAdd;
    private RelativeLayout mLayoutEmpty;
    private RecyclerView mListView;
    private ExtendedFloatingActionButton mFab;

    private List<Branch> mBranchList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_branch);

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Branch List");

        mEmptyInfo = findViewById(R.id.empty_info);
        mActionAdd = findViewById(R.id.action_add);
        mLayoutEmpty = findViewById(R.id.layout_empty);
        mListView = findViewById(R.id.listview);
        mFab = findViewById(R.id.fab);

        mActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddBranch.class));
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddBranch.class);
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
        getBranchListFromDb();
    }

    private void getBranchListFromDb() {

        mBranchList = DbHelper.getBranchList(mContext);

        if (mBranchList != null && mBranchList.size() > 0) {

            mListView.setVisibility(View.VISIBLE);
            mFab.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);

            BranchListAdapter adapter = new BranchListAdapter(mContext, mBranchList, clickListener);
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

    BranchListAdapter.ItemClickListener clickListener = new BranchListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Branch branch = mBranchList.get(position);
            if (view.getId() == R.id.action_delete_branch) {
                DbHelper.deleteBranch(mContext, branch.getId());
                getBranchListFromDb();
            } else if (view.getId() == R.id.action_edit_info) {
                Intent intent = new Intent(mContext, AddBranch.class);
                intent.putExtra(Constant.IS_UPDATE, true);
                intent.putExtra(Constant.DATA1, branch.getId());
                startActivity(intent);
            }
        }
    };
}