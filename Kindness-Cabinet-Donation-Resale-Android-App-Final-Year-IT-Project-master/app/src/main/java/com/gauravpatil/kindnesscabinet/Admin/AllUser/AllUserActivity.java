package com.gauravpatil.kindnesscabinet.Admin.AllUser;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Admin.History.POJOAdminGetAllHistory;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.Favorites.FavoritesActivity;
import com.gauravpatil.kindnesscabinet.History.AdapterGetAllHistory;
import com.gauravpatil.kindnesscabinet.History.POJOGetAllHistory;
import com.gauravpatil.kindnesscabinet.R;
import com.gauravpatil.kindnesscabinet.java_classes.AdapterGetAllCategoryDetails;
import com.gauravpatil.kindnesscabinet.java_classes.POJOGetAllCategoryDetails;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AllUserActivity extends AppCompatActivity {

    ListView lvShowAllUser;
    TextView tvNoUserAvailable;
    List<POJOGetAllUserDetails> pojoGetAllUserDetails;
    AdapterGetAllUserDetails adapterGetAllUserDetails;
    SearchView searchUser;

    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        preferences = PreferenceManager.getDefaultSharedPreferences(AllUserActivity.this);
        editor= preferences.edit();

        pojoGetAllUserDetails = new ArrayList<>();
        searchUser = findViewById(R.id.svAdminAllUserSearchUser);
        lvShowAllUser = findViewById(R.id.lvAdminAllUserAllUser);
        tvNoUserAvailable = findViewById(R.id.tvAdminAllUserNoUserAvailable);
        progressDialog = new ProgressDialog(AllUserActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("All User Loading in Progress...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        setTitle("Your All User");

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchUser(query);
                return false;
            }
        });

        getAllUser();
    }

    private void searchUser(String query) {
        List<POJOGetAllUserDetails> tempCategory = new ArrayList<>();

        for (POJOGetAllUserDetails obj : pojoGetAllUserDetails) {
            if (obj.getName().toUpperCase().contains(query.toUpperCase())) {
                tempCategory.add(obj);
            }
        }

        // Show or hide UI elements based on search result
        if (tempCategory.isEmpty()) {
            lvShowAllUser.setVisibility(View.GONE);
            tvNoUserAvailable.setVisibility(View.VISIBLE);
        } else {
            lvShowAllUser.setVisibility(View.VISIBLE);
            tvNoUserAvailable.setVisibility(View.GONE);
        }

        adapterGetAllUserDetails = new AdapterGetAllUserDetails(tempCategory, AllUserActivity.this);
        lvShowAllUser.setAdapter(adapterGetAllUserDetails);
    }

    private void getAllUser() {

        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); //put the data in AsyncHttpClient object

        client.post(Urls.adminAllUser,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                try
                {
                    JSONArray jsonArray = response.getJSONArray("getAllUser");

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String strid = jsonObject.getString("id");
                        String strImage = jsonObject.getString("image");
                        String strName = jsonObject.getString("name");
                        String strMobileNo = jsonObject.getString("mobile_no");
                        String strEmailID = jsonObject.getString("email_id");
                        String strGender = jsonObject.getString("gender");
                        String strAge = jsonObject.getString("age");
                        String StrAddress = jsonObject.getString("address");
                        String strUsername = jsonObject.getString("username");

                        pojoGetAllUserDetails.add(new POJOGetAllUserDetails(strid,strImage,strName,
                                strMobileNo, strEmailID,strGender,strAge,
                                StrAddress,strUsername));
                    }

                    adapterGetAllUserDetails = new AdapterGetAllUserDetails(pojoGetAllUserDetails,
                            AllUserActivity.this);

                    lvShowAllUser.setAdapter(adapterGetAllUserDetails);

                    progressDialog.dismiss();
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
                Toast.makeText(AllUserActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }
}