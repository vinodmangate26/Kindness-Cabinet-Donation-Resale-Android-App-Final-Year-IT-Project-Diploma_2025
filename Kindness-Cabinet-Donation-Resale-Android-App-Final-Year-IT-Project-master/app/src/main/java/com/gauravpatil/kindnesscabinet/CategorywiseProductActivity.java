package com.gauravpatil.kindnesscabinet;

import android.app.ProgressDialog;
import android.os.Bundle;
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

public class CategorywiseProductActivity extends AppCompatActivity {

    ListView lvCategorywiseDonateandSeller;
    TextView tvCategorywiseDonateandSellerNoCategoryAvailable;
    List<POJOGetAllDonateandSellerInformation> pojoGetAllDonateandSellerInformations;
    AdapterGetAllDonateandSellerInformation adapterGetAllDonateandSellerInformation;

    ProgressDialog progressDialog;


    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorywise_product);

        category = getIntent().getStringExtra("category");
        pojoGetAllDonateandSellerInformations = new ArrayList<>();
        lvCategorywiseDonateandSeller = findViewById(R.id.lvCategorywiseDonateandSeller);
        tvCategorywiseDonateandSellerNoCategoryAvailable = findViewById(R.id.tvCategorywiseDonateandSellerNoCategoryAvailable);
        progressDialog = new ProgressDialog(CategorywiseProductActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading Categorytwise Product...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getCategorywiseDonateandSeller();
    }

    private void getCategorywiseDonateandSeller() {
        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("category",category);

        client.post(Urls.getCategorywiseDonateandSellerInformation,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                try
                {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("getCategorywiseDonateandSellerInformation");

                    if(jsonArray.isNull(0))
                    {
                        tvCategorywiseDonateandSellerNoCategoryAvailable.setVisibility(View.VISIBLE);
                    }

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String Strid = jsonObject.getString("id");
                        String Strname = jsonObject.getString("name");
                        String Strmobile_no = jsonObject.getString("mobile_no");
                        String Strproduct_cat = jsonObject.getString("product_cat");
                        String Strproduct_image = jsonObject.getString("product_image");
                        String Strproduct_name = jsonObject.getString("product_name");
                        String Strpaid_status = jsonObject.getString("paid_status");
                        String Strproductrating = jsonObject.getString("productrating");
                        String Strquantity = jsonObject.getString("quantity");
                        String Strdescrition = jsonObject.getString("descrition");
                        String Strpickup_location = jsonObject.getString("pickup_location");
                        String Strpickup_option = jsonObject.getString("pickup_option");
                        String Strrole = jsonObject.getString("role");

                        pojoGetAllDonateandSellerInformations.add(new POJOGetAllDonateandSellerInformation(Strid,Strname,Strmobile_no,
                                Strproduct_cat, Strproduct_image,Strproduct_name,Strpaid_status,
                                Strproductrating,Strquantity,Strdescrition,
                                Strpickup_location,Strpickup_option,Strrole));

                    }

                    adapterGetAllDonateandSellerInformation = new AdapterGetAllDonateandSellerInformation(pojoGetAllDonateandSellerInformations,
                            CategorywiseProductActivity.this);

                    lvCategorywiseDonateandSeller.setAdapter(adapterGetAllDonateandSellerInformation);
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
                Toast.makeText(CategorywiseProductActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });


    }
}