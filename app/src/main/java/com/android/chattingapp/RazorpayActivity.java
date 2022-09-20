package com.android.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {
String payid,paystat,phone,uid;

    private static final String TAG = "Razorpay";
    Checkout checkout;
    RazorpayClient razorpayClient;
    Order order;

    private String order_receipt_no = "Receipt No. " +  System.currentTimeMillis()/1000;
    private String order_reference_no = "Reference No. #" +  System.currentTimeMillis()/1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);
        Checkout.preload(getApplicationContext());
        Intent intent = getIntent();
//        payid = intent.getStringExtra("payid");
//        paystat = intent.getStringExtra("paystatus");
        phone = intent.getStringExtra("phone");
        uid = intent.getStringExtra("uid");
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        textView = findViewById(R.id.textView);


        // Initialize client
        try {
            razorpayClient = new RazorpayClient(getResources().getString(R.string.razorpay_key_id), getResources().getString(R.string.razorpay_secret_key));
            Checkout.preload(getApplicationContext());
            checkout = new Checkout();
        } catch (RazorpayException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<String, String>();
        razorpayClient.addHeaders(headers);

        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", 100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", order_receipt_no);
            orderRequest.put("payment_capture", true);

            order = razorpayClient.Orders.create(orderRequest);

            startPayment(order);


        } catch (RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void startPayment(Order order) {
        checkout.setKeyID(getResources().getString(R.string.razorpay_key_id));
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.caremee);

        /**
         * Reference to current activity
         */

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Caremee ");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", order_reference_no);
            options.put("image", R.drawable.caremee);
            options.put("order_id", order.get("id"));
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", "500");

            checkout.open(RazorpayActivity.this, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }



//                Checkout checkout=new Checkout();
//
//                checkout.setKeyID("rzp_live_RkfIQ3wjla80mC");
//
//                checkout.setImage(R.drawable.caremee);
//
//                JSONObject object = new JSONObject();
//                try {
//                    object.put("name","CareMee");
//                    object.put("description","Consultation charges");
//                    object.put("theme.color","#0093DD");
//                    object.put("currency","INR");
//                    object.put("Amount","100");
////                    object.put("prefill.contact","9886850203");
////                    object.put("prefill.Title","Caremee");
////                    object.put("prefill.email","caremeeoff@gmail.com");
//                    checkout.open( RazorpayActivity.this,object);
//                } catch (JSONException e) {
//                    Log.e("TAG", "Error in starting Rozorpay Checkout",e);
//
//                }

    @Override
    public void onPaymentSuccess(String s)
    {
        Intent i = new Intent(RazorpayActivity.this, UserRegistration.class);
        i.putExtra("payid", s);
        i.putExtra("phone", phone);
        i.putExtra("uid", uid);
        i.putExtra("paystatus", "Success");
        Log.d(s,"succeded");
        startActivity(i);
        finish();
    }
    @Override
    public void onPaymentError(int i, String s) {
        Intent ib = new Intent(RazorpayActivity.this, OTPActivity.class);
        ReusableFunctionsAndObjects.showMessageAlert(RazorpayActivity.this,"Payment Failed","Sorry Your payment if failed please try again.","Ok",(byte)0);
        startActivity(ib);
        finish();
    }
}
