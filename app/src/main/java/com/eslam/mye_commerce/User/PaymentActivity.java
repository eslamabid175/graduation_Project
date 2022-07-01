package com.eslam.mye_commerce.User;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eslam.mye_commerce.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
Toolbar toolbar;
double amount=1.0;
TextView subtotl,discount,shiping,totl,tot;
Button pyment_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

    toolbar=findViewById(R.id.payment_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    subtotl=findViewById(R.id.sub_total);
    discount=findViewById(R.id.textView17);
    shiping=findViewById(R.id.textView18);
    totl=findViewById(R.id.total_amt);
    pyment_btn=findViewById(R.id.pay_btn);
    tot=findViewById(R.id.total_amt);
String ammount =getIntent().getStringExtra("amount");
        amount=Double.parseDouble((ammount));
tot.setText(ammount+"$");
      //   Log.d(TAG, "mount " +ammount+",inte "+amount );

        pyment_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pymentMethod();
        }
    });


    }

    private void pymentMethod() {
        Checkout checkout = new Checkout();

        final Activity activity = PaymentActivity.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "My E-Commerce App");
            //Ref no
            options.put("description", "Reference No. #123456");
            //Image to be display
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "USD");
            //double total = Double.parseDouble(mAmountText.getText().toString());
           amount = amount * 100;
            //amount
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", "developer.kharag@gmail.com");
            //contact
            preFill.put("contact", "7489347378");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
    }

   @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Payment Cancel "+s, Toast.LENGTH_SHORT).show();
       Intent intent=new Intent(PaymentActivity.this, ConfirmFinalOrderActivity.class);
       startActivity(intent);
    }
}