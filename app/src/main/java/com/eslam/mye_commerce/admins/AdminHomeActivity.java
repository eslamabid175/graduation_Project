package com.eslam.mye_commerce.admins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eslam.mye_commerce.AdminsNewOrdersActivity;
import com.eslam.mye_commerce.HomeActivity;
import com.eslam.mye_commerce.MainActivity;
import com.eslam.mye_commerce.R;
import com.eslam.mye_commerce.databinding.ActivityAdminHomeBinding;
import com.eslam.mye_commerce.databinding.ActivitySellerRegisterBinding;

public class AdminHomeActivity extends AppCompatActivity {
    private ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminHomeBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        binding.maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });


        binding.adminLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        binding.checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminsNewOrdersActivity.class);
                startActivity(intent);
            }
        });
binding.checkAndAbroveProductsBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(AdminHomeActivity.this,CheckNewProductsActivity.class);
        startActivity(intent);
    }
});


    }
}