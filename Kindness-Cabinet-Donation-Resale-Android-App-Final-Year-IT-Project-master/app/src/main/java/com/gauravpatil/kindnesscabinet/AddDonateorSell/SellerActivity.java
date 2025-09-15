package com.gauravpatil.kindnesscabinet.AddDonateorSell;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.Comman.VolleyMultipartRequest;
import com.gauravpatil.kindnesscabinet.HomeActivity;
import com.gauravpatil.kindnesscabinet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerActivity extends AppCompatActivity {

    Spinner spinner_product_category,spinner_pickup_option;
    EditText et_seller_product_name_title, et_seller_paid_status_title,
            et_seller_productrating_title, et_seller_quantity_title,
            et_seller_description_title, et_seller_pickup_location_title;
    TextView et_seller_type_title;
    AppCompatButton btnsellerSaveChanges,btn_seller_image_title;
    ImageView et_seller_product_image_title;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressBar progress;
    //to upload image
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    ImageView img_upload_profile;
    private Uri filePath;

    ArrayList<String> arrayIdList, arrayProductCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        preferences = PreferenceManager.getDefaultSharedPreferences(SellerActivity.this);
        editor= preferences.edit();

        spinner_product_category = findViewById(R.id.spinner_select_product_category);
        et_seller_product_name_title = findViewById(R.id.et_seller_product_name_title);
        et_seller_paid_status_title = findViewById(R.id.et_seller_paid_status_title);
        et_seller_productrating_title = findViewById(R.id.et_seller_productrating_title);
        et_seller_quantity_title = findViewById(R.id.et_seller_quantity_title);
        et_seller_description_title = findViewById(R.id.et_seller_description_title);
        et_seller_pickup_location_title =findViewById(R.id.et_seller_pickup_location_title);
        spinner_pickup_option = findViewById(R.id.spinner_pickup_option);
        et_seller_type_title = findViewById(R.id.et_seller_type_title);
        btnsellerSaveChanges = findViewById(R.id.btn_sellerAll_Details);
        btn_seller_image_title = findViewById(R.id.btn_seller_image_title);
        et_seller_product_image_title = findViewById(R.id.et_seller_product_image_title);


        arrayIdList = new ArrayList<>();
        arrayProductCategoryName = new ArrayList<>();
        getProductCategory();


        //READ_EXTERNAL_STORAGE PERMISSION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }

        //for  show a file chooser
        btn_seller_image_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnsellerSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner_product_category.getSelectedItem().toString().equals("Select Product Category")) {
                    ((TextView)spinner_product_category.getSelectedView()).setError("Select Product Category First");
                } else if (et_seller_product_name_title.getText().toString().isEmpty()) {
                    et_seller_product_name_title.setError("Please Enter Your Product Name");
                } else if (et_seller_paid_status_title.getText().toString().isEmpty()) {
                    et_seller_paid_status_title.setError("Please Enter Your Paid Status");
                } else if (et_seller_productrating_title.getText().toString().isEmpty()) {
                    et_seller_productrating_title.setError("Please Enter Your Product Rating");
                } else if (et_seller_quantity_title.getText().toString().isEmpty()) {
                    et_seller_quantity_title.setError("Please Enter Your Quantity");
                } else if (et_seller_description_title.getText().toString().isEmpty()) {
                    et_seller_description_title.setError("Please Enter Your Description");
                } else if (et_seller_pickup_location_title.getText().toString().isEmpty()) {
                    et_seller_pickup_location_title.setError("Please Enter Your Pickup Location");
                }
                else if (spinner_product_category.getSelectedItem().toString().equals("Select Product Category")) {
                    ((TextView)spinner_product_category.getSelectedView()).setError("Select Product Category First");
                }
                else if (et_seller_type_title.getText().toString().isEmpty()) {
                    et_seller_type_title.setError("Please Enter Your Type");
                } else {
                    doner_seller_tbl();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //choose the image for external storage
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //set selected image into imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                et_seller_product_image_title.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getProductCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.getAllCategoryDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("getAllcategory");
                            arrayProductCategoryName.clear();
                            arrayIdList.add("-1");
                            arrayProductCategoryName.add("Select Product Category");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject getlistObject = jsonArray.getJSONObject(i);

                                String categoryname = getlistObject.getString("catagoryname");
                                arrayProductCategoryName.add(categoryname);


                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SellerActivity.this
                                    ,android.R.layout.simple_spinner_item, arrayProductCategoryName);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_product_category.setAdapter(adapter);
                            spinner_product_category.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SellerActivity.this, "Shop Service Exception" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SellerActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        RequestQueue requestQueue = Volley.newRequestQueue(SellerActivity.this);
        requestQueue.add(stringRequest);
    }


    private void doner_seller_tbl() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.getDonerSalerCategoryDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            String success = obj.getString("Success");

                            if (success.equals("1"))
                            {
                                uploadBitmap(bitmap, obj.getInt("lastinsertedid"));
                            }else {
//                                String message = obj.getString("message");
                                Toast.makeText(SellerActivity.this, response,Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SellerActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", preferences.getString("name",""));
                params.put("mobile_no", preferences.getString("mobile_no",""));
                params.put("product_cat", spinner_product_category.getSelectedItem().toString());
                params.put("product_name", et_seller_product_name_title.getText().toString());
                params.put("paid_status", et_seller_paid_status_title.getText().toString());
                params.put("productrating", et_seller_productrating_title.getText().toString());
                params.put("quantity", et_seller_quantity_title.getText().toString());
                params.put("description", et_seller_description_title.getText().toString());
                params.put("pickup_location", et_seller_pickup_location_title.getText().toString());
                params.put("pickup_option", spinner_product_category.getSelectedItem().toString());
                params.put("role", et_seller_type_title.getText().toString());
                return params;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        RequestQueue requestQueue = Volley.newRequestQueue(SellerActivity.this);
        requestQueue.add(stringRequest);
    }


    private void uploadBitmap(final Bitmap bitmap,final int lastinsertedid) {
        //getting the tag from the edittext
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                Urls.urladddonateimage,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(SellerActivity.this,"Seller Data Added Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SellerActivity.this, HomeActivity.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags",""+ lastinsertedid);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SellerActivity.this, HomeActivity.class));
        finish();
    }
}