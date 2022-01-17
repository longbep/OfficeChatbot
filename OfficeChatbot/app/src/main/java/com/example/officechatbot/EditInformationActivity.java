package com.example.officechatbot;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.officechatbot.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class EditInformationActivity extends AppCompatActivity {
    ImageView profile_avatar;
    TextView tvEmail;
    EditText txtName,txtPhoneNumber,txtIDNumber,txtDateOfBirth;
    RadioGroup radioGroup;
    RadioButton rbMale,rbFemale,rbOther;
    Button btnSave,btnCancel;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    DatePickerDialog.OnDateSetListener dateSetListener;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        init();
        txtDateOfBirth.setKeyListener(null);

        //Hien thi thong tin
        reference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getAvatar().equals("default")){
                    profile_avatar.setImageResource(R.drawable.default_avatar);
                }
                else{
                    GlideApp.with(EditInformationActivity.this)
                            .load(user.getAvatar())
                            .into(profile_avatar);
                }
                tvEmail.setText(user.getEmail());
                txtName.setText(user.getName());
                txtDateOfBirth.setText(user.getDateOfBirth());
                if(user.getIdNumber().equals("default")){
                    txtIDNumber.getText().clear();
                }
                else {
                    txtIDNumber.setText(user.getIdNumber());
                }
                if(user.getPhone().equals("default")){
                    txtPhoneNumber.getText().clear();
                }
                else {
                    txtPhoneNumber.setText(user.getPhone());
                }
                if(user.getGender().equals("Male")){
                    rbMale.setChecked(true);
                }
                else if(user.getGender().equals("Female")){
                    rbFemale.setChecked(true);
                }
                else{
                    rbOther.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Mo Datepicker
        txtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditInformationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDateOfBirth.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        };

        //Thay doi anh dai dien
        profile_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        //Nut Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Nut Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtName.getText())) {
                    txtName.setError("Enter name!");
                }
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(EditInformationActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //Luu hinh anh len Firebase
                    Calendar calendar = Calendar.getInstance();
                    final StorageReference imageRef = storageReference.child("image" + calendar.getTimeInMillis()+".png");
                    profile_avatar.setDrawingCacheEnabled(true);
                    profile_avatar.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) profile_avatar.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    final UploadTask uploadTask = imageRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditInformationActivity.this);
                            dialog.setTitle("Error!").setMessage("Cannot save the image file.").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.create().show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
                            reference.child("dateOfBirth").setValue(txtDateOfBirth.getText().toString());
                            reference.child("name").setValue(txtName.getText().toString());
                            reference.child("idNumber").setValue(txtIDNumber.getText().toString());
                            reference.child("phone").setValue(txtPhoneNumber.getText().toString());
                            int idrb = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) findViewById(idrb);
                            reference.child("gender").setValue(radioButton.getText().toString());
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return imageRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult() != null) {
                                            reference.child("avatar").setValue(task.getResult().toString());
                                        }
                                        startActivity(new Intent(EditInformationActivity.this,ChatActivity.class));
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IMAGE_REQUEST && resultCode==RESULT_OK && data != null){
            Uri uri = data.getData();
            GlideApp.with(EditInformationActivity.this)
                    .load(uri)
                    .into(profile_avatar);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void init() {
        profile_avatar = (ImageView) findViewById(R.id.profile_avatar);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        txtName = (EditText) findViewById(R.id.txtName);
        txtIDNumber = (EditText) findViewById(R.id.txtIDNumber);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtDateOfBirth = (EditText) findViewById(R.id.txtDateOfBirth);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        rbOther = (RadioButton) findViewById(R.id.rbOther);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
    }
}
