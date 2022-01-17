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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText txtOldPassword,txtNewPassword,txtConfirmPassword;
    Button btnSubmit;
    TextView tvBack;
    ProgressBar progressBar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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
                final String newpassword = txtNewPassword.getText().toString().trim();
                String oldpassword = txtOldPassword.getText().toString().trim();
                String confirmpassword = txtConfirmPassword.getText().toString().trim();
                if(TextUtils.isEmpty(newpassword) || TextUtils.isEmpty(oldpassword)
                        || TextUtils.isEmpty(confirmpassword) || newpassword.length() < 6
                        || !newpassword.equals(confirmpassword) || newpassword.equals(oldpassword)) {
                    if (TextUtils.isEmpty(newpassword)) {
                        txtNewPassword.setError("Enter new password!");
                    }
                    else if(newpassword.length() < 6){
                        txtNewPassword.setError("Password too short, enter minimum 6 characters!");
                    }
                    else if(newpassword.equals(oldpassword)){
                        txtNewPassword.setError("Please choose a different password from the old password.");
                    }
                    if (TextUtils.isEmpty(oldpassword)) {
                        txtOldPassword.setError("Enter old password!");
                    }
                    if (TextUtils.isEmpty(confirmpassword)) {
                        txtConfirmPassword.setError("Confirm password!");
                    }else if(!newpassword.equals(confirmpassword)){
                        txtConfirmPassword.setError("Incorrect password");
                    }
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                AuthCredential credential = EmailAuthProvider
                        .getCredential(firebaseUser.getEmail(), oldpassword);
                firebaseUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswordActivity.this);
                                                dialog.setTitle("Success!").setMessage("Your password has been changed.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(ChangePasswordActivity.this,ChatActivity.class));
                                                    }
                                                });
                                                dialog.create().show();
                                            }
                                        }
                                    });
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswordActivity.this);
                                    dialog.setTitle("Error!").setMessage("Invalid old password.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    dialog.create().show();
                                }
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
            }
        });
    }

    private void init() {
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
        txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvBack = (TextView) findViewById(R.id.tvBack);
    }
}
