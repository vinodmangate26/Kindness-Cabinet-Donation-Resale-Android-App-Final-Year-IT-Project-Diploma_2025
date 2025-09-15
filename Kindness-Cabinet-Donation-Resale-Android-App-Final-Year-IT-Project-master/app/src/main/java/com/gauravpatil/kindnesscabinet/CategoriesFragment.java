package com.gauravpatil.kindnesscabinet;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.gauravpatil.kindnesscabinet.java_classes.AdapterGetAllCategoryDetails;
import com.gauravpatil.kindnesscabinet.java_classes.POJOGetAllCategoryDetails;
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

public class CategoriesFragment extends Fragment {
    ListView lvShowAllCategory;
    TextView tvNoCategoryAvailable;
    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetailsList;
    AdapterGetAllCategoryDetails adapterGetAllCategoryDetails;
    SearchView searchCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        // Initialize UI elements
        pojoGetAllCategoryDetailsList = new ArrayList<>();
        searchCategory = view.findViewById(R.id.svCategoryFragmentSearchCategory);
        lvShowAllCategory = view.findViewById(R.id.lvCategoryFragmentShowMultipleCategory);
        tvNoCategoryAvailable = view.findViewById(R.id.tvCategoryFragmentNoCategoryAvailable);

        // Hide 'No Category Available' initially
        tvNoCategoryAvailable.setVisibility(View.GONE);

        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                searchCategory(query);
                return false;
            }
        });

        getAllCategory();
        return view;
    }

    private void searchCategory(String query)
    {
        List<POJOGetAllCategoryDetails> tempCategory = new ArrayList<>();

        for (POJOGetAllCategoryDetails obj : pojoGetAllCategoryDetailsList)
        {
            if (obj.getCategoryname().toUpperCase().contains(query.toUpperCase()))
            {
                tempCategory.add(obj);
            }
        }

        // Show or hide UI elements based on search result
        if (tempCategory.isEmpty())
        {
            lvShowAllCategory.setVisibility(View.GONE);
            tvNoCategoryAvailable.setVisibility(View.VISIBLE);
        }
        else
        {
            lvShowAllCategory.setVisibility(View.VISIBLE);
            tvNoCategoryAvailable.setVisibility(View.GONE);
        }

        adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(tempCategory, getActivity());
        lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);
    }

    private void getAllCategory() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getAllCategoryDetails, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getAllcategory");

                    if (jsonArray.isNull(0)) {
                        tvNoCategoryAvailable.setVisibility(View.VISIBLE);
                        lvShowAllCategory.setVisibility(View.GONE);
                        return;
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strid = jsonObject.getString("id");
                        String strCategoryImage = jsonObject.getString("catagoryimage");
                        String strCategoryName = jsonObject.getString("catagoryname");

                        pojoGetAllCategoryDetailsList.add(new POJOGetAllCategoryDetails(strid, strCategoryImage, strCategoryName));
                    }

                    adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(pojoGetAllCategoryDetailsList, getActivity());
                    lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);

                    if (!pojoGetAllCategoryDetailsList.isEmpty()) {
                        tvNoCategoryAvailable.setVisibility(View.GONE);
                        lvShowAllCategory.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
