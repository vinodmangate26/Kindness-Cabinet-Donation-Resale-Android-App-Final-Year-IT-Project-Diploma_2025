package com.gauravpatil.kindnesscabinet.Admin.History;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.gauravpatil.kindnesscabinet.Admin.AllUser.AdapterGetAllUserDetails;
import com.gauravpatil.kindnesscabinet.Admin.AllUser.AllUserActivity;
import com.gauravpatil.kindnesscabinet.Admin.AllUser.POJOGetAllUserDetails;
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

public class HistoryActivity extends AppCompatActivity {

    ListView lvHistory;
    TextView tvNoHistoryAvailable;
    List<POJOAdminGetAllHistory> pojoAdminGetAllHistories;
    AdapterAdminGetAllHistory adapterAdminGetAllHistory;

    SearchView searchHistory;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        pojoAdminGetAllHistories = new ArrayList<>();
        searchHistory = findViewById(R.id.svAdminAllHistory);
        lvHistory = findViewById(R.id.lvHistroy);
        tvNoHistoryAvailable = findViewById(R.id.tvNoHistoryAvailable);
        progressDialog = new ProgressDialog(HistoryActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("History Loading in Progress...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();


        searchHistory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHistory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchHistory(query);
                return false;
            }
        });


        getAllHistory();
    }

    private void searchHistory(String query) {
        List<POJOAdminGetAllHistory> tempCategory = new ArrayList<>();

        for (POJOAdminGetAllHistory obj : pojoAdminGetAllHistories) {
            if (obj.getName().toUpperCase().contains(query.toUpperCase())
                    ||obj.getProduct_name().toUpperCase().contains(query.toUpperCase())
                    ||obj.getAddress().toUpperCase().contains(query.toUpperCase())) {
                tempCategory.add(obj);
            }
        }

        // Show or hide UI elements based on search result
        if (tempCategory.isEmpty()) {
            lvHistory.setVisibility(View.GONE);
            tvNoHistoryAvailable.setVisibility(View.VISIBLE);
        } else {
            lvHistory.setVisibility(View.VISIBLE);
            tvNoHistoryAvailable.setVisibility(View.GONE);
        }

        adapterAdminGetAllHistory = new AdapterAdminGetAllHistory(tempCategory, HistoryActivity.this);
        lvHistory.setAdapter(adapterAdminGetAllHistory);
    }

    private void getAllHistory() {

        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        client.post(Urls.getAllHistory,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                try
                {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("getAllHistory");

                    if(jsonArray.isNull(0))
                    {
                        lvHistory.setVisibility(View.GONE);
                        tvNoHistoryAvailable.setVisibility(View.VISIBLE);
                    }

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String mobile_no = jsonObject.getString("mobileno");
                        String email_id = jsonObject.getString("email_id");
                        String address = jsonObject.getString("address");
                        String productCategory = jsonObject.getString("product_category");
                        String productImage = jsonObject.getString("product_image");
                        String productName = jsonObject.getString("product_name");
                        String productCost = jsonObject.getString("product_cost");
                        String productRating = jsonObject.getString("product_rating");
                        String productQuantity = jsonObject.getString("product_quantity");
                        String productDescription = jsonObject.getString("product_description");
                        String productLocation = jsonObject.getString("product_location");
                        String productPickupOption = jsonObject.getString("product_pickup_option");
                        String productDateAndTime = jsonObject.getString("product_date_and_time");
                        String role = jsonObject.getString("role");
                        String username = jsonObject.getString("username");


                        pojoAdminGetAllHistories.add(new POJOAdminGetAllHistory(id,name,mobile_no,email_id,
                                address,username,productCategory,
                                productImage, productName,productCost,productRating,
                                productQuantity,productDescription,productLocation,
                                productPickupOption,productDateAndTime,role));

                    }

                    adapterAdminGetAllHistory = new AdapterAdminGetAllHistory(pojoAdminGetAllHistories,HistoryActivity.this);

                    lvHistory.setAdapter(adapterAdminGetAllHistory);
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
                Toast.makeText(HistoryActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
}