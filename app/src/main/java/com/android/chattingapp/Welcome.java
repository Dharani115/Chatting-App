package com.android.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Welcome extends AppCompatActivity {
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        fAuth = FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        getSupportActionBar().hide();



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
                                        finish();
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
                                        finish();
                                    }
                                } else {
                                    startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
                                    finish();
                                }
                            } else {
                                startActivity(new Intent(getApplicationContext(), OTPActivity.class));
                                finish();
                            }

                        }
                    }

                    private void OpenErrorAlrt(String mobile_number_already_registed) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }
//        else{
//
//            startActivity(new Intent(getApplicationContext(), Welcome.class));
//            finish();
//        }

//    }

//    public void admin(View view) {
//        startActivity(new Intent(getApplicationContext(),Admin_Login.class));
//    }

    public void user(View view) {
        startActivity(new Intent(getApplicationContext(),AskDoctorPatient.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void admin(View view) {
//        startActivity(new Intent(getApplicationContext(),Admin_Login.class));
//        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}