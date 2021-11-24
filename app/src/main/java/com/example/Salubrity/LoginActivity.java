package com.example.Salubrity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{

    private EditText emailAddress, password;

    Button btnLogin;

    private FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.loginButton);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {
        final String email = emailAddress.getText().toString();
        String passW = password.getText().toString();

        if(email.isEmpty() || !email.contains("@gmail.com")){
            showError(emailAddress, "Your email is invalid. @gmail.com");
        }else if(passW.isEmpty() || password.length()<7){
            showError(password, "Password must be 7 characters");
        }else{
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait while check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email, passW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();

                    mLoadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("email", email);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}