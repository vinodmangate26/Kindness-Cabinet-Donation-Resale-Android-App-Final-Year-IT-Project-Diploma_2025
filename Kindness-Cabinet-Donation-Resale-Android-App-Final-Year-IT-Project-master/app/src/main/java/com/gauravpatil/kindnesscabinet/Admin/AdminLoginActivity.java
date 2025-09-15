package com.gauravpatil.kindnesscabinet.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class AdminLoginActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    AppCompatButton btnLogin;

    ProgressDialog progressDialog;

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(AdminLoginActivity.this);
        editor= preferences.edit();

        if(preferences.getBoolean("isAdminLogin",false))
        {
            startActivity(new Intent(AdminLoginActivity.this, HomeAdminActivity.class));
            finish();
        }

        etUsername = findViewById(R.id.etLoginusername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLoginLogin);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etUsername.getText().toString().isEmpty())
                {
                    etUsername.setError("Please Enter Your Username");
                }
                else if (etPassword.getText().toString().isEmpty())
                {
                    etPassword.setError("Please Enter Your Password");
                }
                else if (etUsername.getText().toString().contains(" "))
                {
                    etUsername.setError("Username should not contain spaces");
                }
                else if (etPassword.getText().toString().contains(" "))
                {
                    etPassword.setError("Password should not contain spaces");
                }
                else if (etUsername.getText().toString().length() < 8)
                {
                    etUsername.setError("Username must be greater than 8 Character");
                }
                else if (etPassword.getText().toString().length() < 8)
                {
                    etPassword.setError("Password must be greater than 8 Character");
                }
                else
                {
                    progressDialog = new ProgressDialog(AdminLoginActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Login in Progress...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    adminlogin();
                }
            }
        });

    }


    private void adminlogin()
    {
        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams();

        params.put("username",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post(Urls.adminLogin,params,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
                        super.onSuccess(statusCode, headers, response);
                        try
                        {
                            String status = response.getString("Success");
                            if(status.equals("1"))
                            {

                                editor.putBoolean("isAdminLogin",true).commit();
                                Toast.makeText(AdminLoginActivity.this, "Admin Login Successfully Done", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AdminLoginActivity.this,HomeAdminActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(AdminLoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(AdminLoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }

}