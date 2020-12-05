package com.jrinfolab.beautyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.pojo.Branch;
import com.jrinfolab.beautyshop.ui.ImageViewer;

import java.util.List;

public class BranchListAdapter extends RecyclerView.Adapter<BranchListAdapter.MyViewHolder> {

    List<Branch> branchList;
    Context context;
    ItemClickListener itemClickListener;

    public BranchListAdapter(Context context, List<Branch> branches, ItemClickListener itemClickListener) {
        this.context = context;
        this.branchList = branches;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_branch_card, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Branch branch = branchList.get(position);
        holder.name.setText(branch.getName());
        holder.address.setText(branch.getAddress());
        holder.imageCount.setText(branch.getPhotoList().length + " Images Added");

        holder.viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] imagePath = branch.getPhotoList();
                Intent intent = new Intent(context, ImageViewer.class);
                intent.putExtra(Constant.IMAGE_PATH_LIST, imagePath);
                context.startActivity(intent);
            }
        });

        holder.deleteBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });

        holder.editBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView name, address, imageCount;
        MaterialButton viewImage, editBranch, deleteBranch;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            imageCount = view.findViewById(R.id.image_count);
            viewImage = view.findViewById(R.id.action_view_image);
            editBranch = view.findViewById(R.id.action_edit_info);
            deleteBranch = view.findViewById(R.id.action_delete_branch);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(View view, int position);
    }
}
