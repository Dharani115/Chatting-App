package com.android.chattingapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.chattingapp.Imageview;
import com.android.chattingapp.Models.Chat;
import com.android.chattingapp.Models.Users;
import com.android.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    int MSG_TYPE_LEFT = 0;
    int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    public MessageAdapter(ArrayList<Users> list, Context context) {
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.sender_layout_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.reciver_layout_item, parent, false);
            return new ViewHolder(view);
        }
    }



    public static String getTimeDate(long timestamp){
        try{
            Date netDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sfd.format(netDate);
        } catch(Exception e) {

            return "date";
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete Message ?")
                        .setMessage("Are You sure want to delete the message")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderRoom=FirebaseAuth.getInstance().getUid();
                                database.getReference().child("chats").child(senderRoom)
                                        .setValue(null);


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();


                return false;
            }
        });





        String test2 = getTimeDate((long) chat.getTimestamp());
        holder.show_time.setText(test2);


        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else if(chat.isIsseen() == false){
                holder.txt_seen.setText("Sent");
            }
            else{
                holder.txt_seen.setVisibility(View.GONE);
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);

        }
        if (chat.getType().equals("text")){
            holder.show_image.setVisibility(View.GONE);
            holder.show_pdf.setVisibility(View.GONE);
            holder.show_message.setText(chat.getMessage());

        }
        else if (chat.getType().equals("image")) {
            holder.show_message.setVisibility(View.GONE);
            holder.show_pdf.setVisibility(View.GONE);
            Picasso.get().load(chat.getMessage()).into(holder.show_image);
            holder.show_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ieventreport = new Intent(mContext, Imageview.class);
                    ieventreport.putExtra("image", chat.getMessage());
                    mContext.startActivity(ieventreport);
                }
            });
        }
        else if (chat.getType().equals("pdf")) {
            holder.show_message.setVisibility(View.GONE);;
            holder.show_image.setVisibility(View.GONE);
            holder.show_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(chat.getMessage()));
                    mContext.startActivity(intent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public TextView show_pdf;
        public TextView show_time;
        public ImageView show_image;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.textmsg);
            show_time = itemView.findViewById(R.id.time);
            show_image = itemView.findViewById(R.id.images);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            show_pdf = itemView.findViewById(R.id.pdf);
//            show_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ImageView img=show_image;
//                    Intent ieventreport = new Intent(mContext, Imageview.class);
//                    ieventreport.putExtra("image", img);
//                    mContext.startActivity(ieventreport);
//                }
//            });

        }
    }



    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}