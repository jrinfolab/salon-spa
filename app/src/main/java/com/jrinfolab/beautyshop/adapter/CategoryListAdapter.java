package com.jrinfolab.beautyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.pojo.Category;
import com.jrinfolab.beautyshop.ui.ImageViewer;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    List<Category> categories;
    Context context;
    ItemClickListener itemClickListener;

    public CategoryListAdapter(Context context, List<Category> categories, ItemClickListener itemClickListener) {
        this.context = context;
        this.categories = categories;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_category_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Category category = categories.get(position);
        holder.name.setText(category.getName());
        int roundColor = category.getType() == 0 ? R.color.color_error : R.color.color_verified ;
        int drawable = category.getType() == 0 ? R.drawable.ic_category_expense : R.drawable.ic_category_income ;

        holder.icon.setBackgroundColor(context.getColor(roundColor));
        holder.icon.setImageDrawable(context.getDrawable(drawable));

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rootLayout;
        TextView name;
        ShapeableImageView icon;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            icon = view.findViewById(R.id.icon);
            rootLayout = view.findViewById(R.id.root_layout);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(View view, int position);
    }
}
