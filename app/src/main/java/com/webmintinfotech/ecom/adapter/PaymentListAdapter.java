package com.webmintinfotech.ecom.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.webmintinfotech.ecom.R;
import com.webmintinfotech.ecom.databinding.RowPaymentBinding;
import com.webmintinfotech.ecom.model.PaymentlistItem;
import com.webmintinfotech.ecom.utils.Constants;

import java.util.ArrayList;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.PaymentViewHolder> {

    private final Activity context;
    private static ArrayList<PaymentlistItem> paymentOptionList;
    private final OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, String item);
    }

    public PaymentListAdapter(Activity context, ArrayList<PaymentlistItem> paymentOptionList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.paymentOptionList = paymentOptionList;
        this.itemClickListener = itemClickListener;
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {

        private final RowPaymentBinding itemBinding;

        public PaymentViewHolder(@NonNull RowPaymentBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(PaymentlistItem data, Activity context, int position, OnItemClickListener itemClickListener) {
            Resources resources = context.getResources();
            if (data.getIsSelect()) {
                itemBinding.ivCheck.setVisibility(View.VISIBLE);
            } else {
                itemBinding.ivCheck.setVisibility(View.GONE);
            }

            Drawable paymentImage;
            switch (data.getPaymentName()) {
                case "Wallet":
                    paymentImage = ContextCompat.getDrawable(context, R.drawable.ic_wallet);
                    break;
                case "COD":
                    paymentImage = ContextCompat.getDrawable(context, R.drawable.ic_cod);
                    break;
                case "RazorPay":
                    paymentImage = ContextCompat.getDrawable(context, R.drawable.ic_rezorpaypayment);
                    break;
                case "Stripe":
                    paymentImage = ContextCompat.getDrawable(context, R.drawable.ic_stripepayment);
                    break;
                case "Paystack":
                    paymentImage = ContextCompat.getDrawable(context, R.drawable.ic_paystackpayment);
                    break;
                case "Flutterwave":
                    paymentImage = ContextCompat.getDrawable(context, R.drawable.ic_flutterwavepayment);
                    break;
                default:
                    paymentImage = null;
            }
            itemBinding.ivPayment.setImageDrawable(paymentImage);
            itemBinding.tvpaymenttype.setText(data.getPaymentName() + " " + context.getString(R.string.payment));

            itemView.setOnClickListener(v -> {
//                itemClickListener.onItemClick(position, Constants.ItemClick());
                for (int i = 0; i < paymentOptionList.size(); i++) {
//                    paymentOptionList.get(i).isSelect() = false;
                }
//                data.isSelect() = true;
//                notifyDataSetChanged();
            });
        }
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowPaymentBinding binding = RowPaymentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaymentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentlistItem item = paymentOptionList.get(position);
        holder.bind(item, context, position, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return paymentOptionList.size();
    }
}
