package com.jrinfolab.beautyshop.ui.budget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.adapter.BudgetListAdapter;
import com.jrinfolab.beautyshop.db.DbHelperBudget;
import com.jrinfolab.beautyshop.pojo.Budget;

import java.util.List;

public class BudgetList extends AppCompatActivity {

    private static final String TAG = "ListBudget";

    private TextView mEmptyInfo;
    private RecyclerView mListView;
    private ExtendedFloatingActionButton mFab;
    private List<Budget> mTransactionList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_budget);

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Budget List");

        mEmptyInfo = findViewById(R.id.empty_info);
        mListView = findViewById(R.id.listview);
        mFab = findViewById(R.id.fab);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        mTransactionList = DbHelperBudget.getBudgetList(mContext);

        if (mTransactionList != null && mTransactionList.size() > 0) {

            mEmptyInfo.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            BudgetListAdapter adapter = new BudgetListAdapter(mContext, mTransactionList, clickListener);
            mListView.setAdapter(adapter);
            mListView.setLayoutManager(new LinearLayoutManager(this));

        } else {
            mListView.setVisibility(View.GONE);
            mEmptyInfo.setVisibility(View.VISIBLE);
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

    BudgetListAdapter.ItemClickListener clickListener = new BudgetListAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Budget budget = mTransactionList.get(position);
            if (view.getId() == R.id.root_layout) {
                showDialog(true, budget);
            }
        }
    };

    public void showDialog(final boolean isUpdate, final Budget budget) {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_budget_detail, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final TextInputEditText category = view.findViewById(R.id.edit_category);
        final TextInputEditText note = view.findViewById(R.id.edit_note);
        final TextInputEditText amount = view.findViewById(R.id.edit_amount);

        String buttonText = isUpdate ? "Update" : "Add";

        if (isUpdate) {
            category.setText(budget.getCategory());
            note.setText(budget.getNote());
            amount.setText(budget.getAmount()+"");
        }

        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isUpdate) {
                    budget.setCategory(category.getText().toString());
                    budget.setAmount(Integer.parseInt(amount.getText().toString()));
                    budget.setNote(note.getText().toString());
                    DbHelperBudget.updateBudget(mContext, budget);
                } else {
                    Budget newBudget = new Budget();
                    newBudget.setAmount(Integer.parseInt(amount.getText().toString()));
                    newBudget.setNote(note.getText().toString());
                    newBudget.setCategory(category.getText().toString());
                    DbHelperBudget.addBudget(mContext, newBudget);
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

        if (isUpdate) {
            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DbHelperBudget.deleteBudget(mContext, String.valueOf(budget.getId()));
                    updateUI();
                }
            });
        }

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}