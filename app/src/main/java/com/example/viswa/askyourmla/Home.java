package com.example.viswa.askyourmla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void pHomeRegisterButtonClicked(View view)
    {
        Intent re=new Intent(Home.this,RegisterActivity.class);
        startActivity(re);
    }
    public void pHomeLoginButtonClicked(View view)
    {
        Intent lo=new Intent(Home.this,LoginActivity.class);
        startActivity(lo);
    }

    public void mHomeRegisterButtonClicked(View view)
    {
        Intent re=new Intent(Home.this,MLAregister.class);
        startActivity(re);
    }
    public void mHomeLoginButtonClicked(View view)
    {
        Intent lo=new Intent(Home.this,MLAlogin.class);
        startActivity(lo);
    }
}
