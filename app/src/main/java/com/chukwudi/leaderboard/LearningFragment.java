package com.chukwudi.leaderboard;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class LearningFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingProgress;
    private TextView mErrorMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_learning, container, false);
        mRecyclerView = inflate.findViewById(R.id.recycler_learners);
        mLoadingProgress = inflate.findViewById(R.id.progress_loading);
        mErrorMessage = inflate.findViewById(R.id.text_error_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        try {
            URL topLearnerUrl = ApiUtil.buildUrl(ApiUtil.LEARNING_API_URL);
            new LearnersQueryTask().execute(topLearnerUrl);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        return inflate;
    }

    @SuppressLint("StaticFieldLeak")
    public class LearnersQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(searchUrl);
            } catch (IOException e) {
                Log.d("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.GONE);
            if (result == null) {
                mRecyclerView.setVisibility(View.GONE);
                mErrorMessage.setVisibility(View.VISIBLE);
            } else {
                ArrayList<Learner> learners = ApiUtil.getLearnersFromJson(result);
                LearnersAdapter adapter = new LearnersAdapter(learners);
                mRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
