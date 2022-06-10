package com.eslam.mye_commerce.loginandjoin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eslam.mye_commerce.MainActivity;
import com.eslam.mye_commerce.Model.UserModel;
import com.eslam.mye_commerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.CheckBox;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase db;
    CheckBox dmin_checkbox;
    private Button create_account_btn;
    private EditText inputName, inputpassword, inputphone, inputpassword2, inputemail, dminconfirm;
    private ProgressDialog lodingbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//
//confirm_code=findViewById(R.id.register_confirm_admin_input);
//register_dmin=findViewById(R.id.register_admin_link);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        dmin_checkbox = findViewById(R.id.register_dmin_checkbox);
        dminconfirm = findViewById(R.id.register_confirmm_admin_input);
        create_account_btn = findViewById(R.id.register_btn);
        inputName = findViewById(R.id.register_user_nme_input);
        inputemail = findViewById(R.id.register_emil_input);

        inputphone = findViewById(R.id.register_phone_number_input);
        inputpassword = findViewById(R.id.register_pssword_input);
        inputpassword2 = findViewById(R.id.register_pssword2_input);
        lodingbr = new ProgressDialog(this);

        dmin_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    dminconfirm.setVisibility(View.VISIBLE);
                } else {
                    dminconfirm.setVisibility(View.INVISIBLE);

                }
            }
        });
        dminconfirm.setVisibility(View.VISIBLE);


        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dmin_checkbox.isChecked() == true && (dminconfirm.getText().toString()).equals("123")) {

                    crete_dmin_ccount();
                } else {
                    createaccount();
                }
            }
        });

    }

    private void createaccount() {
        String nme = inputName.getText().toString();
        String emil = inputemail.getText().toString();

        String phone = inputphone.getText().toString();
        String pssword = inputpassword.getText().toString();
        String pssword2 = inputpassword2.getText().toString();

        if (TextUtils.isEmpty(nme)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(emil)) {
            Toast.makeText(this, "Please write your emil...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pssword)) {
            Toast.makeText(this, "Please write your pssword...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pssword2)) {
            Toast.makeText(this, "Please Confirm your pssword...", Toast.LENGTH_SHORT).show();
        } else if (!pssword.equals(pssword2)) {
            Toast.makeText(this, "Please rewrite your pssword...", Toast.LENGTH_SHORT).show();
        } else {

            lodingbr.setTitle("Create account");
            lodingbr.setMessage("please wait,while we are checking the credentails");
            lodingbr.setCanceledOnTouchOutside(false);
            lodingbr.show();

            auth.createUserWithEmailAndPassword(emil, pssword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getUser().getUid();
                        UserModel userModel = new UserModel(id, nme, emil, phone, pssword);
                        db.getReference().child("Users").child(id).setValue(userModel);

                        Toast.makeText(RegisterActivity.this, "User Rigester Succfully", Toast.LENGTH_SHORT).show();
                        lodingbr.dismiss();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        lodingbr.dismiss();
                    }
                }
            });

        }
    }

    public void crete_dmin_ccount() {
        String nme = inputName.getText().toString();
        String emil = inputemail.getText().toString();
        String phone = inputphone.getText().toString();
        String pssword = inputpassword.getText().toString();
        String pssword2 = inputpassword2.getText().toString();

        if (TextUtils.isEmpty(nme)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(emil)) {
            Toast.makeText(this, "Please write your emil...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pssword)) {
            Toast.makeText(this, "Please write your pssword...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pssword2)) {
            Toast.makeText(this, "Please Confirm your pssword...", Toast.LENGTH_SHORT).show();
        } else if (!pssword.equals(pssword2)) {
            Toast.makeText(this, "Please rewrite your pssword...", Toast.LENGTH_SHORT).show();
        } else {
            lodingbr.setTitle("Create admin account");
            lodingbr.setMessage("please wait,while we are checking the credentails");
            lodingbr.setCanceledOnTouchOutside(false);
            lodingbr.show();

            auth.createUserWithEmailAndPassword(emil, pssword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = task.getResult().getUser().getUid();
                        UserModel userModel = new UserModel(id, nme, emil, phone, pssword);

                        db.getReference().child("Admins").child(id).setValue(userModel);

                        Toast.makeText(RegisterActivity.this, "U become Admin Succfully", Toast.LENGTH_SHORT).show();
Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
startActivity(intent);
                        lodingbr.dismiss();

                    } else {
                        Toast.makeText(RegisterActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        lodingbr.dismiss();
                    }
                }
            });

        }
    }

}