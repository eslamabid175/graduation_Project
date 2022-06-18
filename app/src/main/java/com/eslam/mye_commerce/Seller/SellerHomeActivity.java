package com.eslam.mye_commerce.Seller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eslam.mye_commerce.MainActivity;
import com.eslam.mye_commerce.Model.Products;
import com.eslam.mye_commerce.R;
import com.eslam.mye_commerce.ViewHolder.SellerProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
private RecyclerView recyclerView;
private DatabaseReference reference;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intenthome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intenthome);                    return true;
                case R.id.navigation_add:

                    Intent intentct = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intentct);
                    return true;
                case R.id.navigation_logout:

final FirebaseAuth auth;
auth=FirebaseAuth.getInstance();
auth.signOut();
                    Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + item.getItemId());

        }
        return false;
    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
reference= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.seller_home_recycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("Selleruid").equalTo(FirebaseAuth.getInstance()
                        .getUid()),Products.class).build();
        FirebaseRecyclerAdapter<Products, SellerProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, SellerProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerProductViewHolder holder
                            , @SuppressLint("RecyclerView") int position, @NonNull Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price : " + model.getPrice() + " $");
                        holder.productState.setText("State : "+model.getProductState());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String productid= model.getPid();

                                CharSequence options[]=new CharSequence[]{
                                        "Yes",
                                        "No"

                                };
                                AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to delete this Product. are you sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i==0){deleteProduct(productid);}
                                        if(i==1){

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        SellerProductViewHolder holder = new SellerProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productid) {
        reference.child(productid).
               removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SellerHomeActivity.this, "The item has deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SellerHomeActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

