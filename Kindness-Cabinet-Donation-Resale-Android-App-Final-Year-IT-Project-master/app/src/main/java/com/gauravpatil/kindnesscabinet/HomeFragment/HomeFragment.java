package com.gauravpatil.kindnesscabinet.HomeFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.gauravpatil.kindnesscabinet.R;
import com.gauravpatil.kindnesscabinet.java_classes.AdapterGetAllDonateandSellerInformation;
import com.gauravpatil.kindnesscabinet.java_classes.POJOGetAllDonateandSellerInformation;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {
    ListView lvDonateSaleCategoryFragmentShowMultipleCategory;
    TextView tvDonateSaleCategoryFragmentNoCategoryAvailable;
    List<POJOGetAllDonateandSellerInformation> pojoGetAllDonateandSellerInformations;
    AdapterGetAllDonateandSellerInformation adapterGetAllDonateandSellerInformation;
    ProgressDialog progressDialog;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pojoGetAllDonateandSellerInformations = new ArrayList<>();
        lvDonateSaleCategoryFragmentShowMultipleCategory = view.findViewById(R.id.lvDonateSaleCategoryFragmentShowMultipleCategory);
        tvDonateSaleCategoryFragmentNoCategoryAvailable = view.findViewById(R.id.tvDonateSaleCategoryFragmentNoCategoryAvailable);
        searchView = view.findViewById(R.id.svCategoryFragmentSearchProductName);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Product Loading in Progress...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextSize(16);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchCategory(query);
                return false;
            }
        });

        getAllDonateSellerDetails();
        return view;
    }

    private void searchCategory(String query) {
        query = query.trim().toLowerCase();
        List<POJOGetAllDonateandSellerInformation> tempProductList = new ArrayList<>();

        for (POJOGetAllDonateandSellerInformation obj : pojoGetAllDonateandSellerInformations) {
            if (obj.getProduct_name().toLowerCase().contains(query) ||
                    obj.getProduct_cat().toLowerCase().contains(query)) {
                tempProductList.add(obj);
            }
        }

        if (tempProductList.isEmpty()) {
            lvDonateSaleCategoryFragmentShowMultipleCategory.setVisibility(View.GONE);
            tvDonateSaleCategoryFragmentNoCategoryAvailable.setVisibility(View.VISIBLE);
        } else {
            lvDonateSaleCategoryFragmentShowMultipleCategory.setVisibility(View.VISIBLE);
            tvDonateSaleCategoryFragmentNoCategoryAvailable.setVisibility(View.GONE);
        }

        adapterGetAllDonateandSellerInformation = new AdapterGetAllDonateandSellerInformation(tempProductList, getActivity());
        lvDonateSaleCategoryFragmentShowMultipleCategory.setAdapter(adapterGetAllDonateandSellerInformation);
    }

    private void getAllDonateSellerDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getAllDonateandSellerInformation, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("getAllDonateandSellerInformation");
                    pojoGetAllDonateandSellerInformations.clear();

                    if (jsonArray.length() == 0) {
                        tvDonateSaleCategoryFragmentNoCategoryAvailable.setVisibility(View.VISIBLE);
                        lvDonateSaleCategoryFragmentShowMultipleCategory.setVisibility(View.GONE);
                        return;
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        POJOGetAllDonateandSellerInformation item = new POJOGetAllDonateandSellerInformation(
                                jsonObject.getString("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("mobile_no"),
                                jsonObject.getString("product_cat"),
                                jsonObject.getString("product_image"),
                                jsonObject.getString("product_name"),
                                jsonObject.getString("paid_status"),
                                jsonObject.getString("productrating"),
                                jsonObject.getString("quantity"),
                                jsonObject.getString("descrition"),
                                jsonObject.getString("pickup_location"),
                                jsonObject.getString("pickup_option"),
                                jsonObject.getString("role")
                        );

                        pojoGetAllDonateandSellerInformations.add(item);
                    }

                    if (getActivity() != null) {
                        adapterGetAllDonateandSellerInformation = new AdapterGetAllDonateandSellerInformation(pojoGetAllDonateandSellerInformations, getActivity());
                        lvDonateSaleCategoryFragmentShowMultipleCategory.setAdapter(adapterGetAllDonateandSellerInformation);
                    }

                    tvDonateSaleCategoryFragmentNoCategoryAvailable.setVisibility(View.GONE);
                    lvDonateSaleCategoryFragmentShowMultipleCategory.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvDonateSaleCategoryFragmentNoCategoryAvailable.setVisibility(View.VISIBLE);
                    lvDonateSaleCategoryFragmentShowMultipleCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                tvDonateSaleCategoryFragmentNoCategoryAvailable.setVisibility(View.VISIBLE);
                lvDonateSaleCategoryFragmentShowMultipleCategory.setVisibility(View.GONE);
            }
        });
    }
}
