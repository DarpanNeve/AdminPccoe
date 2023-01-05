package com.example.adminpanel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoticeActivity extends AppCompatActivity {
    ImageView logout,logo,sendbutton;
    RecyclerView recyclerView;
    NoticeAdapter NoticeAdapter;
    DatabaseReference mDatabase;
    EditText sendmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        logout=findViewById(R.id.logout);
        logo=findViewById(R.id.logo);
        sendbutton=findViewById(R.id.sendbutton);
        Glide.with(this).load(R.drawable.ic_baseline_send_24).circleCrop().into(sendbutton);
        recyclerView=findViewById(R.id.recyclerView);
        sendmessage=findViewById(R.id.uploadtext);
        sendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendbutton.setBackgroundColor(Color.RED);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        FirebaseRecyclerOptions<NoticeModel> options
                = new FirebaseRecyclerOptions.Builder<NoticeModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("chats"), NoticeModel.class)
                .build();
        NoticeAdapter =new NoticeAdapter(options);
        recyclerView.setAdapter(NoticeAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        NoticeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NoticeAdapter.stopListening();
    }
}