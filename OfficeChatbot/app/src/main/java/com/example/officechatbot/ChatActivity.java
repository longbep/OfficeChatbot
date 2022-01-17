package com.example.officechatbot;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.officechatbot.Model.Message;
import com.example.officechatbot.Model.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    EditText txtMessage;
    ImageView profile_avatar;
    TextView profile_name,profile_email;
    ImageButton btnSend,btnVoice;
    RecyclerView recyclerView;
    List<Message> messageList;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    FirebaseUser firebaseUser;
    DrawerLayout drawerLayout;
    NavigationView nav_menu;
    Toolbar toolbar;
    FirebaseAuth auth;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.56.1:5005");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mSocket.on("bot_send_message", onNewMessage_botSendMessage);
        mSocket.connect();
        auth = FirebaseAuth.getInstance();
        init();

        //Cai dat toolbar
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Doc du lieu chat tren Firebase
        reference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                profile_avatar = (ImageView) findViewById(R.id.profile_avatar);
                profile_name = (TextView) findViewById(R.id.profile_name);
                profile_email = (TextView) findViewById(R.id.profile_email);
                //Hien thi thong tin ng dung
                if(user.getAvatar().equals("default")){
                    profile_avatar.setImageResource(R.drawable.default_avatar);
                }
                else{
                    GlideApp.with(ChatActivity.this)
                            .load(user.getAvatar())
                            .into(profile_avatar);
                }
                profile_name.setText(user.getName());
                profile_email.setText(user.getEmail());

                messageList = new ArrayList<>();
                reference.child("Chats").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messageList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Message msg = snapshot.getValue(Message.class);
                            if(msg.getMessageOf().equals(firebaseUser.getUid())){
                                messageList.add(msg);
                            }
                        }
                        if(messageList.size()==0){
                            Date currentTime = Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                            Message messageBot = new Message("Xin chào. Tôi là Office Chatbot. Tôi có thể giúp gì cho bạn?",true,firebaseUser.getUid(),dateFormat.format(currentTime),timeFormat.format(currentTime));
                            reference.child("Chats").child(firebaseUser.getUid()).push().setValue(messageBot);
                        }
                        messageAdapter = new MessageAdapter(ChatActivity.this,messageList,user.getAvatar());
                        recyclerView.setAdapter(messageAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Nut Voice
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something…");
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                    dialog.setTitle("Error!").setMessage("Sorry! Your device doesn't support speech input ").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.create().show();
                }
            }
        });

        //Su kien gui tin nhan
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txtMessage.getText().toString();
                if(!msg.equals("")){
                    Date currentTime = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    Message message = new Message(msg,false,firebaseUser.getUid(),dateFormat.format(currentTime),timeFormat.format(currentTime));
                    reference.child("Chats").child(firebaseUser.getUid()).push().setValue(message);
                    JSONObject data = new JSONObject();
                    try {
                        data.put("message",msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit("user_send_message", data);
                    txtMessage.getText().clear();

                }
            }
        });

        nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.menu_signout:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                        dialog.setTitle("Confirmation").setMessage("Are you sure you want to sign out?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //FirebaseAuth.getInstance().signOut();
                                auth.signOut();
                                startActivity(new Intent(ChatActivity.this,SigninActivity.class));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.create().show();
                        return true;
                    case R.id.menu_edit:
                        startActivity(new Intent(ChatActivity.this,EditInformationActivity.class));
                        return true;
                    case R.id.menu_changepassword:
                        startActivity(new Intent(ChatActivity.this,ChangePasswordActivity.class));
                        return true;
                    case R.id.menu_info:
                        startActivity(new Intent(ChatActivity.this,AboutActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    private Emitter.Listener onNewMessage_botSendMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String msg;
                    try {
                        msg = data.getString("text");
                        Date currentTime = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        Message messageBot = new Message(msg,true,firebaseUser.getUid(),dateFormat.format(currentTime),timeFormat.format(currentTime));
                        reference.child("Chats").child(firebaseUser.getUid()).push().setValue(messageBot);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Trả lại dữ liệu sau khi nhập giọng nói vào
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtMessage.setText(result.get(0));
                }
                break;
            }

        }
    }

    private void init(){
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnVoice = (ImageButton) findViewById(R.id.btnVoice);
        recyclerView = (RecyclerView) findViewById(R.id.rvListMessage);
        reference = FirebaseDatabase.getInstance().getReference();
        messageList = new ArrayList<Message>();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_menu = (NavigationView) findViewById(R.id.nav_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile_avatar = (ImageView) findViewById(R.id.profile_avatar);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_email = (TextView) findViewById(R.id.profile_email);
    }

    @Override
    public void onBackPressed() {

    }

}
