package com.gauravpatil.kindnesscabinet.PaymentGateway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gauravpatil.kindnesscabinet.Comman.DashSpan;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.Favorites.FavoritesActivity;
import com.gauravpatil.kindnesscabinet.ProgressBar;
import com.gauravpatil.kindnesscabinet.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PaymentGatewayActivity extends AppCompatActivity {

    EditText edt_card_holder_name,edt_card_number,edt_expiry_date,edt_cvv;
    Button btn_payment;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle("Payments");
        getSupportActionBar().setSubtitle("To Pay "+getIntent().getStringExtra("price"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.blue)));
            getSupportActionBar().getThemedContext();
        }

        edt_card_holder_name = findViewById(R.id.edt_card_holder_name);
        edt_card_number = findViewById(R.id.edt_card_number);
        edt_expiry_date = findViewById(R.id.edt_expiry_date);
        edt_cvv = findViewById(R.id.edt_cvv);


        edt_card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Object[] paddingSpans = editable.getSpans(0, editable.length(), DashSpan.class);
                for (Object span : paddingSpans) {
                    editable.removeSpan(span);
                }

                addSpans(editable);
            }

            private static final int GROUP_SIZE = 4;

            private void addSpans(Editable editable) {

                final int length = editable.length();
                for (int i = 1; i * (GROUP_SIZE) < length; i++) {
                    int index = i * GROUP_SIZE;
                    editable.setSpan(new DashSpan(), index - 1, index,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

        });

        edt_expiry_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len=s.toString().length();

                if (before == 0 && len == 2)
                    edt_expiry_date.append("/");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_payment = findViewById(R.id.btn_payment);

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_card_holder_name.getText().toString().isEmpty())
                {

                    edt_card_holder_name.setError("Enter Card  Holder Name");
                }
                else if (edt_card_number.getText().toString().isEmpty())
                {
                    edt_card_number.setError("Enter Card Number");
                }
                else if (edt_expiry_date.getText().toString().isEmpty())
                {
                    edt_expiry_date.setError("Enter Expiry Date");
                }
                else if (edt_cvv.getText().toString().isEmpty())
                {
                    edt_cvv.setError("Enter Valid CVV");
                }
                else
                {

                    progressDialog = new ProgressDialog(PaymentGatewayActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Payment in Process...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();
                    payment();
                }
            }
        });
    }

    private void payment()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",preferences.getString("username",""));
        params.put("card_holder_name",edt_card_holder_name.getText().toString());
        params.put("card_no",edt_card_number.getText().toString());
        params.put("card_expiry_date",edt_expiry_date.getText().toString());
        params.put("cvv_no",edt_cvv.getText().toString());

        client.post(Urls.url_add_payment, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progressDialog.dismiss();
                try {
                    String aa = response.getString("success");

                    if (aa.equals("1"))
                    {
                        startActivity(new Intent(PaymentGatewayActivity.this,Thank_YouActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(PaymentGatewayActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(PaymentGatewayActivity.this, "Could Not Connect",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}