package com.example.viswa.askyourmla;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mlist_item;
    private DatabaseReference mDatabase;
    private  FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mlist_item=(RecyclerView)findViewById(R.id.list_item);
        mlist_item.setHasFixedSize(true);
        mlist_item.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("MLAapp");

        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser()==null)
                {
                    Intent loginIntent=new Intent(MainActivity.this,Home.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter <Insta,InstaViewHolder> FBRA=(new FirebaseRecyclerAdapter<Insta, InstaViewHolder>(
                Insta.class,
                R.layout.card_row,
                InstaViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(InstaViewHolder viewHolder, Insta model, int position) {

                final String postkey=getRef(position).getKey().toString();

                viewHolder.getTitle(model.getTitle());
                viewHolder.getDesc(model.getDesc());
                viewHolder.getImage(getApplicationContext(),model.getImage());
                viewHolder.getUid(model.getUid());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent messageIntent=new Intent(MainActivity.this,Messages.class);
                        messageIntent.putExtra("postid",postkey);
                        startActivity(messageIntent);
                    }
                });
            }
        });
        mlist_item.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class InstaViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        public InstaViewHolder(View itemView)
        {
            super(itemView);
            view=itemView;
            Button delbut=(Button) view.findViewById(R.id.deletepost);
            delbut.setVisibility(View.INVISIBLE);
        }
        public void getTitle(String title)
        {
            TextView post_title=(TextView)view.findViewById(R.id.textTitle);
            post_title.setText(title);
        }
        public void getDesc(String desc)
        {
            TextView post_desc=(TextView)view.findViewById(R.id.textDescription);
            post_desc.setText(desc);
        }
        public void getImage(Context ctx,String image)
        {
            ImageView post_image=(ImageView) view.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void getUid(String uid)
        {
            FirebaseAuth xAuth=FirebaseAuth.getInstance();
            if(xAuth.getCurrentUser().getUid().toString().equals(uid))
            {
                Button delbut=(Button) view.findViewById(R.id.deletepost);
                delbut.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.addIcon)
        {
            Intent intent=new Intent(MainActivity.this,PostActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.logout)
        {
            mAuth.signOut();
            Intent log=new Intent(MainActivity.this,Home.class);
            startActivity(log);
        }

        return super.onOptionsItemSelected(item);
    }
}
