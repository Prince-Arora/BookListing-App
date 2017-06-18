package com.example.laptop.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.laptop.booklistingapp.R.id.search;


public class MainActivity extends AppCompatActivity {
    public String GetText;
    EditText medit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medit = (EditText) findViewById(search);
    }

    public void Search(View view) {
        GetText = medit.getText().toString();
        Intent searchResultIntent = new Intent(MainActivity.this, HttpActivity.class);
        searchResultIntent.putExtra("toSearch", GetText);
        startActivity(searchResultIntent);

    }
}
