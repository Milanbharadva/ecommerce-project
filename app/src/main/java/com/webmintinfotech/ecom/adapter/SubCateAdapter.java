package com.webmintinfotech.ecom.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.model.SubcategoryItem;

import java.util.ArrayList;

public class SubCateAdapter extends RecyclerView.Adapter<SubCateAdapter.ViewHolder> {

    private final ArrayList<SubcategoryItem> mList;

    public SubCateAdapter(ArrayList<SubcategoryItem> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_all_sub_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubcategoryItem ItemsViewModel = mList.get(position);
        holder.textView.setText(ItemsViewModel.getSubcategoryName());
        boolean isVisible = ItemsViewModel.isExpand();
        holder.recyclerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(v -> {
            if (ItemsViewModel.getInnersubcategory() == null || ItemsViewModel.getInnersubcategory().isEmpty()) {
                holder.recyclerView.setClickable(false);
            } else {
                ItemsViewModel.setExpand(!ItemsViewModel.isExpand());
                Log.d("isVisible", String.valueOf(isVisible));
                notifyItemChanged(position);
            }
        });
        SubInnerCateAdapter adapter = ItemsViewModel.getInnersubcategory() != null ?
                new SubInnerCateAdapter(ItemsViewModel.getInnersubcategory()) : null;
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvSubcateitemname);
            recyclerView = itemView.findViewById(R.id.rvinnersubcate);
        }
    }
}
