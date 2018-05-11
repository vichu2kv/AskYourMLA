package com.example.viswa.askyourmla;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Messages extends AppCompatActivity {

    private static int Sign_In_REQUEST_CODE=1;
    private FirebaseListAdapter<Chat> adapter;
    RelativeLayout activity_messages;
    FloatingActionButton fab;
    private String postkey=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        postkey=getIntent().getExtras().getString("postid");
        activity_messages=(RelativeLayout)findViewById(R.id.activity_messages);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input=(EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child("MLAapp").child(postkey).child("Messages").push().setValue(new Chat(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });
        //Check if, not signed in then navigate sign in page
            Snackbar.make(activity_messages,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            dispalyChatMessage();

    }
    private void dispalyChatMessage() {
        ListView listofmessages=(ListView)findViewById(R.id.list_of_messages);
        adapter=new FirebaseListAdapter<Chat>(this,Chat.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference().child("MLAapp").child(postkey).child("Messages"))
        {
            @Override
            protected void populateView(View v, Chat model, int position) {
                //get references to the views of list_item.xml
                TextView messageText,messageTime,messageUser;
                messageText=(TextView)v.findViewById(R.id.message_text);
                messageTime=(TextView)v.findViewById(R.id.message_time);
                messageUser=(TextView)v.findViewById(R.id.message_user);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy(HH:mm:ss)",model.getMessageTime()));
            }
        };
        listofmessages.setAdapter(adapter);
    }
}
