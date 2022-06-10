package com.eslam.mye_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eslam.mye_commerce.databinding.ActivitySellerLoginBinding;
import com.eslam.mye_commerce.databinding.ActivitySellerRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    private ActivitySellerLoginBinding binding;
    private ProgressDialog lodingbr;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySellerLoginBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
auth=FirebaseAuth.getInstance();

        lodingbr = new ProgressDialog(this);

        binding.sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loginSeller();
            }
        });
    }

    private void loginSeller() {
        String email=binding.editTextTextemil.getText().toString();
        String password=binding.editTextpassword.getText().toString();

        if(!email.equals("")&&!password.equals(""))
        {

            lodingbr.setTitle("Login...");
            lodingbr.setMessage("please wait,while we are checking the credentails");
            lodingbr.setCanceledOnTouchOutside(false);
            lodingbr.show();
auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
   if(task.isSuccessful()){


       Toast.makeText(SellerLoginActivity.this, "U r Login Succsfully", Toast.LENGTH_SHORT).show();
lodingbr.dismiss();
       Intent intent = new Intent(SellerLoginActivity.this,SellerHomeActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);
       finish();
   }
    }
});
        }
        else {
            Toast.makeText(this, "plz fill EditTexts", Toast.LENGTH_SHORT).show();
        }
    }
}