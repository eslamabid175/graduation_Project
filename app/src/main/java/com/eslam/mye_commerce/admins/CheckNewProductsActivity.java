package com.eslam.mye_commerce.admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eslam.mye_commerce.ItemClickListner;
import com.eslam.mye_commerce.Model.Products;
import com.eslam.mye_commerce.R;
import com.eslam.mye_commerce.ViewHolder.ProductViewHolder;
import com.eslam.mye_commerce.databinding.ActivityCheckNewProductsBinding;
import com.eslam.mye_commerce.databinding.ActivitySellerProductCategoryBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CheckNewProductsActivity extends AppCompatActivity {
    private ActivityCheckNewProductsBinding binding;
private DatabaseReference unveriviedProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckNewProductsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
unveriviedProducts= FirebaseDatabase.getInstance().getReference().child("Products");



binding.checkAndAbroveProductsRecycler.setHasFixedSize(true);
        binding.checkAndAbroveProductsRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unveriviedProducts.orderByChild("productState").equalTo("not aproved"),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder
                    , @SuppressLint("RecyclerView") int position, @NonNull Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price : " + model.getPrice() + " $");
                Picasso.get().load(model.getImage()).into(holder.imageView);

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        final String productid= model.getPid();

        CharSequence options[]=new CharSequence[]{
                "Yes",
                "No"

        };
        AlertDialog.Builder builder=new AlertDialog.Builder(CheckNewProductsActivity.this);
        builder.setTitle("Do you want to approve this Product. are you sure?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(position==0){changeProductState(productid);}
                if(position==1){

                }
            }
        });
        builder.show();
    }
});
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
binding.checkAndAbroveProductsRecycler.setAdapter(adapter);
adapter.startListening();
    }

    private void changeProductState(String productid) {
        unveriviedProducts.child(productid).child("productState").setValue("approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CheckNewProductsActivity.this, "The item has approved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}