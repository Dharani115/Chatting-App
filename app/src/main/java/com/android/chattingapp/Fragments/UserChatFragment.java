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

import com.android.chattingapp.Adapters.UserPatientAdapter;
import com.android.chattingapp.Models.Users;
import com.android.chattingapp.Notification.Token;
import com.android.chattingapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserChatFragment extends Fragment {

    public UserChatFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList();
    FirebaseDatabase database;
    Users users;
    FirebaseUser fAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();

        UserPatientAdapter adapter = new UserPatientAdapter(list, getContext());




        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        fAuth = FirebaseAuth.getInstance().getCurrentUser();



        String id="4oCgArfvMSdxzxqGmjLZqXhR9Dh2";


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.orderByChild("uid").equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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


                    private void OpenErrorAlrt(String mobile_number_already_registed) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        updateToken(FirebaseInstanceId.getInstance().getToken());


        return binding.getRoot();
    }
    private void updateToken(String token){
        Token token1=new Token();
        FirebaseDatabase.getInstance().getReference("Tokens").child(fAuth.getUid()).child("token").setValue(token1);
    }
}