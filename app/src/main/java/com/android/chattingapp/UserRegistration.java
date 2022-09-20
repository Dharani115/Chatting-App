package com.android.chattingapp;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

public class UserRegistration extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mage,mAddress,mPin,lAddress,lPin;
    TextView mPhone;
    RadioGroup mgender;
    private RadioButton radioSexButton;
    ImageView mprofilel;
    EditText mdob;
    Button mRegisterBtn;
    CheckBox msamebtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    Uri imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    String imageUri;
    DatePickerDialog.OnDateSetListener onDateSetListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        mFullName   = findViewById(R.id.username);
        mEmail      = findViewById(R.id.email);
        mPhone      = findViewById(R.id.Phone);
        mdob      = findViewById(R.id.dob);
        mage      = findViewById(R.id.Age);
        mAddress      = findViewById(R.id.Address);
        lAddress      = findViewById(R.id.LocalAddress);
        mPin      = findViewById(R.id.pin);
        lPin      = findViewById(R.id.LocalPin);
        mRegisterBtn = findViewById(R.id.btnregister);
        msamebtn = findViewById(R.id.samebtn);
//        mLoginBtn = findViewById(R.id.loginBtn);
        mprofilel =findViewById(R.id.profileimage);

        msamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addr=mAddress.getText().toString();
                String pincode=mPin.getText().toString();
                lAddress.setText(addr);
                lPin.setText(pincode);
            }
        });


        String[] bloodgroup = { "O +ve", "O -ve", "A +ve","A -ve","B +ve","B -ve","AB +ve","AB -ve" };
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloodgroup);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);




        getSupportActionBar().hide();



        fAuth = FirebaseAuth.getInstance();
//        String uid= Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
//        Log.i("uid:",uid);


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressBar = findViewById(R.id.progressBar);

        mgender = (RadioGroup) findViewById(R.id.Gender);



        final Calendar calendar= Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mdob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UserRegistration.this, android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener, year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle("Please Enter Valid Details, It Can't be changed later");
                datePickerDialog.show();

            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayofMonth) {
            month = month+1;
            String date=dayofMonth+"/"+month+"/"+year;

                mdob.setText(date);

                final Calendar calendar= Calendar.getInstance();
                int ageyear = calendar.get(Calendar.YEAR);

                int age = ageyear - year;
                if (age<=0){
                    Toast.makeText(UserRegistration.this, "Please enter Valid Date of Birth " , Toast.LENGTH_SHORT).show();
                }
                else {
                String myage= String.valueOf(age);
                    mage.setText(myage);
                }

            }
        };
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("phone");
        String uid = intent.getStringExtra("uid");
        Log.d(TAG, "phone :"+ mobile);

        mPhone.setText(mobile);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                int selectedId = mgender.getCheckedRadioButtonId();
//                progressBar.setVisibility(View.VISIBLE);

                radioSexButton = (RadioButton) findViewById(selectedId);

                final String address = mAddress.getText().toString();
                final String localaddress = lAddress.getText().toString();
                final String pin = mPin.getText().toString();
                final String localpin = lPin.getText().toString();
                final String age = mage.getText().toString();
                final String dob = mdob.getText().toString();
                final String email = mEmail.getText().toString().trim();
                final String name = mFullName.getText().toString();
//                final String blood = spin.getText().toString();
                final String gender = radioSexButton.getText().toString();
//                final String password = mPassword.getText().toString().trim();
                final String phone = mPhone.getText().toString();
                final String bloodgroup = spin.getSelectedItem().toString();
                final String status="hay there i am using CareMee";
                final String lastmessage="";
                if (TextUtils.isEmpty(email)){
                    if( Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        mEmail.setError("Please Enter Valid Email.");
                    }
                else{
                        mEmail.setError("Email is Required.");
                    }

                }
                else if (TextUtils.isEmpty(name)) {
                    mFullName.setError("Name is Required.");
                    progressDialog.dismiss();

                }
                else if (TextUtils.isEmpty(phone)) {
                    mPhone.setError("Phone Number is Required.");

                }
                else if (TextUtils.isEmpty(dob)) {
                    mdob.setError("Date of birth is Required.");

                }
                else if (TextUtils.isEmpty(gender)) {
                    radioSexButton.setError("Gender is Required.");
                    progressDialog.dismiss();

                }
                else if (TextUtils.isEmpty(age)) {
                    mage.setError("Age is Required.");
                    progressDialog.dismiss();

                }
                else if (TextUtils.isEmpty(address)) {
                    mAddress.setError("Address is Required.");
                    progressDialog.dismiss();

                } else if (TextUtils.isEmpty(localaddress)) {
                    lAddress.setError("Local Address is Required.");
                    progressDialog.dismiss();

                } else if (TextUtils.isEmpty(pin)) {
                    mPin.setError("Pin is Required.");
                    progressDialog.dismiss();

                } else if (TextUtils.isEmpty(localpin)) {
                    lPin.setError("Local Pin is Required.");
                    progressDialog.dismiss();

                }
                else{
                    addDatatoFirebase(address,age,bloodgroup,dob,email,gender,localaddress,localpin,name,phone,pin,status);
                }
            }
        });


}

    private void addDatatoFirebase( String address, String age,String bloodgroup, String dob, String email, String gender, String localaddress,String localpin, String name, String phone, String pin, String status) {
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String mobile = intent.getStringExtra("phone");
        String payid = intent.getStringExtra("payid");
        String paystatus = intent.getStringExtra("paystatus");

        DatabaseReference reference = database.getReference().child("users").child(uid);
        StorageReference storageReference = storage.getReference().child("upload").child(uid);
        if (imageuri != null)
        {
            storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        progressBar.setVisibility(View.GONE);

                        progressDialog.dismiss();

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                progressDialog.dismiss();
                                imageUri = uri.toString();
                                Intent intent = getIntent();
                                String uid = intent.getStringExtra("uid");
                                String payid = intent.getStringExtra("payid");
                                String paystatus = intent.getStringExtra("paystatus");
                                String lastmessage = null;
                                Long time = new Date().getTime();
                                String timestamp=time.toString();
                                String password= String.valueOf(Math.random());
                                String type="Patients";

                                Users users = new Users(uid,address,age,bloodgroup,dob,email,gender,imageUri,localaddress,localpin,name,phone,pin,status,timestamp,password,type,payid,paystatus);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Intent i = new Intent(UserRegistration.this, User_ChatBox.class);
                                            i.putExtra("phone", mobile);
                                            i.putExtra("uid",uid);
                                            Log.d(TAG, "phone" + mobile);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(UserRegistration.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
        else {
            imageUri = "https://firebasestorage.googleapis.com/v0/b/chat-51895.appspot.com/o/profile.jpg?alt=media&token=1942bc96-43cf-4d8a-910b-8cc089357e15";
            String lastmessage = null;
            Long time = new Date().getTime();
            String timestamp=time.toString();
            String password= String.valueOf(Math.random());
            String type="Patients";

            Users users = new Users(uid,address,age,bloodgroup,dob,email,gender,imageUri,localaddress,localpin,name,phone,pin,status,timestamp,password,type,payid,paystatus);
            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(UserRegistration.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10){
            if(data!=null){
                imageuri=data.getData();
                mprofilel.setImageURI(imageuri);
            }
        }
    }

    public void profile(View view) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),10);
    }

}
