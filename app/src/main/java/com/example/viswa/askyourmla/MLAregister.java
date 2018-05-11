package com.example.viswa.askyourmla;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MLAregister extends AppCompatActivity {

    private EditText namefield;
    private EditText emailfield;
    private EditText passfield;
    private Spinner constituency;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlaregister);

        namefield=(EditText)findViewById(R.id.mlanameField);
        emailfield=(EditText)findViewById(R.id.mlaemailField);
        passfield=(EditText)findViewById(R.id.mlapassField);
        constituency=(Spinner)findViewById(R.id.constituency);
        String[] items={"Coimbatore(North)","Coimbatore(South)","Mettupalayam","sulur","Singanallur","Thondamuthur","Pollachi","Valparai"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        constituency.setAdapter(adapter);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("MLA's");
    }
    public void mlaRegisterButtonClicked(View view)
    {
        final String name=namefield.getText().toString().trim();
        String email=emailfield.getText().toString().trim();
        String pass=passfield.getText().toString().trim();
        final String con=constituency.getSelectedItem().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(con))
        {
            Toast.makeText(MLAregister.this,"If condition passed",Toast.LENGTH_LONG).show();

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
                        current_user_db.child("Constituency").setValue(con);

                        Intent mainIntent=new Intent(MLAregister.this,MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                }
            });
        }
    }
}
