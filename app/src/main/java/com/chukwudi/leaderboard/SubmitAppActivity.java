package com.chukwudi.leaderboard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.chukwudi.leaderboard.services.ServiceBuilder;
import com.chukwudi.leaderboard.services.SubmitService;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitAppActivity extends AppCompatActivity {

    private Dialog mQuestionDialog;
    private TextInputEditText mFirstName;
    private TextInputEditText mLastName;
    private TextInputEditText mEmail;
    private TextInputEditText mGithubLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_app);

        Button submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showQuestionDialog();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFirstName = findViewById(R.id.first_name_value);
        mLastName = findViewById(R.id.last_name_value);
        mEmail = findViewById(R.id.email_value);
        mGithubLink = findViewById(R.id.github_link_value);

    }

    private void showQuestionDialog() {
        mQuestionDialog = new Dialog(SubmitAppActivity.this);
        mQuestionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mQuestionDialog.setContentView(R.layout.are_you_sure_dialog);
        mQuestionDialog.show();

        Button yesButton = mQuestionDialog.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstNameValue = mFirstName.getText().toString();
                String lastNameValue = mLastName.getText().toString();
                String emailValue = mEmail.getText().toString();
                String githubLinkValue = mGithubLink.getText().toString();
                if (firstNameValue.equals("") || lastNameValue.equals("") || emailValue.equals("") || githubLinkValue.equals("")) {
                    mQuestionDialog.dismiss();
                    failedProcess();
                } else {
                    mQuestionDialog.dismiss();
                    sendOverNetwork(firstNameValue, lastNameValue, emailValue, githubLinkValue);
                }
            }
        });
        Button closeButton = mQuestionDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuestionDialog.dismiss();
            }
        });
    }


    private void successProcess() {
        mQuestionDialog = new Dialog(SubmitAppActivity.this);
        mQuestionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mQuestionDialog.setContentView(R.layout.transaction_successful);
        mQuestionDialog.show();
    }


    private void failedProcess() {
        mQuestionDialog = new Dialog(SubmitAppActivity.this);
        mQuestionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mQuestionDialog.setContentView(R.layout.transaction_failed);
        mQuestionDialog.show();
    }

    private void sendOverNetwork(String firstNameValue, String lastNameValue, String emailValue, String githubLinkValue) {
        SubmitService submitService = ServiceBuilder.buildService(SubmitService.class);
        Call<Void> request = submitService.submitDetails(emailValue, firstNameValue, lastNameValue, githubLinkValue);

        request.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> request, Response<Void> response) {
                successProcess();
            }

            @Override
            public void onFailure(Call<Void> request, Throwable t) {
                failedProcess();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}