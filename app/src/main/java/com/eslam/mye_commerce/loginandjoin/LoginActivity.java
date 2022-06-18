package com.eslam.mye_commerce.loginandjoin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eslam.mye_commerce.HomeActivity;
import com.eslam.mye_commerce.Model.UserModel;
import com.eslam.mye_commerce.R;
import com.eslam.mye_commerce.admins.AdminHomeActivity;
import com.eslam.mye_commerce.loginandjoin.prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {


    EditText emial, password;
    // ...
    CheckBox remembermecheckbox;
    Button login_btn;
    FirebaseAuth auth;
    String dbnme = "Users";
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    private DatabaseReference mDatabase;
    private ProgressDialog lodingbr;
    private TextView adminLink, notadminLink, ForgetPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        remembermecheckbox = findViewById(R.id.remember_me_checkbox);
        Paper.init(this);
        // if (auth.getCurrentUser() != null) {
        //   startActivity(new Intent(LoginActivity.this, HomeActivity.class));

        // Toast.makeText(this, "alredy signed in", Toast.LENGTH_SHORT).show();
        //finish();
        //}


        myRef = FirebaseDatabase.getInstance().getReference("Admins");
        myRef2 = FirebaseDatabase.getInstance().getReference("Users");


        emial = findViewById(R.id.login_emil_input);
        password = findViewById(R.id.login_pssword_input);
        login_btn = findViewById(R.id.login_btn);
        adminLink = findViewById(R.id.admin_link);
        notadminLink = findViewById(R.id.not_admin_link);
        ForgetPasswordLink=findViewById(R.id.forget_pssword_link);
        lodingbr = new ProgressDialog(this);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (notadminLink.getVisibility() == View.VISIBLE) {
                    loginAdmin();

                } else {
                    loginuser();

                }
            }
        });

        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setText("Login admin");
                adminLink.setVisibility(View.INVISIBLE);
                notadminLink.setVisibility(View.VISIBLE);

            }
        });
        notadminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notadminLink.setVisibility(View.INVISIBLE);
                //     dbnme="Users";
            }
        });
    }

    private void loginuser() {
        final String emialtxt = emial.getText().toString();

        final String passwordtxt = password.getText().toString();

        if (emialtxt.isEmpty() || passwordtxt.isEmpty()) {
            Toast.makeText(LoginActivity.this, "please Enter your Email or password", Toast.LENGTH_SHORT).show();

        } else {
            auth.signInWithEmailAndPassword(emialtxt, passwordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        if (remembermecheckbox.isChecked()) {
                            Paper.book().write(Prevelant.useremailkey, emialtxt);
                            Paper.book().write(Prevelant.userpasswordkey, passwordtxt);
                        }


                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String value = snapshot.child(auth.getUid()).child("email").getValue().toString();
//                                Log.d(TAG, "Value is: " + value);
//                                Log.d(TAG, "emil2 is: " + emialtxt);
                                UserModel usersData = snapshot.child(auth.getUid()).getValue(UserModel.class);

                                if (emialtxt.toString().equals(value.toString())) {
                                    Log.d(TAG, "emil3 is: " + emialtxt);
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    Prevelant.currentOnlineUser = usersData;
                                    startActivity(intent);
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(auth.getUid());
                                    reference.child("password").setValue(passwordtxt.toString());
                                    Toast.makeText(LoginActivity.this, "U r User", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(LoginActivity.this, "error" + task.getException(), Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w(TAG, "Failed to read value.2", error.toException());

                            }
                        });


                    } else {
                        Toast.makeText(LoginActivity.this, "erorr" + task.getException(), Toast.LENGTH_SHORT).show();

                    }


                }
            });

        }
    }

    private void loginAdmin() {
        final String emialtxt = emial.getText().toString();

        final String passwordtxt = password.getText().toString();

        if (emialtxt.isEmpty() || passwordtxt.isEmpty()) {
            Toast.makeText(LoginActivity.this, "please Enter your Email or password", Toast.LENGTH_SHORT).show();

        } else {


            auth.signInWithEmailAndPassword(emialtxt, passwordtxt).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (remembermecheckbox.isChecked()) {
                                    Paper.book().write(Prevelant.useremailkey, emialtxt);
                                    Paper.book().write(Prevelant.userpasswordkey, passwordtxt);
                                }


                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String value = dataSnapshot.child(auth.getUid()).child("email").getValue().toString();
                                        Log.d(TAG, "Value is: " + value);
                                        Log.d(TAG, "emil2 is: " + emialtxt);

                                        if (emialtxt.toString().equals(value.toString())) {
                                            Log.d(TAG, "emil3 is: " + emialtxt);

                                            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));

                                            Toast.makeText(LoginActivity.this, "U r Admin", Toast.LENGTH_SHORT).show();


                                        } else {
                                            //
                                            Toast.makeText(LoginActivity.this, "error" + task.getException(), Toast.LENGTH_SHORT).show();

                                            //
//


                                            //Log.d(TAG, "emil4 is: " + emialtxt);
//
//    startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                                            lodingbr.setTitle("already Loged in");
                                            lodingbr.setMessage("please wait...");
                                            lodingbr.setCanceledOnTouchOutside(false);
                                            lodingbr.show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Log.w(TAG, "Failed to read value.", error.toException());
                                    }
                                });


                            } else {
                                Toast.makeText(LoginActivity.this, "erorr" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}