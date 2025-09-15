package com.gauravpatil.kindnesscabinet.Feedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.HomeActivity;
import com.gauravpatil.kindnesscabinet.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Feedback_Activity extends AppCompatActivity {

    EditText et_feedback_title,et_feedback_description;
    Button btnsubmitfeedback;
    ProgressDialog progressDialog;

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        preferences = PreferenceManager.getDefaultSharedPreferences(Feedback_Activity.this);
        editor= preferences.edit();

        username = preferences.getString("username","");

        et_feedback_title = findViewById(R.id.et_feedback_title);
        et_feedback_description = findViewById(R.id.edt_feedback_description);
        btnsubmitfeedback = findViewById(R.id.btn_submit_feedback);

        btnsubmitfeedback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (et_feedback_title.getText().toString().isEmpty())
                {
                    et_feedback_title.setError("Please Enter Your Feedback Title");
                }
                else if (et_feedback_description.getText().toString().isEmpty())
                {
                    et_feedback_description.setError("Please Enter Your Feedback Description");
                }
                else
                {
                    progressDialog = new ProgressDialog(Feedback_Activity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Feedback in Progress...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    addFeedback();
                }
            }
        });

    }

    private void addFeedback() {
        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("username",username);
        params.put("title",et_feedback_title.getText().toString());
        params.put("description",et_feedback_description.getText().toString());


        client.post(Urls.addFeedback,params,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
                        super.onSuccess(statusCode, headers, response);
                        try
                        {
                            String status = response.getString("success");
                            if(status.equals("1"))
                            {
                                Toast.makeText(Feedback_Activity.this, "Feedback Added Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Feedback_Activity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(Feedback_Activity.this, "Registration Fail", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }
                        catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
                    {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(Feedback_Activity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }
}