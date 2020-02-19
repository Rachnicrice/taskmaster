package com.rachnicrice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rachnicrice.taskmaster.Room.TaskDao;
import com.rachnicrice.taskmaster.Room.TaskDatabase;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        View submit = findViewById(R.id.submit);

        submit.setOnClickListener( (v) -> {
            final TextView submitted = findViewById(R.id.submittedMsg);
            submitted.setVisibility(View.VISIBLE);

            TaskDatabase db = Room.databaseBuilder(getApplicationContext(),
                    TaskDatabase.class, "task")
                    .allowMainThreadQueries()
                    .build();
            TaskDao dao = db.taskDao();

            EditText titleView = findViewById(R.id.editText2);
            Editable title = titleView.getText();

            EditText detailView = findViewById(R.id.editText);
            Editable details = detailView.getText();

            dao.addTask(new Task(title.toString(), details.toString()));
        });
    }
}
