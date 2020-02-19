package com.rachnicrice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rachnicrice.taskmaster.Room.TaskDao;
import com.rachnicrice.taskmaster.Room.TaskDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MyTaskRecyclerViewAdapter.OnTaskClickedListener {

    private static final String TAG = "Rachael";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab the buttons
        View b1 = findViewById(R.id.button1);
        View b2 = findViewById(R.id.button2);

        View set = findViewById(R.id.settings);

        //Set up the event listeners
        b1.setOnClickListener((v) -> {
            Intent i = new Intent(this, AddTask.class);
            startActivity(i);
        });

        b2.setOnClickListener((v) -> {
            Intent i = new Intent(this, AllTasks.class);
            startActivity(i);
        });

        set.setOnClickListener( (v) -> {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "We are in onResume yay!");

        //Check to see if there is a user saved in Shared Preferences
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        String name = p.getString("user", "def");
        Log.i(TAG, name);
        TextView user = findViewById(R.id.userTasks);

        if (!name.equals("def")) {
            String text = name + "'s Tasks";
            user.setText(text);
        }
    }

    @Override
    public void taskClicked(Task t) {
        Intent i = new Intent(this, TaskDetail.class);
        i.putExtra("title", t.title);
        i.putExtra("details", t.details);
        i.putExtra("state", t.state);
        startActivity(i);

    }
}
