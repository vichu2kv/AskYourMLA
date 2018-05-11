package com.example.viswa.askyourmla;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST=2;
    private Uri uri=null;
    private ImageButton imageButton;
    private Spinner location;
    private EditText desc;
    private StorageReference storageReference;
    private LinearLayout activity_post;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        location=(Spinner) findViewById(R.id.editlocation);
        String[] items={"Coimbatore(North)","Coimbatore(South)","Mettupalayam","sulur","Singanallur","Thondamuthur","Pollachi","Valparai"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        location.setAdapter(adapter);
        desc=(EditText)findViewById(R.id.editdesc);
        storageReference=FirebaseStorage.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("MLAapp");
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
    }

    public void imageButtonClicked(View view)
    {
        Intent galleryintent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("Image/*");
        startActivityForResult(galleryintent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            uri=data.getData();
            imageButton=(ImageButton)findViewById(R.id.imagebutton);
            imageButton.setImageURI(uri);
        }
    }

    public void submitButtonClicked(View view)
    {
        final String locationvalue=location.getSelectedItem().toString();
        final String descvalue=desc.getText().toString();

        if(!TextUtils.isEmpty(locationvalue) && !TextUtils.isEmpty(descvalue))
        {
            StorageReference filePath=storageReference.child("PostImage").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PostActivity.this,"IMAGE UPLOAD COMPLETE",Toast.LENGTH_LONG).show();
                    final Uri downloadurl=taskSnapshot.getDownloadUrl();
                    final DatabaseReference newpost=databaseReference.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Toast.makeText(PostActivity.this,"UPLOAD COMPLETE",Toast.LENGTH_LONG).show();
                            newpost.child("title").setValue(locationvalue);
                            newpost.child("desc").setValue(descvalue);
                            newpost.child("image").setValue(downloadurl.toString());

                            //newpost.child("profilepic").setValue(dataSnapshot.child("image").getValue().toString());
                            //newpost.child("username").setValue(dataSnapshot.child("name").getValue())
                            newpost.child("uid").setValue(mCurrentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent mainActivityIntent=new Intent(PostActivity.this,MainActivity.class);
                                        startActivity(mainActivityIntent);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });
        }
    }
}
