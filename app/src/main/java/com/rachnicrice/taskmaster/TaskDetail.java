package com.rachnicrice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        String taskTitle = getIntent().getStringExtra("title");
        TextView pageTitle = findViewById(R.id.taskName);

        pageTitle.setText(taskTitle);
    }
}
