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
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
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
            uploadWithTransferUtility();

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

    public void uploadWithTransferUtility(){

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();

        File file = new File(getApplicationContext().getFilesDir(), "sample.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append("Howdy World!");
            writer.close();
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "public/sample.txt",
                        new File(getApplicationContext().getFilesDir(),"sample.txt"));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d(TAG, "Bytes Transferred: " + uploadObserver.getBytesTransferred());
        Log.d(TAG, "Bytes Total: " + uploadObserver.getBytesTotal());
    }
}
