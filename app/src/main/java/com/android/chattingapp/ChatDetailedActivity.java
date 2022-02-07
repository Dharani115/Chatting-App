package com.android.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

//import com.android.chattingapp.Adapters.ChatAdapter;
import com.android.chattingapp.databinding.ActivityChatDetailedBinding;

//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatDetailedActivity extends AppCompatActivity {

    ActivityChatDetailedBinding binding;
    FirebaseDatabase database;
    Intent intent;
    ValueEventListener seenListener;
//    APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailedBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        getSupportActionBar().hide();
//
//        database = FirebaseDatabase.getInstance();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//
//
//        final String senderId = auth.getUid();
//        String recieverId = getIntent().getStringExtra("userId");
//        String UserName = getIntent().getStringExtra("UserName");
//        String Profilepic = getIntent().getStringExtra("profilepic");
//
//        seenMessage(recieverId,senderId);
//
//        binding.userName.setText(UserName);
//        Picasso.get().load(Profilepic).placeholder(R.drawable.avatar).into(binding.profileImage);
//
//        binding.backArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ChatDetailedActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//        final ArrayList<Chat> chats = new ArrayList<>();
//        final ChatAdapter chatAdapter=new ChatAdapter(this, chats,recieverId);
//        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
//        layoutManager.setStackFromEnd(true);
//
//        binding.chatecyclerView.setLayoutManager(layoutManager);
//
//        binding.chatecyclerView.setAdapter(chatAdapter);
//
//        layoutManager.setStackFromEnd(true);
////        binding.chatecyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
//
//
//        final String senderRoom=senderId+recieverId;
//        final String receiverRoom=recieverId+senderId;
//
//        database.getReference().child("chats")
//                .child(senderRoom)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        chats.clear();
//                       for(DataSnapshot snapshot1 : snapshot.getChildren())
//                       {
//                           Chat model = snapshot1.getValue(Chat.class);
//                           model.setMessageId(snapshot1.getKey());
//
//                           chats.add(model);
//
//                           binding.chatecyclerView.getLayoutManager().scrollToPosition(chats.size()-1);
//
//                       }
//                       chatAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//
//                });
//
//
//        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            String message = binding.etMessage.getText().toString();
//            final Chat model=new Chat(senderId,message);
//            model.setTimestamp(new Date().getTime());
//            binding.etMessage.setText("");
//
//            database.getReference().child("chats")
//                    .child(senderRoom)
//                    .push()
//                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                     database.getReference().child("chats")
//                             .child(receiverRoom)
//                             .push()
//                             .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                         @Override
//                         public void onSuccess(Void unused) {
//
//                         }
//                     });
//                }
//            });
//            }
//        });
//
//
//    }
//    private void seenMessage(String recieverId, String senderId){
//            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chats");
//            seenListener=reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot snapshot1: snapshot.getChildren()){
//                        Chat chat=snapshot1.getValue(Chat.class);
//                    if (recieverId.equals(FirebaseAuth.getInstance().getUid()) && chat.getuId().equals(senderId)){
//                        HashMap<String,Object> hashMap =new HashMap<>();
//                        hashMap.put("isseen", true);
//                        snapshot1.getRef().updateChildren(hashMap);
//
//                    }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
////        private void sendMessage

    }
}