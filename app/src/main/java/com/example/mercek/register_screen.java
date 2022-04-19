package com.example.mercek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register_screen extends AppCompatActivity {

    private ImageView logo_yellow, registerWithFacebookImageView, registerWithTwitterImageView, registerWithGoogleImageView;
    private EditText userNameEditText, mailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton, haveAccountButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        logo_yellow=findViewById(R.id.logo_yellow);
        registerWithFacebookImageView=findViewById(R.id.registerWithFacebookImageView);
        registerWithTwitterImageView=findViewById(R.id.registerWithTwitterImageView);
        registerWithGoogleImageView=findViewById(R.id.registerWithGoogleImageView);
        userNameEditText=findViewById(R.id.userNameEditText);
        mailEditText=findViewById(R.id.mailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        confirmPasswordEditText=findViewById(R.id.confirmPasswordEditText);
        haveAccountButton=findViewById(R.id.haveAccountButton);
        registerWithGoogleImageView=findViewById(R.id.registerWithGoogleImageView);
        registerButton=findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String email = mailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (userName.isEmpty()) {
                    userNameEditText.setError("Kullanıcı Adını doldurunuz!");
                    userNameEditText.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    mailEditText.setError("Maili alanını doldurunuz!");
                    mailEditText.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("Şifre alanını doldurunuz!");
                    userNameEditText.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    confirmPasswordEditText.setError("Onay şifresini doldurunuz!");
                    confirmPasswordEditText.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userNameEditText.setError("Enter the valid email address");
                    userNameEditText.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    passwordEditText.setError("Şifrenin uzunluğu 6 karakter uzun olmalıdır!");
                    passwordEditText.requestFocus();
                    return;
                }/*
                if(password != confirmPassword)
                {
                    confirmPasswordEditText.setError("Şifreler uyuşmuyor!");
                    confirmPasswordEditText.requestFocus();
                    return;
                }*/

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            // Create a new user with a first and last name
                            Map<String, Object> user = new HashMap<>();
                            user.put("userId", userId);
                            user.put("userName", userName);
                            user.put("userMail", email);
                            user.put("userPassword", password);

                            // Add a new document with a generated ID

                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(register_screen.this, "Başarıyla kayıt olundu!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(register_screen.this, home_screen.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(register_screen.this, "Giriş başarız!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                            /*db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            Toast.makeText(register_screen.this, "Başarıyla kayıt olundu!", Toast.LENGTH_SHORT).show();

                                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( task ->{
                                                if(task.isSuccessful()) {
                                                    //Toast.makeText(register_screen.this, "Başarıyla giriş yapıldı!", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(register_screen.this, home_screen.class));
                                                    finish();
                                                }else {
                                                    Toast.makeText(register_screen.this, "Giriş başarız!", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        } else {
                            //Toast.makeText(register_screen.this, "Kayıt olunamadı!", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
            }
        });

        haveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register_screen.this, login_screen.class);
                startActivity(intent);
            }
        });
    }
}