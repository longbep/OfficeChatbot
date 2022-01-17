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

import com.example.officechatbot.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity {
    TextView tvSignIn;
    Button btnSignUp;
    ProgressBar progressBar;
    EditText txtEmail,txtPassword,txtName;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });
    }
    private void SignUp(){
        final String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        final String name = txtName.getText().toString().trim();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || password.length() < 6 || !email.contains("@")) {
            if (TextUtils.isEmpty(email)) {
                txtEmail.setError("Enter email!");
            }
            else if(!email.contains("@")){
                txtEmail.setError("Invalid email, contain '@'");
            }
            if (TextUtils.isEmpty(name)) {
                txtName.setError("Enter name!");
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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            User user = new User(email,name,"default","default","default","Male","1/1/1900");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this);
                                    dialog.setTitle("Success!").setMessage("Your account has been created. You can now sign in your account.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(SignupActivity.this,SigninActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    dialog.create().show();
                                }
                            });
                        } else {
                            txtEmail.setError("Email is already taken");
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
        txtName.setText("");

        txtEmail.setError(null);
        txtPassword.setError(null);
        txtName.setError(null);

    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtOldPassword);
        txtPassword = (EditText) findViewById(R.id.txtNewPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
