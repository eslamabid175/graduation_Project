package com.eslam.mye_commerce.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eslam.mye_commerce.HomeActivity;
import com.eslam.mye_commerce.Model.Cart;
import com.eslam.mye_commerce.R;
import com.eslam.mye_commerce.ViewHolder.CartViewHolder;
import com.eslam.mye_commerce.databinding.ActivityCartBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    private ActivityCartBinding binding;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.cartList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.cartList.setLayoutManager(layoutManager);

        auth = FirebaseAuth.getInstance();


        binding.nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                binding.totalPrice.setText("Total Price = " + String.valueOf(overTotalPrice) + " $");

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
//        String s=getIntent().getStringExtra("Total Price");
//        binding.totalPrice.setText("Total Price = " + String.valueOf(s) + " $");

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(auth.getUid())
                                .child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price " + model.getPrice() + " $");
                holder.txtProductName.setText(model.getPname());

                int oneTyprProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTyprProductTPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    cartListRef.child("User View")
                                            .child(auth.getUid())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_item, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        binding.cartList.setAdapter(adapter);
        adapter.startListening();
    }


    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(auth.getUid());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        binding.totalPrice.setText("Dear " + userName + "\n order is shipped successfully.");
                        binding.cartList.setVisibility(View.GONE);

                        binding.msg1.setVisibility(View.VISIBLE);
                        binding.totalPrice.setText("Shipping State = Shipped");
                        binding.msg1.setText("Congratulations, your final order has been Shipped successfully. Soon you will received your order at your door step.");
                        binding.nextBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    } else if (shippingState.equals("not shipped")) {
                        binding.totalPrice.setText("Shipping State = Not Shipped");
                        binding.cartList.setVisibility(View.VISIBLE);
                        binding.msg1.setText("Products Not Shipped yet");

                        binding.msg1.setVisibility(View.VISIBLE);
                        binding.nextBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, " products not shipped yet, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}