//package com.webmintinfotech.ecom.base;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewbinding.ViewBinding;
//import java.util.ArrayList;
//
//public abstract class BaseAdaptor<T, Binding extends ViewBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private final Activity context;
//    private final ArrayList<T> items;
//    private Binding binding;
//
//    public BaseAdaptor(Activity context, ArrayList<T> items) {
//        this.context = context;
//        this.items = items;
//    }
//
//    protected abstract void onBindData(RecyclerView.ViewHolder holder, T item, int position);
//    protected abstract int setItemLayout();
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        binding = getBinding(parent);
//        ViewHolder viewHolder = new ViewHolder(binding);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        onBindData(holder, items.get(position), position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    protected abstract Binding getBinding(ViewGroup parent);
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private Binding binding;
//
//        public ViewHolder(ViewBinding binding) {
//            super(binding.getRoot());
//        }
//
//
//        public Binding getBinding() {
//            return binding;
//        }
//
//        public void setBinding(Binding binding) {
//            this.binding = binding;
//        }
//    }
//}

package com.webmintinfotech.ecom.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

public abstract class BaseAdaptor<T, Binding extends ViewBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity context;
    private ArrayList<T> items;
    private Binding binding;

    public BaseAdaptor(Activity context, ArrayList<T> items) {
        this.context = context;
        this.items = items;
    }

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val, int position);
    public abstract int setItemLayout();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = getBinding(parent);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindData(holder, items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public abstract Binding getBinding(ViewGroup parent);

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Binding binding;

        public ViewHolder(Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public Binding getBinding() {
            return binding;
        }
    }
}

