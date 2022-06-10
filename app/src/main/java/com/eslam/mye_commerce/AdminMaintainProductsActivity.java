package com.eslam.mye_commerce;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eslam.mye_commerce.databinding.ActivityAdminMaintainProductsBinding;
import com.eslam.mye_commerce.databinding.ActivityProductDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private ActivityAdminMaintainProductsBinding binding;
    private DatabaseReference productsRef;


    private String productID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMaintainProductsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
     //   setContentView(R.layout.activity_admin_maintain_products);


        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);


        displaySpecificProductInfo();



        binding.applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                applyChanges();
            }
        });


  binding.deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteThisProduct();
            }
        });
    }





    private void deleteThisProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
             if(task.isSuccessful()) {
                 Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                 startActivity(intent);
                 finish();

                 Toast.makeText(AdminMaintainProductsActivity.this, "The Product Is deleted successfully.", Toast.LENGTH_SHORT).show();
             }
                 else{
                Toast.makeText(AdminMaintainProductsActivity.this, "erorrRemove "+task.getException(), Toast.LENGTH_SHORT).show();
            }

            }
        });
    }





    private void applyChanges()
    {
        String pNamee = binding.productNameMaintain.getText().toString();
        String pPricee = binding.productPriceMaintain.getText().toString();
        String pDescriptionn = binding.productDescriptionMaintain.getText().toString();

        if (pNamee.equals(""))
        {
            Toast.makeText(this, "Write down Product Name.", Toast.LENGTH_SHORT).show();
        }
        else if (pPricee.equals(""))
        {
            Toast.makeText(this, "Write down Product Price.", Toast.LENGTH_SHORT).show();
        }
        else if (pDescriptionn.equals(""))
        {
            Toast.makeText(this, "Write down Product Description.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("Description", pDescriptionn);
            productMap.put("price", pPricee);
            productMap.put("Pname", pNamee);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(AdminMaintainProductsActivity.this, "erorrchnges "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }





    private void displaySpecificProductInfo()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String pName = dataSnapshot.child("Pname").getValue().toString();
                    Log.d(TAG, "nme"+pName);
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    Log.d(TAG, "price"+pPrice);

                    String pDescription = dataSnapshot.child("Description").getValue().toString();
                    Log.d(TAG, "description"+pDescription);

                    String pImage = dataSnapshot.child("image").getValue().toString();
                    Log.d(TAG, "imge"+pImage);


                    binding.productNameMaintain.setText(pName);
                    binding.productPriceMaintain.setText(pPrice);
                    binding.productDescriptionMaintain.setText(pDescription);
                    Picasso.get().load(pImage).into(binding.productImageMaintain);
                }
                else {
                    Toast.makeText(AdminMaintainProductsActivity.this, "erorr"+dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminMaintainProductsActivity.this, "error "+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}