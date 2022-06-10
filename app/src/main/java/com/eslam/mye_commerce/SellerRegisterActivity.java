package com.eslam.mye_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eslam.mye_commerce.databinding.ActivitySellerRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterActivity extends AppCompatActivity {
private ActivitySellerRegisterBinding binding;
FirebaseAuth auth;
    private ProgressDialog lodingbr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding=ActivitySellerRegisterBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        lodingbr = new ProgressDialog(this);

        auth=FirebaseAuth.getInstance();

        binding.sellerAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerRegisterActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        binding.sellerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerseller();
            }
        });
    }

    private void registerseller() {
        String name=binding.editTextTextName.getText().toString();
        String email=binding.editTextEmail.getText().toString();
        String password=binding.editTextpswword.getText().toString();
        String phone=binding.editTextPhone.getText().toString();
        String address=binding.editTextaddress.getText().toString();

if(!name.equals("")&&!email.equals("")&&!password.equals("")&&!phone.equals("")&&!address.equals(""))
{

    lodingbr.setTitle("Create account");
    lodingbr.setMessage("please wait,while we are checking the credentails");
    lodingbr.setCanceledOnTouchOutside(false);
    lodingbr.show();
    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            DatabaseReference rootref= FirebaseDatabase.getInstance().getReference();
            String sid=auth.getUid();
            HashMap<String,Object> sellermap=new HashMap<>();
            sellermap.put("sid",sid);
            sellermap.put("email",email);
            sellermap.put("phone",phone);
            sellermap.put("address",address);
            sellermap.put("name",name);
            sellermap.put("password",password);

rootref.child("Sellers").child(sid).updateChildren(sellermap).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        lodingbr.dismiss();


        Toast.makeText(SellerRegisterActivity.this, "U r regestir Succsfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SellerRegisterActivity.this,SellerHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
});
        }
    });
}
else
{
    Toast.makeText(this, "please complete the registration Form", Toast.LENGTH_SHORT).show();

}

    }
}