package com.jrinfolab.beautyshop.ui.budget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.adapter.CategoryListAdapter;
import com.jrinfolab.beautyshop.db.DbHelper;
import com.jrinfolab.beautyshop.db.DbProvider;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.helper.Util;
import com.jrinfolab.beautyshop.pojo.Category;
import com.jrinfolab.beautyshop.ui.AddBranch;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CategoryList extends AppCompatActivity {

    private static final String TAG = "ListBranch";

    private TextView mEmptyInfo;
    private Button mActionAdd;
    private RelativeLayout mLayoutEmpty;
    private RecyclerView mListView;
    private ExtendedFloatingActionButton mFab;

    private List<Category> mCategoryList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_category);

        mContext = this;

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category List");

        mEmptyInfo = findViewById(R.id.empty_info);
        mActionAdd = findViewById(R.id.action_add);
        mLayoutEmpty = findViewById(R.id.layout_empty);
        mListView = findViewById(R.id.listview);
        mFab = findViewById(R.id.fab);

        mActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(false, null);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(false, null);
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

        Util.insertDefaultCategory(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();
    }

    private void updateUI() {

        mCategoryList = DbHelper.getCategoryList(mContext);

        if (mCategoryList != null && mCategoryList.size() > 0) {

            mListView.setVisibility(View.VISIBLE);
            mFab.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);

            CategoryListAdapter adapter = new CategoryListAdapter(mContext, mCategoryList, clickListener);
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

    CategoryListAdapter.ItemClickListener clickListener = new CategoryListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Category category = mCategoryList.get(position);
            if (view.getId() == R.id.root_layout) {
                showDialog(true, category);
            }
        }
    };

    public void showDialog(final boolean isUpdate, final Category category) {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.cateogty_detail, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final TextInputEditText catName = view.findViewById(R.id.edit_name);
        TextInputLayout iplCatName = view.findViewById(R.id.ipl_name);
        final RadioButton radioIncome = view.findViewById(R.id.radio_income);
        final RadioButton radioExpense = view.findViewById(R.id.radio_expense);

        String buttonText = isUpdate ? "Update" : "Add";

        radioExpense.setChecked(true);
        radioIncome.setChecked(false);

        final AtomicInteger catTyp = new AtomicInteger(0);

        radioIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioExpense.setChecked(false);
                    catTyp.set(1);
                }

            }
        });

        radioExpense.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioIncome.setChecked(false);
                    catTyp.set(0);
                }
            }
        });

        if (isUpdate) {
            catName.setText(category.getName());
            catTyp.set(category.getType());
            radioExpense.setChecked(catTyp.get() == 0);
            radioIncome.setChecked(catTyp.get() == 1);
        }

        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isUpdate) {
                    DbHelper.updateCategory(mContext, catName.getText().toString(), catTyp.get(), category.getId());
                } else {
                    DbHelper.addCategory(mContext, catName.getText().toString(), catTyp.get());
                }
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if(isUpdate) {
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DbHelper.deleteCategory(mContext, category.getId());
                    updateUI();
                }
            });
        }

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}