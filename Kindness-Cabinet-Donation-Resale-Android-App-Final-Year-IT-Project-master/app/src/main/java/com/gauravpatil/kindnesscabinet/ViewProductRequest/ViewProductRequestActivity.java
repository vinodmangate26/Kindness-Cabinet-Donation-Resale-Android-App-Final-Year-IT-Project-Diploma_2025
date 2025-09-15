package com.gauravpatil.kindnesscabinet.ViewProductRequest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.History.AdapterGetAllHistory;
import com.gauravpatil.kindnesscabinet.History.POJOGetAllHistory;
import com.gauravpatil.kindnesscabinet.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewProductRequestActivity extends AppCompatActivity {

    ListView lvViewRequestProduct;
    TextView tvNoCategoryAvailable;
    List<POJOGetAllHistory> pojoViewAllProductRequest;
    AdapterGetRequest adapterGetRequest;

    ProgressDialog progressDialog;


    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_request);

        preferences = PreferenceManager.getDefaultSharedPreferences(ViewProductRequestActivity.this);
        editor= preferences.edit();

        setTitle("View All Request of Product");

        pojoViewAllProductRequest = new ArrayList<>();
        lvViewRequestProduct = findViewById(R.id.lvRequestProduct);
        tvNoCategoryAvailable = findViewById(R.id.tvNoRequestProductAvailable);
        progressDialog = new ProgressDialog(ViewProductRequestActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Product Loading in Progress...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getAllRequestProduct();

    }

    private void getAllRequestProduct() {
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("mobile_no",preferences.getString("mobile_no",""));
        client.post(Urls.getRequestProduct,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                try
                {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("getAllFavouriteProduct");

                    if(jsonArray.isNull(0))
                    {
                        lvViewRequestProduct.setVisibility(View.GONE);
                        tvNoCategoryAvailable.setVisibility(View.VISIBLE);
                    }

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String productId = jsonObject.getString("product_id");
                        String productCategory = jsonObject.getString("product_category");
                        String productImage = jsonObject.getString("product_image");
                        String productName = jsonObject.getString("product_name");
                        String productCost = jsonObject.getString("product_paid_status");
                        String productRating = jsonObject.getString("product_rating");
                        String productQuantity = jsonObject.getString("product_quantity");
                        String productDescription = jsonObject.getString("product_description");
                        String productLocation = jsonObject.getString("product_location");
                        String productPickupOption = jsonObject.getString("product_pickup_option");
                        String productDateAndTime = jsonObject.getString("product_date_and_time");
                        String role = jsonObject.getString("role");

                        pojoViewAllProductRequest.add(new POJOGetAllHistory(id,productId,productCategory,
                                productImage, productName,productCost,productRating,
                                productQuantity,productDescription,productLocation,
                                productPickupOption,productDateAndTime,role));

                    }

                    adapterGetRequest = new AdapterGetRequest(pojoViewAllProductRequest,ViewProductRequestActivity.this);

                    lvViewRequestProduct.setAdapter(adapterGetRequest);
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
                Toast.makeText(ViewProductRequestActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }
}