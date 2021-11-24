package com.example.Salubrity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button register;

    private EditText emailAddress, password, confirmPassword;

    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.button_register);

        emailAddress = findViewById(R.id.email_address_register);
        password = findViewById(R.id.password_register);
        confirmPassword = findViewById(R.id.retype_password_register);
        mAuth=FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
    }

    private void checkCredentials() {
        String email = emailAddress.getText().toString();
        String passW = password.getText().toString();
        String confirmPassW = confirmPassword.getText().toString();

        if(email.isEmpty() || !email.contains("@gmail.com")){
            showError(emailAddress, "Your email is invalid. @gmail.com");
        }else if(passW.isEmpty() || password.length()<7){
            showError(password, "Password must be 7 characters");
        }else if(confirmPassW.isEmpty() || !confirmPassW.equals(passW)){
            showError(confirmPassword, "Password does not match!");
        }else{
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait, while we are checking your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, passW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "You Successfully Registered", Toast.LENGTH_SHORT).show();

                        mLoadingBar.dismiss();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}