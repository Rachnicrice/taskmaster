package com.rachnicrice.taskmaster;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.amplify.generated.graphql.ListTeamsQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;


import javax.annotation.Nonnull;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class TaskFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private MyTaskRecyclerViewAdapter.OnTaskClickedListener mListener;
    private RecyclerView recyclerView;
    private MyTaskRecyclerViewAdapter adaptor;
    private AWSAppSyncClient mAWSAppSyncClient;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskFragment() {
    }

    public static TaskFragment newInstance(int columnCount) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mAWSAppSyncClient = AWSAppSyncClient.builder()
                    .context(view.getContext().getApplicationContext())
                    .awsConfiguration(new AWSConfiguration(view.getContext().getApplicationContext()))
                    .build();

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mAWSAppSyncClient.query(ListTeamsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.NETWORK_FIRST)
                .enqueue(new GraphQLCall.Callback<ListTeamsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ListTeamsQuery.Data> response) {
                        Log.i("rnr.team", response.toString());
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });

        mAWSAppSyncClient.query(ListTasksQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.NETWORK_FIRST)
                .enqueue(new GraphQLCall.Callback<ListTasksQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ListTasksQuery.Data> response) {

                        Handler h = new Handler(Looper.getMainLooper()){
                            @Override
                            public void handleMessage(Message inputMessage) {
                                if (adaptor == null) {
                                    adaptor = new MyTaskRecyclerViewAdapter(response.data().listTasks().items(), mListener);
                                    recyclerView.setAdapter(adaptor);
                                }
                                adaptor.setTaskList(response.data().listTasks().items());
                                adaptor.notifyDataSetChanged();
                            }
                        };
                        h.obtainMessage().sendToTarget();
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("mnf", "attaching");
        if (context instanceof MyTaskRecyclerViewAdapter.OnTaskClickedListener) {
            Log.i("mnf", "saving");
            mListener = (MyTaskRecyclerViewAdapter.OnTaskClickedListener) context;
        } else {
            Log.w("mnf", "it's not in an OnTaskClickedListener!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
