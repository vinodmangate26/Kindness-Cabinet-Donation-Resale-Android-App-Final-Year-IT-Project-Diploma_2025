package com.gauravpatil.kindnesscabinet;

import static android.app.ProgressDialog.show;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.Favorites.FavoritesActivity;
import com.gauravpatil.kindnesscabinet.PaymentGateway.PaymentGatewayActivity;
import com.gauravpatil.kindnesscabinet.java_classes.AdapterGetAllDonateandSellerInformation;
import com.gauravpatil.kindnesscabinet.java_classes.AdapterGetDonerSalerDetails;
import com.gauravpatil.kindnesscabinet.java_classes.POJOGetAllDonateandSellerInformation;
import com.gauravpatil.kindnesscabinet.java_classes.POJOGetDonerSalerDetails;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MoreDetailsAllDonateandSellerInformationActivity extends AppCompatActivity {

    ImageView ivProductImage;
    TextView tvRole,tvProductCategory, tvProductName, tvProductPaidStatus,
            tvProductrating, tvProductQuantity, tvProductDescription, tvProductLocation, tvProductPickupOption,
            tvPersonName, tvPersonMobileno;

    Button btnrequestThroughPostOffice, btnSelfPickUp;

    String id, name, mobile_no, product_cat, product_image, product_name, paid_status, productrating, quantity,
            descrition, pickup_location, pickup_option, role;

    private boolean isFavorite = false; // Track state
    private ImageView favoriteIcon;

    ProgressDialog progressDialog;


    SharedPreferences preferences; // Store temporary data within app
    SharedPreferences.Editor editor; // edit or put the Temporary dada in SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details_all_donate_and_seller_information);

        preferences = PreferenceManager.getDefaultSharedPreferences(MoreDetailsAllDonateandSellerInformationActivity.this);
        editor= preferences.edit();

        ivProductImage = findViewById(R.id.et_product_image_title);
        tvRole = findViewById(R.id.tvRole);
        tvProductCategory = findViewById(R.id.et_product_cat_title);
        tvProductName = findViewById(R.id.et_product_name_title);
        tvProductPaidStatus = findViewById(R.id.et_paid_status_title);
        tvProductrating = findViewById(R.id.et_productrating_title);
        tvProductQuantity = findViewById(R.id.et_quantity_title);
        tvProductDescription = findViewById(R.id.et_description_title);
        tvProductLocation = findViewById(R.id.et_pickup_location_title);
        tvProductPickupOption = findViewById(R.id.et_pickup_option_title);
        tvPersonName = findViewById(R.id.tv_person_name);
        tvPersonMobileno = findViewById(R.id.tv_person_mobile_number);
        btnrequestThroughPostOffice = findViewById(R.id.btnRequesttosentthroughpostoffice);
        btnSelfPickUp = findViewById(R.id.btnSelfPickupBySellerAddress);


        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        mobile_no = getIntent().getStringExtra("mobile_no");
        product_cat = getIntent().getStringExtra("product_cat");
        product_image = getIntent().getStringExtra("product_image");
        product_name = getIntent().getStringExtra("product_name");
        paid_status = getIntent().getStringExtra("paid_status");
        productrating = getIntent().getStringExtra("productrating");
        quantity = getIntent().getStringExtra("quantity");
        descrition = getIntent().getStringExtra("descrition");
        pickup_location = getIntent().getStringExtra("pickup_location");
        pickup_option = getIntent().getStringExtra("pickup_option");
        role = getIntent().getStringExtra("role");

        favoriteIcon = findViewById(R.id.favorite_icon);

        Glide.with(MoreDetailsAllDonateandSellerInformationActivity.this)
                .load(Urls.image +product_image)
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(ivProductImage);

        if (role.equals("Doner")) {
            tvRole.setTextColor(Color.GREEN);
            tvRole.setText(role);
        } else if (role.equals("Seller")) {
            tvRole.setTextColor(Color.RED);
            tvRole.setText(role);
        }

        tvProductCategory.setText(product_cat);
        tvProductName.setText(product_name);
        tvProductPaidStatus.setText(paid_status);
        tvProductrating.setText(productrating);
        tvProductQuantity.setText(quantity);
        tvProductDescription.setText(descrition);
        tvProductLocation.setText(pickup_location);
        tvProductPickupOption.setText(pickup_option);
        tvPersonName.setText(name);
        tvPersonMobileno.setText(mobile_no);

        btnrequestThroughPostOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MoreDetailsAllDonateandSellerInformationActivity.this);
                progressDialog.setTitle("Request Sending");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                saveRequestThroughPostOffice();
            }
        });



        btnSelfPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvProductPaidStatus.getText().toString().equals("free") ||
                        tvProductPaidStatus.getText().toString().equals("Free") ||
                        tvProductPaidStatus.getText().toString().equals("0") ||
                        tvProductPaidStatus.getText().toString().equals("0 Rs") ||
                        tvProductPaidStatus.getText().toString().equals("0 rs") ||
                        tvProductPaidStatus.getText().toString().equals("0 rupees") ||
                        tvProductPaidStatus.getText().toString().equals("0 Rupees"))
                {
                    progressDialog = new ProgressDialog(MoreDetailsAllDonateandSellerInformationActivity.this);
                    progressDialog.setTitle("Request Sending");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    btnSelfPickup();
                }
                else
                {
                    Intent intent = new Intent(MoreDetailsAllDonateandSellerInformationActivity.this,
                            PaymentGatewayActivity.class);
                    intent.putExtra("price",tvProductPaidStatus.getText().toString());
                    editor.putString("product_id",id).commit();
                    editor.putString("product_category",tvProductCategory.getText().toString()).commit();
                    editor.putString("product_image",product_image).commit();
                    editor.putString("product_name",tvProductName.getText().toString()).commit();
                    editor.putString("product_paid_status",tvProductPaidStatus.getText().toString()).commit();
                    editor.putString("product_rating",tvProductrating.getText().toString()).commit();
                    editor.putString("product_qunatity",tvProductQuantity.getText().toString()).commit();
                    editor.putString("product_description",tvProductDescription.getText().toString()).commit();
                    editor.putString("product_location",tvProductLocation.getText().toString()).commit();
                    editor.putString("product_pickup_option",tvProductPickupOption.getText().toString()).commit();
                    editor.putString("doner_seller_name",tvPersonName.getText().toString()).commit();
                    editor.putString("doner_seller_mobile_no",tvPersonMobileno.getText().toString()).commit();
                    editor.putString("type",tvRole.getText().toString()).commit();
                    editor.putString("pick_up_type",tvRole.getText().toString()).commit();

                    startActivity(intent);
                }
            }
        });

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite; // Toggle state

                if (isFavorite) {
                    favoriteIcon.setImageResource(R.drawable.icon_favourite_feel); // Red Heart
                    progressDialog = new ProgressDialog(MoreDetailsAllDonateandSellerInformationActivity.this);
                    progressDialog.setTitle("Adding to Favourite Product");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    addFavouriteProduct();
                } else {
                    favoriteIcon.setImageResource(R.drawable.icon_favourite_blank); // Empty Heart
                }
            }
        });

        getCommanFavoriteProduct();

    }

    private void getCommanFavoriteProduct() {

        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        client.post(Urls.getCommanFavoriteProduct,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                try
                {
                    JSONArray jsonArray = response.getJSONArray("getCommanFavouriteProduct");

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String product_name = jsonObject.getString("product_name");

                        if (tvProductName.getText().toString().equals(product_name))
                        {
                            Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Product Match", Toast.LENGTH_SHORT).show();
                            favoriteIcon.setImageResource(R.drawable.icon_favourite_feel); // Red Heart
                        }
                        else
                        {
                            favoriteIcon.setImageResource(R.drawable.icon_favourite_blank); // Empty Heart
                        }

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
                Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });


    }

    private void addFavouriteProduct() {

        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("username",preferences.getString("username",""));
        params.put("product_category",tvProductCategory.getText().toString());
        params.put("product_image",product_image);
        params.put("product_name",tvProductName.getText().toString());
        params.put("product_paid_status",tvProductPaidStatus.getText().toString());
        params.put("product_rating",tvProductrating.getText().toString());
        params.put("product_qunatity",tvProductQuantity.getText().toString());
        params.put("product_description",tvProductDescription.getText().toString());
        params.put("product_location",tvProductLocation.getText().toString());
        params.put("product_pickup_option",tvProductPickupOption.getText().toString());
        params.put("type",tvRole.getText().toString());
        params.put("pick_up_type",tvRole.getText().toString());

        client.post(Urls.urladdFavouriteProduct,params,
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
                                Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Request Sent Successfully Done", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MoreDetailsAllDonateandSellerInformationActivity.this, FavoritesActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Fail", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

    }

    private void btnSelfPickup()
    {
        Intent intent = new Intent(MoreDetailsAllDonateandSellerInformationActivity.this, PaymentGatewayActivity.class);
        startActivity(intent);
    }

    private void saveRequestThroughPostOffice()
    {
        //Client and Server Communication over network data transfer or manipulate
        AsyncHttpClient client = new AsyncHttpClient(); //Client and Server Communication
        RequestParams params = new RequestParams(); // Put the data

        params.put("product_id",id);
        params.put("product_category",tvProductCategory.getText().toString());
        params.put("product_image",product_image);
        params.put("product_name",tvProductName.getText().toString());
        params.put("product_paid_status",tvProductPaidStatus.getText().toString());
        params.put("product_rating",tvProductrating.getText().toString());
        params.put("product_qunatity",tvProductQuantity.getText().toString());
        params.put("product_description",tvProductDescription.getText().toString());
        params.put("product_location",tvProductLocation.getText().toString());
        params.put("product_pickup_option",tvProductPickupOption.getText().toString());
        params.put("doner_seller_name",tvPersonName.getText().toString());
        params.put("doner_seller_mobile_no",tvPersonMobileno.getText().toString());
        params.put("type",tvRole.getText().toString());
        params.put("pick_up_type",tvRole.getText().toString());

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
                                Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Request Sent Successfully Done", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MoreDetailsAllDonateandSellerInformationActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Fail", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MoreDetailsAllDonateandSellerInformationActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }
}