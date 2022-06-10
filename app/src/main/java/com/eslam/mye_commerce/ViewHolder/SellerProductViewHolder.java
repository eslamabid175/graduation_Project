package com.eslam.mye_commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eslam.mye_commerce.ItemClickListner;
import com.eslam.mye_commerce.R;

public class SellerProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView txtProductName, txtProductDescription, txtProductPrice,productState;
public ImageView imageView;
public ItemClickListner listner;


public SellerProductViewHolder(View itemView) {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_seller_image);
        productState=(TextView) itemView.findViewById(R.id.product_seller_state);
        txtProductName = (TextView) itemView.findViewById(R.id.product_seller_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_seller_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_seller_price);
        }

public void setItemClickListner(ItemClickListner listner) {
        this.listner = listner;
        }

@Override
public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
        }
        }