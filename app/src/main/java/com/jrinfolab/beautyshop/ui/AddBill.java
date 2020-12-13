package com.jrinfolab.beautyshop.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.adapter.ItemClickListener;
import com.jrinfolab.beautyshop.adapter.NewBillListAdapter;
import com.jrinfolab.beautyshop.db.DbHelper;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.helper.Preference;
import com.jrinfolab.beautyshop.pojo.Branch;
import com.jrinfolab.beautyshop.pojo.PriceItem;
import com.jrinfolab.beautyshop.view.LoaderButton;

import java.util.ArrayList;
import java.util.List;

public class AddBill extends AppCompatActivity {

    private RecyclerView mListView;
    private Button mActionAdd, mActionBrowse;
    private LinearLayout mBillLayout;
    private LoaderButton mLoaderButton;
    private TextView mTotalPrice;
    private TextInputEditText mEditName, mEditPhone;
    private TextInputLayout mIplName, mIplPhone;

    private Context mContext;
    List<PriceItem> mPriceItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Bill");

        mContext = this;

        mIplName = findViewById(R.id.ipl_name);
        mIplPhone = findViewById(R.id.ipl_phone);
        mEditName = findViewById(R.id.name);
        mEditPhone = findViewById(R.id.phone);
        mListView = findViewById(R.id.list_view);
        mTotalPrice = findViewById(R.id.total_price);
        mActionAdd = findViewById(R.id.action_add_service);
        mActionBrowse = findViewById(R.id.action_browse_service);
        mBillLayout = findViewById(R.id.bill_info_layout);
        mLoaderButton = findViewById(R.id.loader_button);

        mPriceItems = new ArrayList<>();

        mPriceItems.add(new PriceItem("Mauruthu", 20));
        mPriceItems.add(new PriceItem("Mauruthu", 20));
        mPriceItems.add(new PriceItem("Mauruthu", 20));

        mLoaderButton.setText("Add New Bill", "Please wait ....", false);

        mActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(false, 0);
            }
        });

        mActionBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServiceList.class);
                intent.putExtra(Constant.DATA1, true);
                startActivityForResult(intent, 100);
            }
        });

        mLoaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formValidation();
            }
        });

        updateListItems();
    }

    private void formValidation() {

        mLoaderButton.showLoader(true);

        String name = mEditName.getText().toString();
        String phone = mEditPhone.getText().toString();

        boolean validated = true;

        if (name.length() < 3) {
            mIplName.setError("Enter valid name");
            validated = false;
        }

        if (phone.length() < 10) {
            mIplPhone.setError("Enter valid number");
            validated = false;
        }

        if (!validated) {
            mLoaderButton.showLoader(false);
            return;
        }

        mLoaderButton.showLoader(false);
        finish();
    }


    private void updateListItems() {

        if (mPriceItems.size() > 0) {
            mBillLayout.setVisibility(View.VISIBLE);
            mLoaderButton.setVisibility(View.VISIBLE);
            NewBillListAdapter adapter = new NewBillListAdapter(mContext, mPriceItems, mItemClickListener);
            mListView.setAdapter(adapter);
            mListView.setLayoutManager(new LinearLayoutManager(this));
            int totalPrice = 0;
            for (PriceItem item : mPriceItems) {
                totalPrice = totalPrice + item.getPrice();
            }

            mTotalPrice.setText(getString(R.string.text_rupees_symbol) + totalPrice);
        } else {
            mBillLayout.setVisibility(View.GONE);
            mLoaderButton.setVisibility(View.GONE);
        }
    }

    private void showDialog(boolean isUpdate, final int index) {

        final MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_bill_service_item, null, false);
        final TextInputEditText name = view.findViewById(R.id.name);
        final TextInputEditText price = view.findViewById(R.id.price);

        if (isUpdate) {

            final PriceItem priceItem = mPriceItems.get(index);

            final String oldName = priceItem.getName();
            final int oldPrice = priceItem.getPrice();

            name.setText(oldName);
            price.setText(oldPrice + "");

            dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                    String newName = name.getText().toString();
                    int newPrice = Integer.parseInt(price.getText().toString());

                    if (oldName.equals(newName) && oldPrice == newPrice) {
                        return;
                    } else {
                        PriceItem newPriceItem = new PriceItem(newName, newPrice);
                        mPriceItems.set(index, newPriceItem);
                    }

                    updateListItems();
                }
            });

            dialogBuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPriceItems.remove(index);
                    updateListItems();
                }
            });

        } else {

            dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                    String newName = name.getText().toString();
                    int newPrice = Integer.parseInt(price.getText().toString());

                    mPriceItems.add(new PriceItem(newName, newPrice));

                    updateListItems();
                }
            });
        }

        dialogBuilder.setView(view);

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.setCancelable(true);
        dialogBuilder.create().show();
    }

    ItemClickListener mItemClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            showDialog(true, position);
        }
    };

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        mPriceItems.add(new PriceItem(intent.getStringExtra(Constant.DATA1),
                intent.getIntExtra(Constant.DATA2, 0)));

        updateListItems();
    }
}
