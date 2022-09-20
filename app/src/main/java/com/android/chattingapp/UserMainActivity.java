package com.android.chattingapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.chattingapp.Adapters.UserFragmentAdapter;
import com.android.chattingapp.databinding.ActivityUserMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserMainActivity extends AppCompatActivity {
    ActivityUserMainBinding binding;
    FirebaseAuth auth;
    private boolean doubleBackToExitPressedOnce;
    FirebaseUser fuser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.viewpager.setAdapter(new UserFragmentAdapter(getSupportFragmentManager()));
        FirebaseDatabase database=FirebaseDatabase.getInstance();

        auth=FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        binding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserMainActivity.this, SettingsActivity.class);
                i.putExtra("uid",auth.getUid());
                startActivity(i);

            }
        });


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(UserMainActivity.this,R.style.Dailoge);

                dialog.setContentView(R.layout.logoutdailog_layout);
                TextView yesbtn,nobtn;
                yesbtn=dialog.findViewById(R.id.yesbtn);
                nobtn=dialog.findViewById(R.id.nobtn);

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();//logout
                        startActivity(new Intent(getApplicationContext(),Welcome.class));
                        finish();
                    }
                });
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }



    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
        Toast.makeText(this,"Click back again to exit",Toast.LENGTH_SHORT).show();
        doubleBackToExitPressedOnce=true;
        finish();
    }
    private void status(String status){
        Intent intent=getIntent();
        String uid = intent.getStringExtra("uid");
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}