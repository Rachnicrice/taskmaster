package com.rachnicrice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.rachnicrice.taskmaster.Room.TaskDao;
import com.rachnicrice.taskmaster.Room.TaskDatabase;

import javax.annotation.Nonnull;

import type.CreateTaskInput;

public class AddTask extends AppCompatActivity {

    private AWSAppSyncClient mAWSAppSyncClient;
    private static String TAG = "rnr.catch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        View submit = findViewById(R.id.submit);

        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();

        submit.setOnClickListener((v) -> {
            final TextView submitted = findViewById(R.id.submittedMsg);
            submitted.setVisibility(View.VISIBLE);

            EditText taskTitle = findViewById(R.id.editText2);
            EditText taskDetails = findViewById(R.id.editText);

            String title = taskTitle.getText().toString();
            String details = taskDetails.getText().toString();

            CreateTaskInput input = CreateTaskInput.builder()
                    .title(title)
                    .details(details)
                    .state("new")
                    .build();
            mAWSAppSyncClient.mutate(CreateTaskMutation.builder().input(input).build()).enqueue(
                    new GraphQLCall.Callback<CreateTaskMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
                            Log.i(TAG, response.data().toString());
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            Log.w(TAG, "failure");
                        }
                    });
        });
    }
}
