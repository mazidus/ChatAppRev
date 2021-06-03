package com.mzds.chat4.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mzds.chat4.R;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    TextView txt_signup;
    EditText login_email,login_pass;
    TextView signIn_btn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Wait");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();

        txt_signup=findViewById(R.id.txt_Register);
        signIn_btn=findViewById(R.id.login_button);
        login_email=findViewById(R.id.login_email);
        login_pass=findViewById(R.id.login_pass);

        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=login_email.getText().toString();
                String pass=login_pass.getText().toString();
                progressDialog.show();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "enter email & password", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "pass must 6", Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}