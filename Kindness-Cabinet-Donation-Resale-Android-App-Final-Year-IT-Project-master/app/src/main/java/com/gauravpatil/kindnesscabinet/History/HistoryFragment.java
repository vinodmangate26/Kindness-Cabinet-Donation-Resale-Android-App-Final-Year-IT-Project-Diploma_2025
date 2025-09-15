package com.gauravpatil.kindnesscabinet.History;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.HomeActivity;
import com.gauravpatil.kindnesscabinet.LoginActivity;
import com.gauravpatil.kindnesscabinet.R;
import com.gauravpatil.kindnesscabinet.java_classes.AdapterGetAllDonateandSellerInformation;
import com.gauravpatil.kindnesscabinet.java_classes.POJOGetAllDonateandSellerInformation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HistoryFragment extends Fragment {
    ListView lvHistory;
    TextView tvNoHistoryAvailable;
    List<POJOGetAllHistory> pojoHistory;
    AdapterGetAllHistory adapterHistory;

    ProgressDialog progressDialog;


    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_history, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor= preferences.edit();

        pojoHistory = new ArrayList<>();
        lvHistory = view.findViewById(R.id.lvHistroy);
        tvNoHistoryAvailable = view.findViewById(R.id.tvNoHistoryAvailable);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("History Loading in Progress...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getAllHistory();

        return view;

    }

    private void getAllHistory() {

        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("username",preferences.getString("username",""));
        client.post(Urls.getHistory,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                try
                {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("getHistory");

                    if(jsonArray.isNull(0))
                    {
                        lvHistory.setVisibility(View.GONE);
                        tvNoHistoryAvailable.setVisibility(View.VISIBLE);
                    }

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String username = jsonObject.getString("username");
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


                        pojoHistory.add(new POJOGetAllHistory(id,username,productCategory,
                                productImage, productName,productCost,productRating,
                                productQuantity,productDescription,productLocation,
                                productPickupOption,productDateAndTime,role));

                    }

                    adapterHistory = new AdapterGetAllHistory(pojoHistory,getActivity());

                    lvHistory.setAdapter(adapterHistory);
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
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
}