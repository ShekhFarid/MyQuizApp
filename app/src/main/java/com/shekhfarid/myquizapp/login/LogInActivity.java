package com.shekhfarid.myquizapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shekhfarid.myquizapp.R;
import com.shekhfarid.myquizapp.databinding.ActivityLogInBinding;
import com.shekhfarid.myquizapp.home.HomeActivity;


public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding logInBinding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInBinding = DataBindingUtil.setContentView(LogInActivity.this, R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        logInBinding.registationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = logInBinding.userTextID.getText().toString().trim();
                String password = logInBinding.passTextID.getText().toString().trim();
                registation(username,password);
            }

        });
        logInBinding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = logInBinding.userTextID.getText().toString().trim();
                String password = logInBinding.passTextID.getText().toString().trim();
                login(username,password);
            }
        });
    }
    public void registation(String username,String password){
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this,"Success",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LogInActivity.this,"Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void login(String username,String password){
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LogInActivity.this,"Success",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LogInActivity.this,"Failed"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
