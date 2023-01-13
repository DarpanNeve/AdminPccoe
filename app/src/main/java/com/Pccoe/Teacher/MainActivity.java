package com.Pccoe.Teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ImageView logout,logo,sidephoto;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String email;
    TextView heading,sidename;
    NavigationView navbar;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        email=firebaseUser.getEmail();
        assert email != null;
        if(!email.contains("@pccoepune.org")){
            signout();
            toast("Login with College ID");
        }
        replaceFragment(new Profile());
        navbar=findViewById(R.id.navbar);
        drawer=findViewById(R.id.drawer);
        View header = navbar.getHeaderView(0);
        logout=findViewById(R.id.logout);
        heading=findViewById(R.id.heading);
        logo=findViewById(R.id.logo);
        sidephoto=header.findViewById(R.id.sidephoto);
        sidename=header.findViewById(R.id.sidename);
        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId()){
                    case R.id.profile1:
                        replaceFragment(new Profile());
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.timetable1:
                        replaceFragment(new DailyTimeTable());
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.weeklytimetable:
                        drawer.closeDrawer(GravityCompat.START);
                        Intent myIntent = new Intent(MainActivity.this, WeeklyTImeTable.class);
                        MainActivity.this.startActivity(myIntent);
                        break;
                    case R.id.teacherweeklytimetable:
                        drawer.closeDrawer(GravityCompat.START);
                        Intent my1Intent = new Intent(MainActivity.this,TeacherWeeklyTImeTable.class);
                        MainActivity.this.startActivity(my1Intent);
                        break;
                    case R.id.classroomweeklytimetable:
                        drawer.closeDrawer(GravityCompat.START);
                        Intent my2Intent = new Intent(MainActivity.this,ClassroomWeeklyTimeTable.class);
                        MainActivity.this.startActivity(my2Intent);
                        break;
                    case R.id.Notice:
                        drawer.closeDrawer(GravityCompat.START);
                        Intent Intent1 = new Intent(MainActivity.this, NoticeActivity.class);
                        MainActivity.this.startActivity(Intent1);
                        break;
                    case R.id.about:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.pccoepune.com")));
                        break;
                    case R.id.feedback:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://forms.gle/QYpzZLkCHiLoC6E46")));
                        break;
                    case R.id.logout1:
                        drawer.closeDrawer(GravityCompat.START);
                        signout();
                        break;

                }
                return false;
            }
        });
        Glide.with(this).load(firebaseUser.getPhotoUrl()).circleCrop().into(logout);
        if(firebaseUser!=null)
        {
            // When firebase user is not equal to null
            // Set image on image view
            Glide.with(this).load(firebaseUser.getPhotoUrl()).circleCrop().into(sidephoto);
            // set name on text view
            sidename.setText(firebaseUser.getDisplayName());
        }
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out from google
                signout();
                toast("Signout successful");
            }
        });
    }
    private void signout() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    firebaseAuth.signOut();
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    finish();
                }
            }
        });
    }
    private void toast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2, fragment);
        fragmentTransaction.commit();
    }
}