package com.eslam.mye_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eslam.mye_commerce.loginandjoin.LoginActivity;
import com.eslam.mye_commerce.loginandjoin.RegisterActivity;
import com.eslam.mye_commerce.loginandjoin.prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private Button join_now_btn, login_btn;
TextView seller_begin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        join_now_btn = findViewById(R.id.main_join_now_btn);
        login_btn = findViewById(R.id.main_login_btn);
        auth = FirebaseAuth.getInstance();
seller_begin=findViewById(R.id.seller_begin);
seller_begin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, SellerRegisterActivity.class);
        startActivity(intent);
    }
});

        Paper.init(this);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        join_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String useremil = Paper.book().read(Prevelant.useremailkey);
        String userpsword = Paper.book().read(Prevelant.userpasswordkey);
        if (useremil != "" && userpsword != "") {
            if (!TextUtils.isEmpty(useremil) && !TextUtils.isEmpty(userpsword)) {


                final String emialtxt = useremil.toString();
                //final String emiltxt = phone.getText().toString();

                final String passwordtxt = userpsword.toString();

                if (emialtxt.isEmpty() || passwordtxt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "please Enter your Email or password", Toast.LENGTH_SHORT).show();


                } else {


                    auth.signInWithEmailAndPassword(emialtxt, passwordtxt).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));

                                        Toast.makeText(MainActivity.this, "Login succ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "erorr" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}