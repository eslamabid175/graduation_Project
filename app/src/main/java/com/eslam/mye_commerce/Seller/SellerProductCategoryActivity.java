package com.eslam.mye_commerce.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.eslam.mye_commerce.admins.SellerAddNewProductsActivity;
import com.eslam.mye_commerce.databinding.ActivitySellerProductCategoryBinding;

public class SellerProductCategoryActivity extends AppCompatActivity {
    private ActivitySellerProductCategoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerProductCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.imageViewTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "tShirts");
                startActivity(intent);
            }
        });
        binding.imageViewSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "Sports");
                startActivity(intent);
            }
        });
        binding.imageViewFemlDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "FemlDresses");
                startActivity(intent);
            }
        });
        binding.imageViewSwetshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "Swetshirt");
                startActivity(intent);
            }
        });
        binding.imageViewGlsses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "Glsses");
                startActivity(intent);
            }
        });
        binding.imageViewPursebge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "pursebge");
                startActivity(intent);
            }
        });
        binding.imageViewHats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "hats");
                startActivity(intent);
            }
        });
        binding.imageViewShoess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "shoess");
                startActivity(intent);
            }
        });
        binding.imageViewHeadphoness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "headphoness");
                startActivity(intent);
            }
        });
        binding.imageViewLaptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
            }
        });
        binding.imageViewWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "watches");
                startActivity(intent);
            }
        });
        binding.imageViewMobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductsActivity.class);
                intent.putExtra("category", "Mobiles");
                startActivity(intent);
            }
        });


    }
}