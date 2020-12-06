package com.jrinfolab.beautyshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.pojo.Employee;
import com.jrinfolab.beautyshop.ui.ImageViewer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {

    List<Employee> empList;
    Context context;
    ItemClickListener itemClickListener;

    public EmployeeListAdapter(Context context, List<Employee> empList, ItemClickListener itemClickListener) {
        this.context = context;
        this.empList = empList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_employee_info, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final Employee emp = empList.get(position);
        holder.name.setText(emp.getName());
        holder.phone.setText(emp.getPhone());
        holder.branchName.setText(emp.getBranchName());
        holder.address.setText(emp.getAddress());

        File file = new File(emp.getImageUrl());
        Uri uri = Uri.fromFile(file);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            holder.userImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });

        holder.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });

        holder.actionCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView name, address, phone, branchName;
        MaterialButton actionEdit, actionDelete, actionCall;
        ImageView userImage;

        public MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            phone = view.findViewById(R.id.phone);
            branchName = view.findViewById(R.id.branch_name);
            userImage = view.findViewById(R.id.user_image);

            actionEdit = view.findViewById(R.id.action_edit);
            actionCall = view.findViewById(R.id.action_call);
            actionDelete = view.findViewById(R.id.action_delete);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(View view, int position);
    }
}
