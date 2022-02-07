package com.android.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        getSupportActionBar().hide();







        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                userRef.orderByChild("uid").equalTo(fAuth.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    String email = null;
                                    if (user != null) {
                                        email = user.getUid();

                                        String id = "4oCgArfvMSdxzxqGmjLZqXhR9Dh2";

                                        if (fAuth.getCurrentUser() != null) {
                                            if (email.equals(id)) {
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                                                finish();
                                            } else {
                                                startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
                                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                                finish();
                                            }
                                        } else {
                                            startActivity(new Intent(getApplicationContext(), OTPActivity.class));
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                            finish();
                                        }
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), OTPActivity.class));
                                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                        finish();
                                    }

                                }
                            }


                            private void OpenErrorAlrt(String mobile_number_already_registed) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Intent intent =new Intent(SplashScreen.this, Welcome.class);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                startActivity(intent);
                                finish();
                            }
                        });
//                Intent intent =new Intent(SplashScreen.this, Welcome.class);
//                startActivity(intent);
//                finish();
            }
        },3000);
    }
}
