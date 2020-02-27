package com.rachnicrice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.amplify.generated.graphql.CreateTeamMutation;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.annotation.Nonnull;

import type.CreateTaskInput;
import type.CreateTeamInput;

public class AddTask extends AppCompatActivity {

    private AWSAppSyncClient mAWSAppSyncClient;
    private static String TAG = "rnr.catch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        View submit = findViewById(R.id.submit);
        View addImage = findViewById(R.id.addImage);

        addImage.setOnClickListener((v) -> {

//            private void uploadFile() {
//                File sampleFile = new File(getApplicationContext().getFilesDir(), "sample.txt");
//                try {
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(sampleFile));
//                    writer.append("Howdy World!");
//                    writer.close();
//                }
//                catch(Exception e) {
//                    Log.e("StorageQuickstart", e.getMessage());
//                }
//
//                Amplify.Storage.uploadFile(
//                        "myUploadedFileName.txt",
//                        sampleFile.getAbsolutePath(),
//                        new ResultListener<StorageUploadFileResult>() {
//                            @Override
//                            public void onResult(StorageUploadFileResult result) {
//                                Log.i("StorageQuickStart", "Successfully uploaded: " + result.getKey());
//                            }
//
//                            @Override
//                            public void onError(Throwable error) {
//                                Log.e("StorageQuickstart", "Upload error.", error);
//                            }
//                        }
//                );
//            }

        });


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

            RadioGroup group = (RadioGroup) findViewById(R.id.addGroup);
            int radioId = group.getCheckedRadioButtonId();
            RadioButton selected = group.findViewById(radioId);
            String team = selected.getText().toString();


            CreateTaskInput input = CreateTaskInput.builder()
                    .title(title)
                    .details(details)
                    .state("new")
                    .taskTeamId(team)
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
