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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText txtEmail;
    Button btnSubmit;
    TextView tvBack;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        init();

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email) || !email.contains("@")) {
                    if (TextUtils.isEmpty(email)) {
                        txtEmail.setError("Enter email!");
                    } else if (!email.contains("@")) {
                        txtEmail.setError("Invalid email, contain '@'");
                    }
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ForgetPasswordActivity.this);
                            dialog.setTitle("Success!").setMessage("We have sent you instructions to reset your password!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(ForgetPasswordActivity.this,SigninActivity.class));
                                }
                            });
                            dialog.create().show();
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ForgetPasswordActivity.this);
                            dialog.setTitle("Fail").setMessage("Failed to send reset email!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.create().show();
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        txtEmail = (EditText) findViewById(R.id.txtOldPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvBack = (TextView) findViewById(R.id.tvBack);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
