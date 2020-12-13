package com.jrinfolab.beautyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.pojo.PriceItem;

import java.util.List;

public class NewBillListAdapter extends RecyclerView.Adapter<NewBillListAdapter.MyViewHolder> {

    List<PriceItem> priceItems;
    Context context;
    ItemClickListener itemClickListener;

    public NewBillListAdapter(Context context, List<PriceItem> priceItems, ItemClickListener itemClickListener) {
        this.context = context;
        this.priceItems = priceItems;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_add_bill_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        PriceItem item = priceItems.get(position);
        holder.number.setText(position + 1 + ". ");
        holder.name.setText(item.getName());
        holder.price.setText(context.getString(R.string.text_rupees_symbol) + item.getPrice());

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(holder.name, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return priceItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView number, name, price;
        RelativeLayout rootLayout;

        public MyViewHolder(@NonNull View view) {
            super(view);
            rootLayout = view.findViewById(R.id.root_layout);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            number = view.findViewById(R.id.number);
        }
    }
}
