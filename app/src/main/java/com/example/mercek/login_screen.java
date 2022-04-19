package com.example.mercek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_screen extends AppCompatActivity {

    private ImageView logo_yellow, loginWithFacebookImageView, loginWithTwitterImageView, loginWithGoogleImageView;
    private EditText mailEditText, passwordEditText;
    private CheckBox rememberMeTextView;
    private TextView forgotPasswordTextView, loginWithSocialNetworkTextView;
    private Button loginButton, dontHaveAccountButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        logo_yellow=findViewById(R.id.logo_yellow);
        //loginWithFacebookImageView=findViewById(R.id.loginWithFacebookImageView);
        //loginWithTwitterImageView=findViewById(R.id.loginWithTwitterImageView);
        loginWithGoogleImageView=findViewById(R.id.loginWithGoogleImageView);
        mailEditText=findViewById(R.id.mailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        rememberMeTextView=findViewById(R.id.rememberMeTextView);
        forgotPasswordTextView=findViewById(R.id.forgotPasswordTextView);
        dontHaveAccountButton=findViewById(R.id.dontHaveAccountButton);
        loginWithSocialNetworkTextView=findViewById(R.id.loginWithSocialNetworkTextView);
        loginButton=findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                mAuth.signInWithEmailAndPassword("keremcikikci@gmail.com", "893042").addOnCompleteListener( task ->{
                    if(task.isSuccessful()) {
                        //Toast.makeText(login_screen.this, "Başarıyla giriş yapıldı!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(login_screen.this, home_screen.class));
                        finish();
                    }else {
                        Toast.makeText(login_screen.this, "Giriş başarız!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        dontHaveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_screen.this, register_screen.class);
                startActivity(intent);
            }
        });

    }
}