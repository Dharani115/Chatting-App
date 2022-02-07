package com.android.chattingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chattingapp.MessageActivity;
import com.android.chattingapp.Models.Chat;
import com.android.chattingapp.Models.Users;
import com.android.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPatientAdapter extends RecyclerView.Adapter<UserPatientAdapter.ViewHolder>{

    ArrayList<Users> list;
    Context context;
    String theLastMessage;
    String lastmsgdate;
    Boolean msgcount;
    String todaydate;




    public UserPatientAdapter(ArrayList<Users> list,Context context){
        this.list = list;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView username;
        TextView lastMessage;
        TextView senderTime;
        CardView msgcnt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            username = itemView.findViewById(R.id.usernamelist);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            senderTime = itemView.findViewById(R.id.senderTime);
            msgcnt = itemView.findViewById(R.id.msgcount);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users users = list.get(position);

        Picasso.get().load(users.getImageUri()).placeholder(R.drawable.avatar).into(holder.image);
        holder.username.setText(users.getName());
        lastMessage(users.getUid(), holder.lastMessage);
        lastmsgdate(users.getUid(), holder.senderTime);
        messagecount(users.getUid(), holder.msgcnt);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (context, MessageActivity.class);
                intent.putExtra("userId", users.getUid());
                intent.putExtra("profilepic", users.getImageUri());
                intent.putExtra("UserName", users.getName());
//                Pair[] pairs= new Pair[2];
//                pairs[0]=new Pair<View,String>(holder.image,"imagetransition");
//                pairs[1]=new Pair<View,String>(holder.username,"nametransition");
//                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(UserPatientAdapter.this,pairs);
                context.startActivity(intent);

            }
        });

    }
    private void messagecount(String uid, CardView msgcnt) {
        msgcount =true;
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(uid)
                               ) {
                            msgcount = chat.isIsseen();
                            msgcnt.setVisibility(View.VISIBLE);

                        }
                    }
                }

                if (msgcount) {
                    msgcnt.setVisibility(View.GONE);
                } else if(msgcount=false) {
                    msgcnt.setVisibility(View.VISIBLE);
                }
                else {
                }

                msgcount = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static String getTimeDate(long timestamp){
        try{
            Date netDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sfd.format(netDate);
        } catch(Exception e) {

            return "date";
        }
    }
    public static String getTodaydate(long timestamp){
        try{
            Date netDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sfd.format(netDate);
        } catch(Exception e) {

            return "date";
        }
    }
    private void lastmsgdate(String uid, TextView senderTime) {
        lastmsgdate = "default";
        todaydate="default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(uid) ||
                                chat.getReceiver().equals(uid) && chat.getSender().equals(firebaseUser.getUid())) {
                            lastmsgdate = getTimeDate(chat.getTimestamp());
                            todaydate = getTodaydate(chat.getTimestamp());
                            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                            if(date.equals(lastmsgdate)){
                                senderTime.setText(todaydate);
                            }
                            else{
                                senderTime.setText(lastmsgdate);
                            }
                        }
                    }
                }



                lastmsgdate = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lastMessage(final String userid, final TextView last_msg) {
        theLastMessage = "default";
        msgcount=true;
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                            msgcount = chat.isIsseen();


                        }
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("Hay there i am using Caremee");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public int getItemCount() {

        return list.size();
    }


}
