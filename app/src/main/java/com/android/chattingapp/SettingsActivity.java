package com.android.chattingapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.chattingapp.Models.Users;
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

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    CircleImageView setting_image;
    EditText setting_name,setting_phone,setting_email,setting_address,setting_localaddress,setting_pin,setting_localpin;
    TextView mdob;
    TextView setting_age;
    TextView mbloodgroup;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Button save;
    Uri imageUri;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TextView mgender;
    private RadioButton radioSexButton;
    String dob;
    String gender;
    String status;
    String lastmessage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        setting_address=findViewById(R.id.address);
        setting_localaddress=findViewById(R.id.localaddress);
        setting_pin=findViewById(R.id.pin);
        setting_localpin=findViewById(R.id.localpin);
        setting_image=findViewById(R.id.setting_image);
        setting_name=findViewById(R.id.name);
        setting_email=findViewById(R.id.email);
        setting_phone=findViewById(R.id.phone);
        setting_age=findViewById(R.id.age);
        save=findViewById(R.id.save);
        mdob= findViewById(R.id.dob);
        mbloodgroup= findViewById(R.id.bloodgroup);
        mgender= findViewById(R.id.Gender);
        mdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this,"You can't edit!",Toast.LENGTH_SHORT).show();
            }
        });
        mgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this,"You can't edit!",Toast.LENGTH_SHORT).show();
            }
        });
        setting_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this,"You can't edit!",Toast.LENGTH_SHORT).show();
            }
        });
        mbloodgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this,"You can't Edit!",Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().hide();
        DatabaseReference reference=database.getReference().child("users").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String email= snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String address=snapshot.child("address").getValue().toString();
                String localaddress=snapshot.child("localaddress").getValue().toString();
                String pin=snapshot.child("pin").getValue().toString();
                String localpin=snapshot.child("localpin").getValue().toString();
                String phone=snapshot.child("phone").getValue().toString();
                String age=snapshot.child("age").getValue().toString();
                String bloodgroup=snapshot.child("bloodgroup").getValue().toString();
                status=snapshot.child("status").getValue().toString();
                String image=snapshot.child("imageUri").getValue().toString();
                gender=snapshot.child("gender").getValue().toString();
                dob=snapshot.child("dob").getValue().toString();


                setting_address.setText(address);
                setting_localaddress.setText(localaddress);
                setting_pin.setText(pin);
                setting_localpin.setText(localpin);
                setting_name.setText(name);
                setting_phone.setText(phone);
                setting_email.setText(email);
                setting_age.setText(age);
                mdob.setText(dob);
                mbloodgroup.setText(bloodgroup);
                mgender.setText(gender);
                Picasso.get().load(image).into(setting_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                final String address= setting_address.getText().toString();
                final String localaddress= setting_localaddress.getText().toString();
                final String pin= setting_pin.getText().toString();
                final String localpin= setting_localpin.getText().toString();
                final String age =setting_age.getText().toString();
                final String email =setting_email.getText().toString();
                final String name =setting_name.getText().toString();
                final String phone= setting_phone.getText().toString();
                final String bloodgroup= mbloodgroup.getText().toString();
                mdob.getText().toString();
                mgender.getText().toString();

                if (imageUri != null)
                {
                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        Intent intent = getIntent();
                        String uid = intent.getStringExtra("uid");
                        Long time = new Date().getTime();
                        String timestamp=time.toString();
                        String password= String.valueOf(Math.random());
                        String type="Patients";
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            progressDialog.dismiss();
                                            String finalImageUri=uri.toString();
                                            Users users = new Users(uid,address,age,bloodgroup,dob,email,gender,finalImageUri,localaddress,localpin,name,phone,pin,status,timestamp,password,type);


                                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(SettingsActivity.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(SettingsActivity.this,"Update failed try again",Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        Intent intent = getIntent();
                        String uid = intent.getStringExtra("uid");
                        Long time = new Date().getTime();
                        String timestamp=time.toString();
                        String password= String.valueOf(Math.random());
                        String type="Patients";
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            String finalImageUri=uri.toString();
                            Users users = new Users(uid,address,age,bloodgroup,dob,email,gender,finalImageUri,localaddress,localpin,name,phone,pin,status,timestamp,password,type);


                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SettingsActivity.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(SettingsActivity.this,"Update failed try again",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    });
                }

            }
        });




    }

    public void profile(View view) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==10){
            if(data!=null)
            {
                imageUri=data.getData();
                setting_image.setImageURI(imageUri);
            }
        }
    }
}