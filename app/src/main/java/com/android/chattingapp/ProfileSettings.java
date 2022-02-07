package com.android.chattingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettings extends AppCompatActivity {
    CircleImageView setting_image;
    EditText setting_name,setting_phone,setting_email,setting_age,setting_address,mdob;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ImageView save;
    Uri setImageURI;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    RadioGroup mgender;
    private RadioButton radioSexButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        setting_address=findViewById(R.id.address);
        setting_image=findViewById(R.id.setting_image);
        setting_name=findViewById(R.id.name);
        setting_email=findViewById(R.id.email);
        setting_phone=findViewById(R.id.phone);
        setting_age=findViewById(R.id.age);
        save=findViewById(R.id.save);
        mdob= findViewById(R.id.dob);
//        mgender = (RadioGroup) findViewById(R.id.Gender);



        final Calendar calendar= Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mdob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProfileSettings.this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener, year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayofMonth) {
                month = month+1;
                String date=dayofMonth+"/"+month+"/"+year;
                mdob.setText(date);
            }
        };
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        DatabaseReference reference=database.getReference().child("users").child(uid);
        StorageReference storageReference=storage.getReference().child("upload").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int selectedId = mgender.getCheckedRadioButtonId();
//
//                radioSexButton = (RadioButton) findViewById(selectedId);

                String email= snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String address=snapshot.child("address").getValue().toString();
                String phone=snapshot.child("phone").getValue().toString();
                String age=snapshot.child("age").getValue().toString();
                String image=snapshot.child("imageUri").getValue().toString();
//                String gender=snapshot.child("gender").getValue().toString();
                String dob=snapshot.child("dob").getValue().toString();


                setting_address.setText(address);
                setting_name.setText(name);
                setting_phone.setText(phone);
                setting_email.setText(email);
                setting_age.setText(age);
                mdob.setText(dob);
//                mgender.setText(gender);
                Picasso.get().load(image).into(setting_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),10);
            }
        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setting_address.getText().toString();
//                setting_age.getText().toString();
//                setting_email.getText().toString();
//                setting_name.getText().toString();
//                setting_phone.getText().toString();
//
//                if (setImageURI != null)
//                {
//                    storageReference.putFile(setImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//
//                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            String finalImageUri=uri.toString();
////                                            users users = new users(auth.getUid(), setting_address, setting_age,setting_email,finalImageUri, setting_name, setting_phone);
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    });
//                }
//
//            }
//        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==10){
            if(data!=null)
            {
                setImageURI=data.getData();
                setting_image.setImageURI(setImageURI);
            }
        }
    }

}
