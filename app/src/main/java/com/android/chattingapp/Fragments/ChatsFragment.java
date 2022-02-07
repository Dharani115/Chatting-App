package com.android.chattingapp.Fragments;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.chattingapp.Adapters.UsersAdapter;
import com.android.chattingapp.Models.Chat;
import com.android.chattingapp.Models.Chatlist;
import com.android.chattingapp.Models.Users;
import com.android.chattingapp.Notification.Token;
import com.android.chattingapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList();
    ArrayList<Chatlist> chats = new ArrayList();
    FirebaseDatabase database;
    FirebaseUser fAuth;
    String user;
    Users users;
    Chat chat;
    String lastmsgdate;
    String todaydate;
    UsersAdapter adapter;
    String listid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new UsersAdapter(list, getContext());
        binding.chatRecyclerView.setAdapter(adapter);


//        Collections.sort(list, new Comparator<Users>() {
//            @Override
//            public int compare(Users o1, Users o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });

//        Collections.sort(list, new ChatTimeComparator() {
//            @Override
//                    public int compare(Users o1, Users o2) {
//                        return o2.getBloodgroup().compareTo(o1.getBloodgroup());
//
//                    }
//
//                });








        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUid(dataSnapshot.getKey());
                    if (!users.getUid().equals(getInstance().getUid()) && (!users.getUid().equals("7yBNOdB4qcS3eNjoWaQUSy7XOaQ2"))) {

                        list.add(users);

                    }
                    Collections.sort(list, new Comparator<Users>() {
                        @Override
                        public int compare(Users o1, Users o2) {
                            return o2.getTimestamp().compareTo(o1.getTimestamp());
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return binding.getRoot();
    }

//    private void getChats() {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mChats.clear();
//                if (!isAdded()) return;
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Chat chat = snapshot1.getValue(Chat.class);
//                    if(chat.getReceiver().equals(fUser.getUid()) || chat.getSender().equals(fUser.getUid())){
//                        mChats.add(chat);
//                    }
//                }
//                sortUsers();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
//
//    private void sortUsers() {
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fAuth.getUid()).orderByChild("time").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                list.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String userID = Objects.requireNonNull(snapshot.child("id").getValue()).toString();
//                    String time = snapshot.child("time").getValue().toString();
//
//                    Chatlist chatlist = new Chatlist();
//
//
//                    Users userDB = userDao.getAll(userID);
//                    chatlist.setDate(time);
//                    chatlist.setUserName(userDB.getUserName());
//                    chatlist.setUserID(userID);
//
//
//                    list.add(chatlist);
//
//                }
//
//                Collections.sort(list, new Comparator<Chatlist>() {
//                    @Override
//                    public int compare(Chatlist o1, Chatlist o2) {
//                        return Integer.valueOf(o2.getTime().compareTo(o1.getTime()));
//                    }
//                });
//                if (adapter != null) {
//
//                    adapter.notifyDataSetChanged();
//
//                }
//
//            }
//        });
//    }
    //    public static String getTodaydate(long timestamp){
//        try{
//            Date netDate = (new Date(timestamp));
//            SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//            return sfd.format(netDate);
//        } catch(Exception e) {
//
//            return "date";
//        }
//    }
    private void updateToken(String token) {

        Token token1 = new Token();
        FirebaseDatabase.getInstance().getReference("Tokens").child(fAuth.getUid()).setValue(token1);
    }

    private static class ChatTimeComparator implements Comparator<Chat> {
        @Override
        public int compare(Chat chat, Chat t1) {
            return chat.getTimestamp().compareTo(t1.getTimestamp());
        }

    }
}
