package com.eslam.mye_commerce.loginandjoin;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.eslam.mye_commerce.HomeActivity;
import com.eslam.mye_commerce.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check = "";
    FirebaseAuth auth;

    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();

        check = getIntent().getStringExtra("check");
    }


    @Override
    protected void onStart() {
        super.onStart();
        binding.findEmail.setVisibility(View.GONE);


        if (check.equals("settings")) {

            binding.pageTitle.setText("Set Questions");
            binding.titleQuestions.setText("Please set answer the following  Questions?");
            binding.verifyBtn.setText("Set");

            displynswers();

            binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setnswers();

                }
            });
        } else if (check.equals("login")) {
            binding.findEmail.setVisibility(View.VISIBLE);
            binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    verifyuser();
                    setpssword();
                }
            });

        }
    }

    private void setpssword() {

        String em = binding.findEmail.getText().toString();
        String nswr1 = binding.question1.getText().toString().toLowerCase();
        String nswr2 = binding.question2.getText().toString().toLowerCase();
        auth.sendPasswordResetEmail(em);
        final EditText setpassword = new EditText(ResetPasswordActivity.this);
       setpassword.setHint("Write new ur emil Here...");
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
        builder.setTitle("Reset Password ?");
        builder.setMessage("Enter ur emil to recireve Reset Link .");
        builder.setView(setpassword);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                auth.sendPasswordResetEmail(em).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPasswordActivity.this, "Reset emeil sent..", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this, "reset link is not sent "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

 builder.show();
    }


    private void verifyuser() {
        String em = binding.findEmail.getText().toString();
        String nswr1 = binding.question1.getText().toString().toLowerCase();
        String nswr2 = binding.question2.getText().toString().toLowerCase();
        if (!em.equals("") && !nswr1.equals("") && !nswr2.equals("")) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(auth.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        String emi = snapshot.child("email").getValue().toString();
//            if(em.equals(emi)){
//
//            }

                        if (snapshot.hasChild("Security Questions")) {
                            String nsw1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                            Log.d(TAG, "ns1 " + nsw1);
                            String nsw2 = snapshot.child("Security Questions").child("answer2").getValue().toString();
                            if (!nsw1.equals(nswr1)) {
                                Toast.makeText(ResetPasswordActivity.this, "ur 1st nswer is wrong", Toast.LENGTH_SHORT).show();
                            } else if (!nsw2.equals(nswr2)) {
                                Toast.makeText(ResetPasswordActivity.this, "ur 2st nswer is wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("new Password");

                                final EditText newpassword = new EditText(ResetPasswordActivity.this);
                                newpassword.setHint("Write new Password Here...");
                                builder.setView(newpassword);
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!newpassword.getText().toString().equals("")) {
                                            reference.child("password").setValue(newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
//auth.signOut();
                                                        Toast.makeText(ResetPasswordActivity.this, "password changed succesfully, Login again.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();
                            }

                        }
//            else{
//                Toast.makeText(ResetPasswordActivity.this, "This user emil doesnt exist", Toast.LENGTH_SHORT).show();
//            }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private void setnswers() {
        String nswr1 = binding.question1.getText().toString().toLowerCase();
        String nswr2 = binding.question2.getText().toString().toLowerCase();
        if (nswr1.equals("") && nswr2.equals("")) {
            Toast.makeText(ResetPasswordActivity.this, "plz answer the qs", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
            HashMap<String, Object> userq = new HashMap<>();
            userq.put("answer1", nswr1);
            userq.put("answer2", nswr2);

            reference.child("Security Questions").updateChildren(userq).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "u have answer qs succesfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(i);

                    }
                }
            });
        }
    }

    private void displynswers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());

        reference.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ns1 = snapshot.child("answer1").getValue().toString();
                    String ns2 = snapshot.child("answer2").getValue().toString();
                    binding.question1.setText(ns1);
                    binding.question2.setText(ns2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}