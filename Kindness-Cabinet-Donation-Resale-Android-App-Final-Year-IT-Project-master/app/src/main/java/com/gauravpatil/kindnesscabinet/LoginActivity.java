package com.gauravpatil.kindnesscabinet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.gauravpatil.kindnesscabinet.Admin.AdminLoginActivity;
import com.gauravpatil.kindnesscabinet.java_classes.NetworkChangeListener;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity
{

    private Handler handler = new Handler();
    private boolean isPressed = false;
    private Runnable runnable;
    ImageView ivAdmin;
    EditText etUsername,etPassword;
    AppCompatButton btnLogin,btnGogleSignIn;
    TextView tvSignUp,tvForgetPassword;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    GoogleSignInOptions googleSignInOptions; //Show option of gmail
    GoogleSignInClient googleSignInClient;  //Selected gmail option store

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences

//  GoogleSignInOption => GoogleSignIn => GoogleSignInClient => GoogleSignIn => GoogleSignInAccount (name,email,photo)000

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivAdmin = findViewById(R.id.ivLoginAdmin);
        etUsername = findViewById(R.id.etLoginusername);
        etPassword = findViewById(R.id.etLoginPassword);
        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);
        btnLogin = findViewById(R.id.btnLoginLogin);
        tvSignUp = findViewById(R.id.tvLoginNewUser);
        btnGogleSignIn = findViewById(R.id.btnLoginGoogleWithSignUp);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor= preferences.edit();

        if(preferences.getBoolean("islogin",false))
        {
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

        ivAdmin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPressed = true;
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (isPressed) {
                                    // Open new activity after 3 seconds
                                    Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        };
                        handler.postDelayed(runnable, 3000); // 3 seconds delay
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isPressed = false;
                        handler.removeCallbacks(runnable); // Cancel if released early
                        break;
                }
                return true;
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, Confirm_Register_MobileNo_Activity.class);
                startActivity(intent);
            }
        });

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
                else if (!etUsername.getText().toString().matches(".*[@#$%&!].*"))
                {
                    etUsername.setError("Please used at least 1 Special Symbol");
                }
                else if (!etPassword.getText().toString().matches(".*[@#$%&!].*"))
                {
                    etPassword.setError("Please used at least 1 Special Symbol.");
                }
                else
                {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Login in Progress...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    userlogin();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnGogleSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });
    }

    private void signIn()
    {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999)
        {
            Task <GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                task.getResult(ApiException.class);
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
            catch (ApiException e)
            {
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }
    private void userlogin()
    {
        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams();

        params.put("username",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post(Urls.userLogin,params,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
                        super.onSuccess(statusCode, headers, response);
                        try
                        {
                            String status = response.getString("Success");
                            String name = response.getString("name");
                            String mobileNo = response.getString("mobile_no");
                            if(status.equals("1"))
                            {
                                editor.putBoolean("islogin",true).commit();
                                editor.putString("name",name).commit();
                                editor.putString("mobile_no", mobileNo).commit();
                                Toast.makeText(LoginActivity.this, "Login Successfully Done", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                editor.putString("username",etUsername.getText().toString()).commit();
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }
}