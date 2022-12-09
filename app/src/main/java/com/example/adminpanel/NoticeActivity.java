package com.example.adminpanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoticeActivity extends AppCompatActivity {
    ImageView logout,logo;
    RecyclerView recyclerView;
    NoticeAdapter NoticeAdapter;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        logout=findViewById(R.id.logout);
        logo=findViewById(R.id.logo);
        recyclerView=findViewById(R.id.recyclerView);

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