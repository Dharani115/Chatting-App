package com.android.chattingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;
    TextView register;
    ProgressBar progressBar;
    ProgressDialog progressDialog;


    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;


    //payment
    private static final String TAG = "Razorpay";
    Checkout checkout;
    RazorpayClient razorpayClient;
    Order order;
    String email, password;
    TextView paystatus;
    String paymentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getSupportActionBar().hide();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
        progressBar = findViewById(R.id.progressBar);
//        paystatus = findViewById(R.id.paystatus);

//        Button pay=findViewById(R.id.payment);
//        verifyOTPBtn.setVisibility(View.GONE);
//        Checkout.preload(getApplicationContext());

//        Intent intent = getIntent();
//        String payid = intent.getStringExtra("payid");
//        String paystat = intent.getStringExtra("paystatus");
//        paystatus.setText(paystat);
////        paystatus.append(paystat);
//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(getApplicationContext(), RazorpayActivity.class));
//                finish();
//                Checkout checkout=new Checkout();
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
//                    object.put("Amount",100);
//                    checkout.open( OTPActivity.this,object);
//                } catch (JSONException e) {
//                    Log.e("TAG", "Error in starting Rozorpay Checkout",e);
//
//                }
//            }
//        });


        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    progressDialog.show();
//                verifyOTPBtn.setVisibility(View.GONE);

                    // below line is for checking weather the user
                    // has entered his mobile number or not.
                    if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                        progressDialog.dismiss();

                        // when mobile number text field is empty
                        // displaying a toast message.
                        Toast.makeText(OTPActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();

                        // if the text field is not empty we are calling our
                        // send OTP method for getting OTP from Firebase.
                        generateOTPBtn.setEnabled(false);
                        String phone = "+91" + edtPhone.getText().toString();
                        sendVerificationCode(phone);

                    }
                }

        });

        // initializing on click listener
        // for verify otp button
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    progressDialog.dismiss();
                    Toast.makeText(OTPActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    progressDialog.dismiss();

                    verifyCode(edtOTP.getText().toString());
                    verifyOTPBtn.setEnabled(false);

                }
            }

        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String id="7yBNOdB4qcS3eNjoWaQUSy7XOaQ2";
                        if (task.isSuccessful()) {
                            progressDialog.show();

                            if (mAuth.getUid().equals(id)){

                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                userRef.orderByChild("phone").equalTo(edtPhone.getText().toString())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                startActivity(new Intent(getApplicationContext(), Admin_ChatBox.class));
                                                finish();
                                            }
                                        }
                                        private void OpenErrorAlrt (String mobile_number_already_registed)
                                        {
                                        }

                                        @Override
                                        public void onCancelled (DatabaseError databaseError)
                                        {
                                        }

                                });
                            }
                            else if(task.isSuccessful()) {
                                String stat="success";
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                userRef.orderByChild("phone").equalTo(edtPhone.getText().toString())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() != null) {
                                                    startActivity(new Intent(getApplicationContext(), User_ChatBox.class));
                                                    finish();
                                                } else {
                                                    String mobile = edtPhone.getText().toString();
                                                    Intent i = new Intent(OTPActivity.this, RazorpayActivity.class);
                                                    i.putExtra("phone", mobile);
                                                    i.putExtra("uid", mAuth.getUid());
//                                                    Log.d(TAG, "phone" + mobile);
                                                    startActivity(i);
                                                    finish();

                                                }

                                            }

                                            private void OpenErrorAlrt(String mobile_number_already_registed) {
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }

                        }
                        else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            progressDialog.dismiss();

                            Toast.makeText(OTPActivity.this, "Invalid Code.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)		 // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)				 // Activity (for callback binding)
                        .setCallbacks(mCallBack)		 // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        progressDialog.show();

    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks


            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            progressDialog.dismiss();

            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            progressDialog.dismiss();

            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                progressDialog.dismiss();

                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressDialog.dismiss();
//            verifyOTPBtn.setVisibility(View.GONE);

            // displaying error message with firebase exception.
            Toast.makeText(OTPActivity.this, "Please Check Your internet connection and Try Again", Toast.LENGTH_LONG).show();
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        progressDialog.dismiss();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

}


