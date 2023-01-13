package com.example.adminpanel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class NoticeActivity extends AppCompatActivity {
    ImageView logout,logo,sendbutton;
    RecyclerView recyclerView;
    NoticeAdapter NoticeAdapter;
    DatabaseReference DatabaseReference;
    EditText sendmessage;
    String message,name,email1;
    Date time;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    data data;
    int id=0;
    FirebaseDatabase firebaseDatabase;
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
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference=firebaseDatabase.getReference().child("chats");
        googleSignInClient= GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        FirebaseRecyclerOptions<NoticeModel> options
                = new FirebaseRecyclerOptions.Builder<NoticeModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("chats"), NoticeModel.class)
                .build();
        NoticeAdapter =new NoticeAdapter(options);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true); // last argument (true) is flag for reverse layout
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(NoticeAdapter);
        data = new data();
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
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sendmessage.getText().toString().length()!=0){
                    email1=firebaseUser.getEmail();
                    assert email1 != null;
                    email1=email1.replace("."," ");
                    email1=email1.replace("@pccoepune org","");
                    email1 = email1.replaceAll("[0-9]","");
                    email1=email1.toUpperCase();
                    message=sendmessage.getText().toString();
                    time= Calendar.getInstance().getTime();
                    name=email1;
                    addDatatoFirebase(name,message,time);

                }
            }

            private void addDatatoFirebase(String sender, String message, Date time) {
                // below 3 lines of code is used to set
                // data in our object class.
                data.setName(sender);
                data.setMessage(message);
                data.setTime(String.valueOf(time));

                DatabaseReference.child(String.valueOf(id+1)).setValue(data);

                // we are use add value event listener method
                // which is called with database reference.
                DatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // inside the method of on Data change we are setting
                        // our object class to our database reference.
                        // data base reference will sends data to firebase.
                        // after adding this data we are showing toast message.
                        Toast.makeText(NoticeActivity.this, "data added", Toast.LENGTH_SHORT).show();
                        sendmessage.setText("");
                        sendnotification();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // if the data is not added or it is cancelled then
                        // we are displaying a failure toast message.
                        Toast.makeText(NoticeActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
// ...
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NoticeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendnotification() {
        DatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    id=(int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    NotificationChannel channel=new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager=getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "n");
                builder.setSmallIcon(R.drawable.pccoelogo);

                builder.setContentTitle(name);
                builder.setAutoCancel(true);
                builder.setContentText(message);
                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplicationContext());
                managerCompat.notify(999,builder.build());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        NoticeAdapter.notifyDataSetChanged();
        NoticeAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        NoticeAdapter.stopListening();
    }
}