package com.example.officechatbot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {
    TextView tvSignUp,tvForgetPassword;
    Button btnSignIn;
    ProgressBar progressBar;
    EditText txtEmail,txtPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }
    private void SignIn(){
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || password.length() < 6 || !email.contains("@")) {
            if (TextUtils.isEmpty(email)) {
                txtEmail.setError("Enter email!");
            }
            else if(!email.contains("@")){
                txtEmail.setError("Invalid email, contain '@'");
            }
            if (TextUtils.isEmpty(password)) {
                txtPassword.setError("Enter password!");
            }
             else if (password.length() < 6) {
                txtPassword.setError("Password too short, enter minimum 6 characters!");
            }
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SigninActivity.this,ChatActivity.class);
                            startActivity(intent);
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(SigninActivity.this);
                            dialog.setTitle("Error!").setMessage("Wrong email or password!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.create().show();
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
    }
    @Override
    public void onResume()
    {
        super.onResume();
        txtEmail.setText("");
        txtPassword.setText("");

        txtEmail.setError(null);
        txtPassword.setError(null);
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        tvSignUp = (TextView) findViewById(R.id.tvSignIn);
        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        txtEmail = (EditText) findViewById(R.id.txtOldPassword);
        txtPassword = (EditText) findViewById(R.id.txtNewPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
