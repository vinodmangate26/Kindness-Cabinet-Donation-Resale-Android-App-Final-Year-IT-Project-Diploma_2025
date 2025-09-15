package com.gauravpatil.kindnesscabinet.Favorites;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.History.MoreDetailsAllHistory;
import com.gauravpatil.kindnesscabinet.HomeActivity;
import com.gauravpatil.kindnesscabinet.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MoreDetailsFavouriteActivity extends AppCompatActivity {

    ImageView ivProductImage;
    TextView tvRole,tvProductDateAndTime, tvProductCategory, tvProductName, tvProductPaidStatus,
            tvProductrating, tvProductQuantity, tvProductDescription, tvProductLocation, tvProductPickupOption;

    Button btnDeleteFavourite;

    String id, product_date_and_time, product_cat, product_image, product_name, paid_status, productrating, quantity,
            description, pickup_location, pickup_option,role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details_favourite);

        ivProductImage = findViewById(R.id.et_product_image_title);
        tvRole = findViewById(R.id.tvRole);
        tvProductDateAndTime = findViewById(R.id.et_product_date_and_time_title);
        tvProductCategory = findViewById(R.id.et_product_cat_title);
        tvProductName = findViewById(R.id.et_product_name_title);
        tvProductPaidStatus = findViewById(R.id.et_paid_status_title);
        tvProductrating = findViewById(R.id.et_productrating_title);
        tvProductQuantity = findViewById(R.id.et_quantity_title);
        tvProductDescription = findViewById(R.id.et_description_title);
        tvProductLocation = findViewById(R.id.et_pickup_location_title);
        tvProductPickupOption = findViewById(R.id.et_pickup_option_title);
        btnDeleteFavourite = findViewById(R.id.btnDeleteFavourite);

        id = getIntent().getStringExtra("id");
        product_date_and_time = getIntent().getStringExtra("date_and_time");
        product_cat = getIntent().getStringExtra("product_cat");
        product_image = getIntent().getStringExtra("product_image");
        product_name = getIntent().getStringExtra("product_name");
        paid_status = getIntent().getStringExtra("paid_status");
        productrating = getIntent().getStringExtra("productrating");
        quantity = getIntent().getStringExtra("quantity");
        description = getIntent().getStringExtra("descrition");
        pickup_location = getIntent().getStringExtra("pickup_location");
        pickup_option = getIntent().getStringExtra("pickup_option");
        role = getIntent().getStringExtra("role");


        setTitle(product_name);

        Glide.with(MoreDetailsFavouriteActivity.this)
                .load(Urls.image + product_image)
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

        setTitle(product_cat);

        tvProductDateAndTime.setText(product_date_and_time);
        tvProductCategory.setText(product_cat);
        tvProductName.setText(product_name);
        tvProductPaidStatus.setText(paid_status);
        tvProductrating.setText(productrating);
        tvProductQuantity.setText(quantity);
        tvProductDescription.setText(description);
        tvProductLocation.setText(pickup_location);
        tvProductPickupOption.setText(pickup_option);

        btnDeleteFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MoreDetailsFavouriteActivity.this);
                ad.setTitle("Delete Favourite")
                        .setMessage("Are You Sure You Want To Remove")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteFavourite(Integer.parseInt(id));
                            }
                        });

                AlertDialog alertDialog = ad.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
            }
        });

    }

    private void deleteFavourite(int idd) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", idd);
        client.post(Urls.url_delete_favourite, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String aa = response.getString("success");

                    if (aa.equals("1")) {
                        Intent intent = new Intent(MoreDetailsFavouriteActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MoreDetailsFavouriteActivity.this, "Unable to delete favourite", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable
                    , JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
