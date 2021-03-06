package com.rachnicrice.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.CreateTeamMutation;
import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignOutOptions;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import type.CreateTeamInput;


public class MainActivity extends AppCompatActivity implements MyTaskRecyclerViewAdapter.OnTaskClickedListener {

    private static final String TAG = "Rachael";
    private AWSAppSyncClient mAWSAppSyncClient;

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

        set.setOnClickListener((v) -> {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
        });

        View logout = findViewById(R.id.logout);
        logout.setOnClickListener((v) -> {
            String username = AWSMobileClient.getInstance().getUsername();
            AWSMobileClient.getInstance().signOut();

            AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                        @Override
                        public void onResult(UserStateDetails userStateDetails) {
                            Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                            if (userStateDetails.getUserState().equals(UserState.SIGNED_OUT)) {
                                AWSMobileClient.getInstance().showSignIn(MainActivity.this, new Callback<UserStateDetails>() {

                                    @Override
                                    public void onResult(UserStateDetails result) {
                                        Log.d(TAG, "onResult: " + result.getUserState());

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e(TAG, "onError: ", e);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("INIT", "Initialization error.", e);
                        }
                    }
            );

        });

        //TODO Put this code into a method
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                        if (userStateDetails.getUserState().equals(UserState.SIGNED_OUT)) {
                            AWSMobileClient.getInstance().showSignIn(MainActivity.this, new Callback<UserStateDetails>() {

                                @Override
                                public void onResult(UserStateDetails result) {
                                    Log.d(TAG, "onResult: " + result.getUserState());

                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e(TAG, "onError: ", e);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "We are in onResume yay!");

        //Check to see if there is a user saved in Shared Preferences
//        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
//        String name = p.getString("user", "def");
//        Log.i(TAG, name);
        TextView user = findViewById(R.id.userTasks);

//        if (!name.equals("def")) {
//            String text = name + "'s Tasks";
//            user.setText(text);
//        }

        String username = AWSMobileClient.getInstance().getUsername();
        user.setText(username + "'s Tasks");
    }

    @Override
    public void taskClicked(ListTasksQuery.Item t) {
        Intent i = new Intent(this, TaskDetail.class);
        i.putExtra("title", t.title());
        i.putExtra("details", t.details());
        i.putExtra("state", t.state());
        startActivity(i);

    }
}
