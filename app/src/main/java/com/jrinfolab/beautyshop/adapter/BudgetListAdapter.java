package com.jrinfolab.beautyshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.pojo.Budget;

import java.util.List;

public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.MyViewHolder> {

    List<Budget> transactionList;
    Context context;
    ItemClickListener itemClickListener;

    public BudgetListAdapter(Context context, List<Budget> transactionList, ItemClickListener itemClickListener) {
        this.context = context;
        this.transactionList = transactionList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_budget_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Budget transaction = transactionList.get(position);

        int roundColor = transaction.getType() == 0 ? R.color.color_error : R.color.color_verified;
        int drawable = transaction.getType() == 0 ? R.drawable.ic_category_expense : R.drawable.ic_category_income;

        holder.icon.setImageDrawable(context.getResources().getDrawable(drawable, null));
        holder.icon.setColorFilter(ContextCompat.getColor(context, roundColor));

        holder.amount.setText("Rs " + transaction.getAmount());
        holder.category.setText(transaction.getCategory());
        holder.note.setText(transaction.getNote());

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rootLayout;
        TextView category, note, amount;
        ImageView icon;

        public MyViewHolder(@NonNull View view) {
            super(view);
            category = view.findViewById(R.id.category);
            note = view.findViewById(R.id.note);
            amount = view.findViewById(R.id.amount);
            icon = view.findViewById(R.id.icon);
            rootLayout = view.findViewById(R.id.root_layout);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(View view, int position);
    }
}
