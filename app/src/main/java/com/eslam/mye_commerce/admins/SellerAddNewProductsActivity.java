package com.eslam.mye_commerce.admins;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eslam.mye_commerce.SellerHomeActivity;
import com.eslam.mye_commerce.SellerProductCategoryActivity;
import com.eslam.mye_commerce.databinding.ActivitySellerAddNewProductsBinding;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductsActivity extends AppCompatActivity {

    private static final int gallerypic = 1;
    private ActivitySellerAddNewProductsBinding binding;
    private String categoryName, pname, descriotion, price, saveCurrentDate, saveCurrentTime, productrandomKey, downloadimageurl;
    private Uri ImageUri;
    private StorageReference productImageref;
    private DatabaseReference productRef,sellerRef;
    private ProgressDialog lodingbr;
    private Context context;
private String sName,sid,saddress,sEmail,sPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerAddNewProductsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        categoryName = getIntent().getExtras().get("category").toString();
        productImageref = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");
        lodingbr = new ProgressDialog(this);



//    Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();
        binding.selectProductImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opengallery();
            }
        });
        binding.addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validteProductData();
            }
        });
        sellerRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               sName=snapshot.child("name").getValue().toString();
               sid=snapshot.child("sid").getValue().toString();
               saddress=snapshot.child("address").getValue().toString();
               sEmail=snapshot.child("email").getValue().toString();
               sPhone=snapshot.child("phone").getValue().toString();

           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void opengallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallerypic);
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallerypic && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();

            binding.selectProductImge.setImageURI(ImageUri);


        } else {
            Toast.makeText(this, "Error: no imges", Toast.LENGTH_SHORT).show();
        }
    }

    private void validteProductData() {
        pname = binding.productName.getText().toString();
        descriotion = binding.productDescription.getText().toString();
        price = binding.productPrice.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "there is no product image!!..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pname)) {
            Toast.makeText(this, "please write product name", Toast.LENGTH_SHORT).show();


        } else if (TextUtils.isEmpty(descriotion)) {
            Toast.makeText(this, "please write product description", Toast.LENGTH_SHORT).show();


        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "please write product price", Toast.LENGTH_SHORT).show();


        } else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {

        lodingbr.setTitle("adding new Product");
        lodingbr.setMessage("Dear admin,please wait...");
        lodingbr.setCanceledOnTouchOutside(false);
        lodingbr.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        productrandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = productImageref.child(ImageUri.getLastPathSegment() + productrandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(SellerAddNewProductsActivity.this, "Erorr: " + message, Toast.LENGTH_SHORT).show();
                lodingbr.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductsActivity.this, "Product Image uploded Sucssesfully..", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downloadimageurl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadimageurl = task.getResult().toString();
                            Toast.makeText(SellerAddNewProductsActivity.this, "Product Image uri saved", Toast.LENGTH_SHORT).show();
                            saveproductinfotodb();

                        }
                    }
                });

            }
        });
    }

    private void saveproductinfotodb() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productrandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("Description", descriotion);
        productMap.put("Pname", pname);
        productMap.put("image", downloadimageurl);
        productMap.put("price", price);


        productMap.put("sellerName", sName);
        productMap.put("selleraddress", saddress);
        productMap.put("sellerPhone", sPhone);
        productMap.put("SellerEmail", sEmail);
        productMap.put("Selleruid", sid);



        productMap.put("productState", "not aproved");

        productRef.child(productrandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Intent intent = new Intent(SellerAddNewProductsActivity.this, SellerHomeActivity.class);
                    startActivity(intent);
                    lodingbr.dismiss();
                    Toast.makeText(SellerAddNewProductsActivity.this, "Product added succesfully..", Toast.LENGTH_SHORT).show();
                } else {
                    lodingbr.dismiss();
                    String m = task.getException().toString();
                    Toast.makeText(SellerAddNewProductsActivity.this, "Error: " + m, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}