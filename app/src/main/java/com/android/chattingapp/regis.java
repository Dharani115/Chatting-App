package com.android.chattingapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.chattingapp.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class regis extends AppCompatActivity {

    private EditText EmailTB;
    private EditText PassTB;
    private TextView forgetPass;
    private Button logInButton;
    String email, password;
    public static final String userEmail="";
    FirebaseAuth mAuth;
    DatabaseReference reference;
    public static final String TAG="LOGIN";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EmailTB = findViewById(R.id.Email);
        PassTB = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        logInButton = findViewById(R.id.log_in);
        forgetPass=findViewById(R.id.forgetpass);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(regis.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin=10;
                lp.rightMargin=10;
                input.setLayoutParams(lp);
                input.setHint("Email");

                new AlertDialog.Builder(regis.this).setTitle("Enter email ID").setView(input).setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        boolean isemailvalid=false;
                        if ((((input.getText()).toString()).trim()).equals("")) {
                            ReusableFunctionsAndObjects.showMessageAlert(regis.this,"Invalid Email","Please enter valid email.","Ok",(byte)0);
                            isemailvalid = false;
                        } else {
                            if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(((input.getText()).toString()).trim()).matches()) {
                                isemailvalid = true;
                            } else {
                                ReusableFunctionsAndObjects.showMessageAlert(regis.this,"Invalid Email","Please enter valid email.","Ok",(byte)0);
                                isemailvalid = false;
                            }
                        }
                        if(isemailvalid){
                            progressDialog.setMessage("Please wait....");
                            progressDialog.show();
                            FirebaseAuth.getInstance().sendPasswordResetEmail(input.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        ReusableFunctionsAndObjects.showMessageAlert(regis.this,"Reset Link Sent","Password reset link has been sent to "+input.getText().toString().trim(),"Ok",(byte)1);
                                    }else{
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                            ReusableFunctionsAndObjects.showMessageAlert(regis.this, "Invalid User", "Account does'nt exists. Make sure you have activated your account", "OK",(byte)0);
                                        } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                            ReusableFunctionsAndObjects.showMessageAlert(regis.this, "Invalid Password", "Your password is incorrect", "OK",(byte)0);
                                        } catch (Exception e) {
                                            ReusableFunctionsAndObjects.showMessageAlert(regis.this, "Network Error", "Unable to login, Make sure you are connected to internet", "OK",(byte)0);
                                        }
                                    }
                                }
                            });
                        }

                    }
                }).setNegativeButton("Cancel",null).show();
            }
        });
//        gotToRegister=findViewById(R.id.gotoregister);
//        if (getIntent().getBooleanExtra("ForPatient", false)) {
//            gotToRegister.setVisibility(View.VISIBLE);
//        } else {
//            gotToRegister.setVisibility(View.GONE);
//        }
//        gotToRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Admin_Login.this,PatientRegister.class));
//                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//                finish();
//            }
//        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSign();
            }
        });
    }
    private void userSign() {
        email = EmailTB.getText().toString().trim();
        password = PassTB.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(regis.this, "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(regis.this, "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loging in please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    try {
                        if (user != null)
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        regis.this);

                                                // set title
                                                alertDialogBuilder.setTitle("Please Verify Your EmailID");

                                                // set dialog message
                                                alertDialogBuilder
                                                        .setMessage("A verification Email Is Sent To Your Registered EmailID, please click on the link and Sign in again!")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                Intent signInIntent = new Intent(regis.this, Welcome.class);
                                                                regis.this.finish();
                                                            }
                                                        });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();


                                            }
                                        }
                                    });

                    } catch (Exception e) {
                        Toast.makeText(regis.this,"error",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(regis.this,"You are Not a Doctor",Toast.LENGTH_SHORT).show();
                }
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    email=EmailTB.getText().toString().trim();
                    password=PassTB.getText().toString().trim();

                    if (users.getEmail().equals(email) && users.getPassword().equals(password)){
                        progressDialog.dismiss();
                        EmailTB.getText().clear();

                        PassTB.getText().clear();
                        Intent intent = new Intent(regis.this, Admin_ChatBox.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(regis.this,"Please enter Valid Details",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (!task.isSuccessful()) {
//                    progressDialog.dismiss();
//
//                    Toast.makeText(Admin_Login.this, "Login not successfull", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    progressDialog.dismiss();
//
//                    checkIfEmailVerified();
//
//                }
//            }
//        });

    }
    private void checkIfEmailVerified(){
        FirebaseUser users= FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified=users.isEmailVerified();
        if(!emailVerified){
            Toast.makeText(this,"Verify the Email Id",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        }
        else {
            EmailTB.getText().clear();

            PassTB.getText().clear();
            Intent intent = new Intent(regis.this, Admin_ChatBox.class);

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(userEmail,email);

            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        {
            startActivity(new Intent(regis.this,AskDoctorPatient.class));
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            finish();
        }
    }
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(regis.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }
//    private void OnAuth(FirebaseUser user) {
//        createAnewUser(user.getUid());
//    }
//    private void createAnewUser(String uid) {
//        User user = BuildNewuser();
//        mdatabase.child(uid).setValue(user);
//    }
//
//
//    private User BuildNewuser(){
//        return new User(
//                getDisplayName(),
//                getUserEmail(),
//                new Date().getTime()
//        );
//    }
//
//    public String getDisplayName() {
//        return Name;
//    }
//
//    public String getUserEmail() {
//        return Email;
//    }
}
