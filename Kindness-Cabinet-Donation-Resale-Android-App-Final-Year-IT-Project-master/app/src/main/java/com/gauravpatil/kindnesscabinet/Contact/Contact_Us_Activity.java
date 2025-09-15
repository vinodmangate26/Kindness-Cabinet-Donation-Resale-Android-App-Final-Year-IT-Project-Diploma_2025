package com.gauravpatil.kindnesscabinet.Contact;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.HomeActivity;
import com.gauravpatil.kindnesscabinet.LoginActivity;
import com.gauravpatil.kindnesscabinet.R;
import com.gauravpatil.kindnesscabinet.VerifyOTPActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Contact_Us_Activity extends AppCompatActivity
{
    EditText etContactUsFeedback;
    Button btnCommunitiesSend;
    ImageView ivCall,ivEmail;

    ProgressDialog progressDialog;

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences

    private static final String PHONE_NUMBER = "7875335539";
    private static final String EMAIL_ADDRESS = "gp949958@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        setTitle("Contact Us Activity");

        preferences = PreferenceManager.getDefaultSharedPreferences(Contact_Us_Activity.this);
        editor= preferences.edit();

        etContactUsFeedback = findViewById(R.id.etContactUsFeedback);
        btnCommunitiesSend = findViewById(R.id.btnCommunitiesSend);
        ivCall = findViewById(R.id.ivContactUsCall);
        ivEmail = findViewById(R.id.ivContactUsEmail);

        ivCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makePhoneCall();
            }
        });

        ivEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendEmail();
            }
        });


        btnCommunitiesSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etContactUsFeedback.getText().toString().isEmpty())
                {
                    etContactUsFeedback.setError("Please Enter Message");
                }
                else
                {
                    progressDialog = new ProgressDialog(Contact_Us_Activity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Message Sending in Progress...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    addContactUsMessage();
                }
            }
        });
    }

    private void addContactUsMessage() {
        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("username",preferences.getString("username",""));
        params.put("message",etContactUsFeedback.getText().toString());

        client.post(Urls.addContactUsMessage,params,
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
                                Toast.makeText(Contact_Us_Activity.this, "Message Send Successfully Done", Toast.LENGTH_LONG).show();
                                AlertDialog.Builder ad = new AlertDialog.Builder(Contact_Us_Activity.this);
                                ad.setTitle("Kindness Cabinate App");
                                ad.setMessage("Message Send Successfully");
                                ad.setPositiveButton("Thank You", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Contact_Us_Activity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                AlertDialog alertDialog = ad.create();
                                alertDialog.setCanceledOnTouchOutside(true);
                                alertDialog.show();
                            }
                            else
                            {
                                Toast.makeText(Contact_Us_Activity.this, "Message Send Fail", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Contact_Us_Activity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void makePhoneCall()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
        startActivity(intent);
    }

    private void sendEmail()
    {
        // Create intent for sending email using ACTION_SENDTO
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + EMAIL_ADDRESS));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");

        // Check if there's any email app that can handle this intent
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        } else
        {
            // If no email app can handle ACTION_SENDTO, fallback to ACTION_SEND
            Intent intentOther = new Intent(Intent.ACTION_SEND);
            intentOther.setType("message/rfc822");
            intentOther.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADDRESS});
            intentOther.putExtra(Intent.EXTRA_SUBJECT, "Feedback: ");

            // Check if there's any app that can handle this intent
            if (intentOther.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intentOther);
            }
            else
            {
                // If no email app is found, show a toast message
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        }
    }


}