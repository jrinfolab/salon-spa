package com.jrinfolab.beautyshop.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.adapter.ServiceListAdapter;
import com.jrinfolab.beautyshop.helper.JsonHelper;
import com.jrinfolab.beautyshop.pojo.PriceGroup;
import com.jrinfolab.beautyshop.pojo.PriceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceList extends AppCompatActivity {

    private ExpandableListView mExpListView;

    private Context mContext;

    List<PriceGroup> mGroupItems;
    String[] mGroupNames;
    int mSelectedIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Service Menu");

        mContext = this;

        mExpListView = findViewById(R.id.exp_list_view);

        generateDefItems();

        mExpListView.setGroupIndicator(null);
        mExpListView.setDivider(null);
        mExpListView.setChildDivider(null);

        mExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                groupAddUpdate(true, groupPosition);
                return true;
            }
        });

        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                priceAddUpdate(true, groupPosition, childPosition);
                return true;
            }
        });

        updateItems();
    }

    private void updateItems() {
        ServiceListAdapter adapter = new ServiceListAdapter(mContext, mGroupItems);
        mExpListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_price_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.menu_add_group:
                groupAddUpdate(false, 0);
                break;

            case R.id.menu_add_price:
                priceAddUpdate(false, 0, 0);
                break;
        }
        return true;
    }

    private void generateDefItems() {

        /*try {

            mGroupItems = new ArrayList<>();

            JSONArray array = JsonHelper.getPriceList();

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);
                String goupName = object.getString(JsonHelper.GROUP_NAME);
                JSONArray priceArray = object.getJSONArray(JsonHelper.PRICE_LIST);
                List<PriceItem> priceItemList = new ArrayList<>();


                for (int j = 0; j < priceArray.length(); j++) {
                    JSONObject priceItem = array.getJSONObject(j);
                    String name = priceItem.getString(JsonHelper.SERVICE_NAME);
                    int price = priceItem.getInt(JsonHelper.SERVICE_PRICE);
                    priceItemList.add(new PriceItem(name, price));
                }

                PriceGroup priceGroup = new PriceGroup(goupName, priceItemList);
                mGroupItems.add(priceGroup);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        List<PriceItem> priceItemList1 = new ArrayList<>();
        priceItemList1.add(new PriceItem("Hair Cut", 120));
        priceItemList1.add(new PriceItem("Shaving & Hair Cut", 180));
        priceItemList1.add(new PriceItem("Hair Color", 220));
        priceItemList1.add(new PriceItem("Child Hair Cut", 130));

        List<PriceItem> priceItemList2 = new ArrayList<>();
        priceItemList2.add(new PriceItem("Hair Cut", 120));
        priceItemList2.add(new PriceItem("Shaving & Hair Cut", 180));
        priceItemList2.add(new PriceItem("Hair Color", 220));
        priceItemList2.add(new PriceItem("Child Hair Cut", 130));

        PriceGroup grpItem1 = new PriceGroup("Hair Style", priceItemList1);
        PriceGroup grpItem2 = new PriceGroup("Hair Color", priceItemList2);
        PriceGroup grpItem3 = new PriceGroup("Oil Massage", priceItemList2);

        mGroupItems = new ArrayList<>();
        mGroupItems.add(grpItem1);
        mGroupItems.add(grpItem2);
        mGroupItems.add(grpItem3);
        mGroupItems.add(grpItem1);

        updateGroupNames();
    }

    private void updateGroupNames() {
        mGroupNames = new String[mGroupItems.size()];
        for (int i = 0; i < mGroupItems.size(); i++) {
            mGroupNames[i] = mGroupItems.get(i).getGroupName();
        }
    }

    private void priceAddUpdate(boolean isUpdate, final int grpIndex, final int itemIndex) {

        mSelectedIndex = grpIndex;

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_update_price_detail, null, false);
        final TextInputEditText nameView = view.findViewById(R.id.name);
        final TextInputEditText priceView = view.findViewById(R.id.price);
        final AppCompatAutoCompleteTextView groupView = view.findViewById(R.id.group);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, mGroupNames);
        groupView.setAdapter(adapter);
        groupView.setText(mGroupNames[grpIndex], false);

        if (isUpdate) {

            final PriceGroup priceGroup = mGroupItems.get(grpIndex);
            final List<PriceItem> priceItems = priceGroup.getPriceItemList();

            final String oldName = priceItems.get(itemIndex).getName();
            final int oldPrice = priceItems.get(itemIndex).getPrice();

            nameView.setText(oldName);
            priceView.setText(oldPrice + "");

            dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                    String newName = nameView.getText().toString();
                    int newPrice = Integer.parseInt(priceView.getText().toString());

                    if (oldName.equals(newName) && oldPrice == newPrice && grpIndex == mSelectedIndex) {
                        return;
                    }

                    if (grpIndex == mSelectedIndex) {
                        priceItems.set(itemIndex, new PriceItem(newName, newPrice));
                        priceGroup.setPriceItemList(priceItems);
                        mGroupItems.set(grpIndex, priceGroup);
                    } else {

                        // Remove from old group
                        priceItems.remove(itemIndex);
                        priceGroup.setPriceItemList(priceItems);
                        mGroupItems.set(grpIndex, priceGroup);

                        // Add to new group
                        PriceGroup newGroup = mGroupItems.get(mSelectedIndex);
                        List<PriceItem> items = newGroup.getPriceItemList();
                        items.add(new PriceItem(newName, newPrice));
                        newGroup.setPriceItemList(items);
                        mGroupItems.set(mSelectedIndex, newGroup);
                    }

                    updateItems();
                }
            });

            dialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    priceItems.remove(itemIndex);
                    priceGroup.setPriceItemList(priceItems);
                    mGroupItems.set(grpIndex, priceGroup);
                    updateItems();
                }
            });

        } else {

            dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                    String newName = nameView.getText().toString();
                    int newPrice = Integer.parseInt(priceView.getText().toString());

                    PriceGroup newGroup = mGroupItems.get(mSelectedIndex);
                    List<PriceItem> items = newGroup.getPriceItemList();
                    items.add(new PriceItem(newName, newPrice));
                    newGroup.setPriceItemList(items);
                    mGroupItems.set(mSelectedIndex, newGroup);

                    updateItems();
                }
            });
        }

        dialog.setView(view);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedIndex = position;
            }
        });

        dialog.setCancelable(true);

        dialog.create().show();
    }

    private void groupAddUpdate(final boolean isUpdate, final int grpIndex) {

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_update_price_group, null, false);
        final TextInputEditText nameView = view.findViewById(R.id.name);

        if (isUpdate) {

            PriceGroup priceGroup = mGroupItems.get(grpIndex);
            String groupName = priceGroup.getGroupName();
            final List<PriceItem> priceItems = priceGroup.getPriceItemList();

            nameView.setText(groupName);

            if (priceItems == null || priceItems.size() <= 0) {
                dialog.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGroupItems.remove(grpIndex);
                        updateItems();
                        updateGroupNames();
                    }
                });
            }

            dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    PriceGroup newGroup = new PriceGroup(nameView.getText().toString(), priceItems);
                    mGroupItems.remove(grpIndex);
                    mGroupItems.add(grpIndex, newGroup);
                    updateItems();
                    updateGroupNames();
                }
            });
        } else {

            dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    List<PriceItem> items = new ArrayList<>();
                    PriceGroup newGroup = new PriceGroup(nameView.getText().toString(), items);
                    mGroupItems.add(newGroup);
                    updateItems();
                    updateGroupNames();
                }
            });
        }

        dialog.setView(view);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setCancelable(true);

        dialog.create().show();
    }
}
