package com.gauravpatil.kindnesscabinet.History;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.MoreDetailsAllDonateandSellerInformationActivity;
import com.gauravpatil.kindnesscabinet.R;

public class MoreDetailsAllHistory extends AppCompatActivity {

    ImageView ivProductImage;
    TextView tvRole,tvProductDateAndTime,tvProductCategory, tvProductName, tvProductPaidStatus,
            tvProductrating, tvProductQuantity, tvProductDescription, tvProductLocation, tvProductPickupOption;


    String id, product_date_and_time,product_cat, product_image, product_name, paid_status, productrating, quantity,
            description, pickup_location, pickup_option,role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details_all_history);

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


        Glide.with(MoreDetailsAllHistory.this)
                .load(Urls.image +product_image)
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(ivProductImage);

        setTitle(product_cat);

        if (role.equals("Doner")) {
            tvRole.setTextColor(Color.GREEN);
            tvRole.setText(role);
        } else if (role.equals("Seller")) {
            tvRole.setTextColor(Color.RED);
            tvRole.setText(role);
        }

        tvProductDateAndTime.setText(product_date_and_time);
        tvProductCategory.setText(product_cat);
        tvProductName.setText(product_name);
        tvProductPaidStatus.setText(paid_status);
        tvProductrating.setText(productrating);
        tvProductQuantity.setText(quantity);
        tvProductDescription.setText(description);
        tvProductLocation.setText(pickup_location);
        tvProductPickupOption.setText(pickup_option);
    }
}