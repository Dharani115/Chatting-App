package com.android.chattingapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//public class ChatAdapter extends RecyclerView.Adapter {
//    Context context;
//    ArrayList<Chat> chats;
//    String recId;
//    int SENDER_VIEW_TYPE= 1;
//    int RECEIVER_VIEW_TYPE= 2;
//
//    public ChatAdapter(Context context, ArrayList<Chat> chats) {
//        this.context = context;
//        this.chats = chats;
//    }
//
//    public ChatAdapter(Context context, ArrayList<Chat> chats, String recId) {
//        this.context = context;
//        this.chats = chats;
//        this.recId = recId;
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType==SENDER_VIEW_TYPE){
//            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
//            return new SenderViewVolder(view);
//
//        }
//        else{
//            View view= LayoutInflater.from(context).inflate(R.layout.reciver_layout_item,parent,false);
//            return new RecieverViewVolder(view);
//        }
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        if(chats.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
//            return SENDER_VIEW_TYPE;
//
//        }else{
//            return RECEIVER_VIEW_TYPE;
//        }
//
//    }
//    public static String getTimeDate(long timestamp){
//        try{
//            Date netDate = (new Date(timestamp));
//            SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//            return sfd.format(netDate);
//        } catch(Exception e) {
//
//            return "date";
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Chat chat = chats.get(position);
//
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                new AlertDialog.Builder(context)
//                        .setTitle("Delete Message ?")
//                        .setMessage("Are You sure want to delete the message")
//                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                FirebaseDatabase database=FirebaseDatabase.getInstance();
//                                String senderRoom=FirebaseAuth.getInstance().getUid()+  recId;
//                                database.getReference().child("chats").child(senderRoom)
//                                        .child(chat.getMessageId())
//                                        .setValue(null);
//
//
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                    }
//                }).show();
//
//
//                return false;
//            }
//        });
//
//
//
//        if (holder.getClass()==SenderViewVolder.class){
//
//
//            String test2 = getTimeDate((long) chat.getTimestamp());
//
//            ((SenderViewVolder)holder).senderMsg.setText(chat.getMessgae());
//
//            ((SenderViewVolder)holder).senderTime.setText(test2);
//        }else{
//            String test2 = getTimeDate((long) chat.getTimestamp());
//            ((RecieverViewVolder)holder).receiverMsg.setText(chat.getMessgae());
//
//            ((RecieverViewVolder)holder).receiverTime.setText(test2);
//
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return chats.size();
//    }
//
//    public class RecieverViewVolder extends RecyclerView.ViewHolder{
//        TextView receiverMsg, receiverTime;
//        public RecieverViewVolder(@NonNull View itemView) {
//            super(itemView);
//            receiverMsg=itemView.findViewById(R.id.receiverText);
//            receiverTime=itemView.findViewById(R.id.receiverTime);
//        }
//    }
//
//    public class SenderViewVolder extends RecyclerView.ViewHolder {
//
//        TextView senderMsg, senderTime;
//
//        public SenderViewVolder(@NonNull View itemView) {
//            super(itemView);
//            senderMsg=itemView.findViewById(R.id.senderText);
//            senderTime=itemView.findViewById(R.id.senderTime);
//        }
//    }

//}
