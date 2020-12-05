package com.jrinfolab.beautyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.pojo.PriceGroup;
import com.jrinfolab.beautyshop.pojo.PriceItem;

import java.util.List;

public class ServiceListAdapter extends BaseExpandableListAdapter {

    // https://androidexample.com/Custom_Expandable_ListView_Tutorial_-_Android_Example/index.php?view=article_discription&aid=107&aaid=129
    Context context;
    List<PriceGroup> list;

    public ServiceListAdapter(Context context, List<PriceGroup> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        PriceGroup groupItem = list.get(groupPosition);
        String groupName = groupItem.getGroupName();
        List<PriceItem> priceList = groupItem.getPriceItemList();
        return priceList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        PriceGroup groupItem = list.get(groupPosition);
        return (groupItem.getPriceItemList()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

        view = LayoutInflater.from(context).inflate(R.layout.item_price_group_row, parent, false);
        TextView textView = view.findViewById(R.id.text);

        PriceGroup groupItem = list.get(groupPosition);
        String groupName = groupItem.getGroupName();

        // Always show expanded group
        ((ExpandableListView) parent).expandGroup(groupPosition);

        textView.setText(groupName);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_price_detail_row, parent, false);

        TextView name = view.findViewById(R.id.name);
        TextView price = view.findViewById(R.id.price);

        PriceGroup groupItem = list.get(groupPosition);
        PriceItem priceItem = (groupItem.getPriceItemList()).get(childPosition);

        name.setText(priceItem.getName());
        price.setText("₹  " + priceItem.getPrice());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
