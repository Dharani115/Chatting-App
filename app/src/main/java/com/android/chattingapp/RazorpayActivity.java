package com.android.chattingapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView textView;

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

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textView = findViewById(R.id.textView);


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

            checkout.setKeyID(getResources().getString(R.string.razorpay_key_id));
            Checkout checkout = new Checkout();


            checkout.setImage(R.drawable.caremee);

            try {
                JSONObject options = new JSONObject();

                options.put("name", "Caremee");

                options.put("description", "Consultation Charge");
                options.put("image", R.drawable.caremee);
                options.put("order_id", order.get("id"));
                options.put("currency", "INR");

                options.put("amount", 100);

                checkout.open(RazorpayActivity.this, options);

            } catch(Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }


        } catch (RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public void startPayment(Order order) {
//        checkout.setKeyID(getResources().getString(R.string.razorpay_key_id));
//        Checkout checkout = new Checkout();
//
//
//        checkout.setImage(R.drawable.caremee);
//
//        try {
//            JSONObject options = new JSONObject();
//
//            options.put("name", "Caremee");
//
//            options.put("description", order_reference_no);
//            options.put("image", R.drawable.caremee);
////            options.put("order_id", order.get("id"));
//            options.put("currency", "INR");
//
//            options.put("amount", 100);
//
//            checkout.open(RazorpayActivity.this, options);
//            Log.e(TAG, "payment passed", order.get("id"));
//
//        } catch(Exception e) {
//            Log.e(TAG, "Error in starting Razorpay Checkout", e);
//        }
//    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        textView.setText("Payment ID: " + s);
        textView.append("\nOrder ID: " + order.get("id"));
        textView.append("\n" + order_reference_no);
        Log.e("TAG", "payment done", order.get("id"));
        Toast.makeText(RazorpayActivity.this,"Payment successful",Toast.LENGTH_LONG).show();


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Error: " + s, Toast.LENGTH_LONG).show();
        textView.setText("Error: " + s);
        Log.e("Tag", "Error in starting Razorpay Checkout");

    }
}