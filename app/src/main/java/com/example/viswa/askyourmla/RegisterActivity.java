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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText namefield;
    private EditText emailfield;
    private EditText passfield;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        namefield=(EditText)findViewById(R.id.nameField);
        emailfield=(EditText)findViewById(R.id.emailField);
        passfield=(EditText)findViewById(R.id.passField);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
    }
    public void registerButtonClicked(View view)
    {
        final String name=namefield.getText().toString().trim();
        String email=emailfield.getText().toString().trim();
        String pass=passfield.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
        {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        String user_id=mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db=mDatabase.child(user_id);
                        current_user_db.child("Name").setValue(name);
                        current_user_db.child("Image").setValue("default");

                        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                }
            });
        }
    }
}
