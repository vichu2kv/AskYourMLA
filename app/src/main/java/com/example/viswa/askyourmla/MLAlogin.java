package com.example.viswa.askyourmla;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MLAlogin extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlalogin);

        loginEmail=(EditText)findViewById(R.id.mlaloginEmail);
        loginPass=(EditText)findViewById(R.id.mlaloginPass);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("MLA's");
    }
    public void mlaLoginButtonClicked(View view)
    {
        String email=loginEmail.getText().toString().trim();
        String pass=loginPass.getText().toString().trim();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass))
        {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        checkUserExists();
                    }
                }
            });
        }
    }
    public void checkUserExists()
    {
        final String user_id=mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    Intent loginIntent=new Intent(MLAlogin.this,MainActivity.class);
                    startActivity(loginIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
