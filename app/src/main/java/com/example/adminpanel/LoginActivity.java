package com.example.adminpanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    FirebaseUser firebaseUser;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logo=findViewById(R.id.Pccoe);
        firebaseAuth = FirebaseAuth.getInstance();
        googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("873112653322-asbm6uhvint6h7n5tnkuh8bijmv13cvd.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        else {
            Intent intent=mGoogleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent,100);
        }
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=mGoogleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent,100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if(requestCode==100)
        {
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful())
            {
                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    if(googleSignInAccount!=null)
                    {
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        if(task.isSuccessful())
                                        {   // When task is successful
                                            // Redirect to profile activity
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        }
                                        else
                                        {
                                            // When task is unsuccessful
                                            // Display Toast
                                            toast("Authentication Failed :"+ Objects.requireNonNull(task.getException())
                                                    .getMessage());
                                        }
                                    }
                                });
                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
    private void toast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}