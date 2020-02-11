package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab the buttons
        View b1 = findViewById(R.id.button1);
        View b2 = findViewById(R.id.button2);

        //Set up the event listeners
        b1.setOnClickListener((v) -> {
            Intent in = new Intent(this, AddTask.class);
            startActivity(in);
        });

        b2.setOnClickListener((v) -> {
            Intent i = new Intent(this, AllTasks.class);
            startActivity(i);
        });
    }
}
