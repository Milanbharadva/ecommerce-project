package com.webmintinfotech.ecom.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.databinding.ActProductDetailsBinding;
import com.webmintinfotech.ecom.databinding.RowProductsizeBinding;
import com.webmintinfotech.ecom.model.VariationsItem;

import java.util.ArrayList;

public class VariationAdapter extends RecyclerView.Adapter<VariationAdapter.RowProductViewHolder> {

    private final Activity context;
    private final ArrayList<VariationsItem> productList;
    private final String taxPercent;
    private final String tax;
    private final ActProductDetailsBinding productDetailsBinding;
    private final ItemClickListener itemClick;

    public VariationAdapter(Activity context, ArrayList<VariationsItem> productList, String taxPercent, String tax, ActProductDetailsBinding productDetailsBinding, ItemClickListener itemClick) {
        this.context = context;
        this.productList = productList;
        this.taxPercent = taxPercent;
        this.tax = tax;
        this.productDetailsBinding = productDetailsBinding;
        this.itemClick = itemClick;
    }

    public interface ItemClickListener {
        void onItemClick(int position, String action);
    }

    public class RowProductViewHolder extends RecyclerView.ViewHolder {
        private final RowProductsizeBinding binding;

        public RowProductViewHolder(RowProductsizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(VariationsItem data, Activity context, ActProductDetailsBinding productDetailsBinding, int position, ItemClickListener itemClick) {
            if (data.getVariation() == null) {
                productDetailsBinding.tvproductdesc.setVisibility(View.GONE);
                productDetailsBinding.rvproductSize.setVisibility(View.GONE);
            } else {
                productDetailsBinding.rvproductSize.setVisibility(View.VISIBLE);
                productDetailsBinding.tvproductdesc.setVisibility(View.VISIBLE);
                binding.tvproductsizeS.setText(data.getVariation());
            }
            binding.tvproductsizeS.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.size_gray_border, null));
            if (data.getSelect()) {
                binding.tvproductsizeS.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.size_blue_border, null));
            }
            Log.d("variations-->", data.getVariation());
            itemView.setOnClickListener(v -> itemClick.onItemClick(position, "ItemClick"));
        }
    }

    @Override
    public RowProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowProductsizeBinding binding = RowProductsizeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RowProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RowProductViewHolder holder, int position) {
        holder.bind(productList.get(position), context, productDetailsBinding, position, itemClick);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
