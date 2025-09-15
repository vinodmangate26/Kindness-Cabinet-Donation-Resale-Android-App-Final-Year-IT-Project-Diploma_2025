package com.gauravpatil.kindnesscabinet.PaymentGateway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.HomeActivity;
import com.gauravpatil.kindnesscabinet.MoreDetailsAllDonateandSellerInformationActivity;
import com.gauravpatil.kindnesscabinet.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Thank_YouActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    String id, name, mobile_no, product_cat, product_image, product_name, paid_status, productrating, quantity,
            descrition, pickup_location, pickup_option, role;

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);


        preferences = PreferenceManager.getDefaultSharedPreferences(Thank_YouActivity.this);
        editor= preferences.edit();

        progressDialog = new ProgressDialog(Thank_YouActivity.this);
        progressDialog.setTitle("Product Request Sending");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        btnSelfPickup();
    }

    private void btnSelfPickup() {
        saveRequestThroughPostOffice();
    }

    private void saveRequestThroughPostOffice() {
        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("product_id",preferences.getString("product_id",""));
        params.put("product_category",preferences.getString("product_category",""));
        params.put("product_image",preferences.getString("product_image",""));
        params.put("product_name",preferences.getString("product_name",""));
        params.put("product_paid_status",preferences.getString("product_paid_status",""));
        params.put("product_rating",preferences.getString("product_rating",""));
        params.put("product_qunatity",preferences.getString("product_qunatity",""));
        params.put("product_description",preferences.getString("product_description",""));
        params.put("product_location",preferences.getString("product_location",""));
        params.put("product_pickup_option",preferences.getString("product_pickup_option",""));
        params.put("doner_seller_name",preferences.getString("doner_seller_name",""));
        params.put("doner_seller_mobile_no",preferences.getString("doner_seller_mobile_no",""));
        params.put("type",preferences.getString("type",""));
        params.put("pick_up_type",preferences.getString("pick_up_type",""));

        client.post(Urls.addRequestThroughPostOffice,params,
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
                                progressDialog.dismiss();
                                Toast.makeText(Thank_YouActivity.this, "Payment Done Successfully Done", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Thank_YouActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Thank_YouActivity.this, "Fail", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Thank_YouActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }
}