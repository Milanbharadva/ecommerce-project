package com.webmintinfotech.ecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.model.InnersubcategoryItem;
import com.webmintinfotech.ecom.ui.activity.ActSubCateProduct;

import java.util.ArrayList;

public class SubInnerCateAdapter extends RecyclerView.Adapter<SubInnerCateAdapter.ViewHolder> {

    private final ArrayList<InnersubcategoryItem> mList;

    public SubInnerCateAdapter(ArrayList<InnersubcategoryItem> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_all_inner_sub_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InnersubcategoryItem innersubcategory = mList.get(position);
        holder.textView.setText(innersubcategory.getInnersubcategoryName());
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ActSubCateProduct.class);
            intent.putExtra("innersubcategory_id", String.valueOf(innersubcategory.getId()));
            intent.putExtra("title", innersubcategory.getInnersubcategoryName());
            intent.putExtra("gone", "gone");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvinnersubcateitemname);
        }
    }
}
