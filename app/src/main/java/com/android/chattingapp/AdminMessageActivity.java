package com.android.chattingapp;
//list showing to admin

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chattingapp.Adapters.MessageAdapter;
import com.android.chattingapp.Models.Chat;
import com.android.chattingapp.Models.Users;
import com.android.chattingapp.Notification.Client;
import com.android.chattingapp.Notification.Data;
import com.android.chattingapp.Notification.MyResponse;
import com.android.chattingapp.Notification.Sender;
import com.android.chattingapp.Notification.Token;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageView backarrow;

    CardView btn_send;
    EditText text_send;
    TextView status;
    String stat;
    CardView sendFileButton;
    private String checker="",myUrl="";
    private StorageTask uploadTask;
    private Uri fileuri;
    private ProgressDialog loadingbar;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;

    ValueEventListener seenListener;

    String userid;

    APIService apiService;
    LinearLayout showdetail;

    boolean notify = false;
    private static String SERVER_KEY ="AAAA0I7cqfA:APA91bGWx9TyXP3nLa-EsZEcVRjEXQlRzQbONpHSeafNEnlvmW915AEXb3XUAJ-MZvt9sYM8Dzi8C_pgMYpvbXdt58T0aKPPotmFaQqXJNo0GtXK18PKQ-PvbPlMZ2xDCua-r2MHqC_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detailed);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.chatrecyclerView);
        recyclerView.setHasFixedSize(true);
        loadingbar=new ProgressDialog(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
//        showdetail = findViewById(R.id.showdeatils);
        username = findViewById(R.id.userName);
        btn_send = findViewById(R.id.sendBtn);
        text_send = findViewById(R.id.etMessage);
        status = findViewById(R.id.status);
        sendFileButton = findViewById(R.id.fileattachment);
        backarrow=findViewById(R.id.backArrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMessageActivity.this,Admin_ChatBox.class));
            finish();
            }
        });


        getSupportActionBar().hide();

        intent = getIntent();
        userid = getIntent().getStringExtra("userId");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                Long timestamp= Long.valueOf(new Date().getTime());
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg,timestamp);
                } else {
                    Toast.makeText(AdminMessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                if (text_send.length() > 0) {
                    TextKeyListener.clear(text_send.getText());
                }
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AdminMessageActivity.this,UserDetails.class);
                intent.putExtra("userId", userid);
                Pair[] pairs= new Pair[1];
                pairs[0]=new Pair<View,String>(profile_image,"imagetransition");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(AdminMessageActivity.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });
        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[];
                options = new CharSequence[]{
                        "Images",
                };
                AlertDialog.Builder builder=new AlertDialog.Builder(AdminMessageActivity.this);
                builder.setTitle("Select a file");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){
                            checker="image";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"Select Image"),10);
                        }
                    }
                });
                builder.show();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getName());
                status.setText(user.getStatus());
                if (user.getImageUri().equals("default")){
                    profile_image.setImageResource(R.drawable.profile);
                } else {
                    //and this
                    Glide.with(getApplicationContext()).load(user.getImageUri()).into(profile_image);
                }

                readMesagges(fuser.getUid(), userid, user.getImageUri());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10 && data!=null){
            loadingbar.setTitle("Sending File");
            loadingbar.setMessage("Please Wait Your Image is uploading");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();


            fileuri = data.getData();
            if (checker.equals("image")){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image_File");

                reference = FirebaseDatabase.getInstance().getReference();
                Long timestamp= Long.valueOf(new Date().getTime());
                intent = getIntent();
                userid = getIntent().getStringExtra("userId");
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                String imgdt=userid+fuser.getUid();
                StorageReference filepath=storageReference.child(timestamp+"."+"jpg");
                uploadTask=filepath.putFile(fileuri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            loadingbar.dismiss();
                            Uri downloadUri=task.getResult();
                            myUrl=downloadUri.toString();
                            String type="image";

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("sender", fuser.getUid());
                            hashMap.put("receiver", userid);
                            hashMap.put("message", myUrl);
                            hashMap.put("timestamp", timestamp);
                            hashMap.put("isseen", false);
                            hashMap.put("type", type);

                            FirebaseDatabase.getInstance().getReference().child("chats").push().setValue(hashMap);
                        }

                    }
                });

                // add user to chat fragment
                final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("users")
                        .child(userid);
                chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String timestampp=timestamp.toString();

                            chatRef.child("timestamp").setValue(timestampp);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                        .child(fuser.getUid());
                chatRefReceiver.child("id").setValue(fuser.getUid(),timestamp);

                final String image = myUrl;

                reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        if (notify) {
                            sendNotifiaction(userid, user.getName(), image);
                        }
                        notify = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if (!checker.equals("image")){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image_File");

                reference = FirebaseDatabase.getInstance().getReference();
                Long timestamp= Long.valueOf(new Date().getTime());
                intent = getIntent();
                userid = getIntent().getStringExtra("userId");
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                String imgdt=userid+fuser.getUid();
                StorageReference filepath=storageReference.child(timestamp+"."+"jpg");
                uploadTask=filepath.putFile(fileuri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            loadingbar.dismiss();
                            Uri downloadUri=task.getResult();
                            myUrl=downloadUri.toString();
                            String type="image";
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("sender", fuser.getUid());
                            hashMap.put("receiver", userid);
                            hashMap.put("message", myUrl);
                            hashMap.put("timestamp", timestamp);
                            hashMap.put("isseen", false);
                            hashMap.put("type", type);

                            FirebaseDatabase.getInstance().getReference().child("chats").push().setValue(hashMap);
                        }

                    }
                });

                // add user to chat fragment
                final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("users")
                        .child(userid);
                chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String timestampp=timestamp.toString();
                            chatRef.child("timestamp").setValue(timestampp);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                        .child(fuser.getUid());
                chatRefReceiver.child("id").setValue(fuser.getUid(),timestamp);

                final String image = myUrl;

                reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        if (notify) {
                            sendNotifiaction(userid, user.getName(), image);
                        }
                        notify = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            else {
                Toast.makeText(AdminMessageActivity.this,"Nothing Selected,Error.",Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void seenMessage(final String userid){
        FirebaseAuth id= FirebaseAuth.getInstance();
        String myid=id.getUid();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    String rec=chat.getReceiver();
                    String sed=chat.getSender();

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message, Long timestamp){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String type="text";

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isseen", false);
        hashMap.put("type", type);

        reference.child("chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String timestampp=timestamp.toString();

                    chatRef.child("timestamp").setValue(timestampp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid(),timestamp);

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    private void sendNotifiaction(final String receiver, final String username, final String message){
//
//        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
//        Query query = tokens.orderByKey().equalTo(receiver);
//        tokens.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Token token = snapshot.getValue(Token.class);
//                    Data data = new Data(fuser.getUid(), R.drawable.caremee, username+": "+message, "New Message",
//                            userid);
//
//                    Sender sender = new Sender(data, token.getToken());
//
//                    apiService.sendNotification(sender)
//                            .enqueue(new Callback<MyResponse>() {
//                                @Override
//                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                    if (response.code() == 200){
//                                        if (response.body().success != 1){
//                                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MyResponse> call, Throwable t) {
//
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void sendNotifiaction(final String receiver, final String username, final String message){
//        FCMSend.SetServerKey(SERVER_KEY);
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(receiver).child("token")
//        Query query = tokens.child("Tokens").child(receiver).equalTo(receiver);
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usertoken=dataSnapshot.getValue(String.class);

                sendNotifications(usertoken, username,message,receiver);



//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                        String usertoken=dataSnapshot.getValue(String.class);
//
//                        Token token = snapshot.getValue(Token.class);
//                        if (token.getToken()==null){
//                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_LONG).show();
//                            Toast.makeText(MessageActivity.this, usertoken, Toast.LENGTH_LONG).show();
//
//                        }
//                        Data data = new Data(fuser.getUid(), R.drawable.caremee, username + ": " + message, "New Message", userid);
//                        Sender sender = new Sender(data, usertoken);
//
//                        apiService.sendNotification(sender)
//                                .enqueue(new Callback<MyResponse>() {
//                                    @Override
//                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                        if (response.code() == 200) {
//                                            if (response.body().success != 1) {
//                                                Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<MyResponse> call, Throwable t) {
//                                        Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        UpdateToken();
    }

    private void sendNotifications(String usertoken, String username, String message, String userid) {
        Data data = new Data( username + ": " + message, "New Message");
        Sender sender = new Sender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(AdminMessageActivity.this, "Failed ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String id = firebaseUser.getUid();
        userid = getIntent().getStringExtra("userId");

        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(userid).setValue(token);
    }

    private void readMesagges(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(AdminMessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }

}
