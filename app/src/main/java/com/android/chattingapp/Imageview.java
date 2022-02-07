package com.android.chattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class Imageview extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        ImageView images=findViewById(R.id.image);
        getSupportActionBar().hide();

        Intent intent=getIntent();
        String image = getIntent().getStringExtra("image");

        Picasso.get().load(image).into(images);
    }
}
