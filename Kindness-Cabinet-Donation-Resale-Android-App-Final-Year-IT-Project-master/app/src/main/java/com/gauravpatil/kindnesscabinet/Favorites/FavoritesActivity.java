package com.gauravpatil.kindnesscabinet.Favorites;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

import java.nio.file.attribute.FileAttributeView;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FavoritesActivity extends AppCompatActivity {

    ListView lvFavorite;
    TextView tvNoFavoriteAvailable;
    List<POJOGetAllFavourite> pojoFavourite;
    AdapterGetAllFavourite adapterFavourite;

    ProgressDialog progressDialog;

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        preferences = PreferenceManager.getDefaultSharedPreferences(FavoritesActivity.this);
        editor= preferences.edit();

        pojoFavourite = new ArrayList<>();
        lvFavorite = findViewById(R.id.lvFavorite);
        tvNoFavoriteAvailable = findViewById(R.id.tvNoFavoriteAvailable);
        progressDialog = new ProgressDialog(FavoritesActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("favourite Product Loading in Progress...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        setTitle("Your Favourite");

        getAllFavourite();

    }

    private void getAllFavourite() {
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("username",preferences.getString("username",""));
        client.post(Urls.getFavourite,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                try
                {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("getFavourite");

                    if(jsonArray.isNull(0))
                    {
                        lvFavorite.setVisibility(View.GONE);
                        tvNoFavoriteAvailable.setVisibility(View.VISIBLE);
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


                        pojoFavourite.add(new POJOGetAllFavourite(id,username,productCategory,
                                productImage, productName,productCost,productRating,
                                productQuantity,productDescription,productLocation,
                                productPickupOption,productDateAndTime,role));

                    }

                    adapterFavourite = new AdapterGetAllFavourite(pojoFavourite,FavoritesActivity.this);

                    lvFavorite.setAdapter(adapterFavourite);
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
                Toast.makeText(FavoritesActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
}