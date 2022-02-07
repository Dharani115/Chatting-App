package com.android.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdminUserDetail extends AppCompatActivity {
    TextView setting_name,setting_phone,setting_email,setting_address,setting_localaddress,setting_pin,setting_localpin;
    CircleImageView setting_image;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Intent intent;
    String userid;
    String dob;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_detail);
        getSupportActionBar().hide();

        setting_address=findViewById(R.id.address);
        setting_localaddress=findViewById(R.id.localaddress);
        setting_pin=findViewById(R.id.pin);
        setting_localpin=findViewById(R.id.localpin);
        setting_image=findViewById(R.id.setting_image);
        setting_name=findViewById(R.id.name);
        setting_email=findViewById(R.id.email);
//        setting_phone=findViewById(R.id.phone);

        database= FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();

        intent = getIntent();
        userid = getIntent().getStringExtra("userId");
        DatabaseReference reference=database.getReference().child("users").child(userid);
        StorageReference storageReference=storage.getReference().child("upload").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String email= snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String address=snapshot.child("address").getValue().toString();
                String localaddress=snapshot.child("localaddress").getValue().toString();
                String pin=snapshot.child("pin").getValue().toString();
                String localpin=snapshot.child("localpin").getValue().toString();
//                String phone=snapshot.child("phone").getValue().toString();
                String image=snapshot.child("imageUri").getValue().toString();


                setting_address.setText(address);
                setting_localaddress.setText(localaddress);
                setting_pin.setText(pin);
                setting_localpin.setText(localpin);
                setting_name.setText(name);
//                setting_phone.setText(phone);
                setting_email.setText(email);

                Picasso.get().load(image).into(setting_image);
                setting_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminUserDetail.this, Imageview.class);
                        intent.putExtra("image", image);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void profile(View view) {
    }
}
