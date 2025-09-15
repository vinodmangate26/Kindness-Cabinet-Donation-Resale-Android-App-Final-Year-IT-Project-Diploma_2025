package com.gauravpatil.kindnesscabinet.Admin.AllUser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Admin.HomeAdminActivity;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.Favorites.MoreDetailsFavouriteActivity;
import com.gauravpatil.kindnesscabinet.Favorites.POJOGetAllFavourite;
import com.gauravpatil.kindnesscabinet.HomeActivity;
import com.gauravpatil.kindnesscabinet.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdapterGetAllUserDetails extends BaseAdapter
{
    // BaseAdapter => multiple view load show
    // AdapterGetAllCategoryDetails => show multiple view collect show ListView

    List<POJOGetAllUserDetails> pojoGetAllUserDetails;
    Activity activity;

    public AdapterGetAllUserDetails(List<POJOGetAllUserDetails> pojoGetAllUserDetails,
                                    Activity activity)
    {
        this.pojoGetAllUserDetails = pojoGetAllUserDetails;
        this.activity = activity;
    }


    @Override
    public int getCount()
    {
        return pojoGetAllUserDetails.size();
    }

    @Override
    public Object getItem(int position)
    {
        return pojoGetAllUserDetails.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_admin_all_user,null);
            holder.ivUserImage = view.findViewById(R.id.civProfilePhoto);
            holder.tvName = view.findViewById(R.id.etProfileName);
            holder.tvMobileNo = view.findViewById(R.id.etProfileMobileNo);
            holder.tvEmailid = view.findViewById(R.id.etProfileEmailId);
            holder.tvGender = view.findViewById(R.id.etProfileGender);
            holder.tvAge = view.findViewById(R.id.etProfileAge);
            holder.tvAddress = view.findViewById(R.id.etProfileAddress);
            holder.tvUsername = view.findViewById(R.id.etProfileUsername);
            holder.btnDeleteUser = view.findViewById(R.id.btnProfileDeleteUser);

            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }

        final POJOGetAllUserDetails obj = pojoGetAllUserDetails.get(position);
        holder.tvName.setText(obj.getName());
        holder.tvMobileNo.setText(obj.getMobileNo());
        holder.tvEmailid.setText(obj.getEmailid());
        holder.tvGender.setText(obj.getGender());
        holder.tvAge.setText(obj.getAge());
        holder.tvAddress.setText(obj.getAddress());
        holder.tvUsername.setText(obj.getUsername());

        Glide.with(activity)
                .load(Urls.image +obj.getImage())
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(holder.ivUserImage);

        holder.btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(activity);
                ad.setTitle("Delete User")
                        .setMessage("Are You Sure You Want To Delete User")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteUser(Integer.parseInt(obj.getId()));
                            }
                        });

                AlertDialog alertDialog = ad.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
            }
        });


        return view;
    }

    private void deleteUser(int userid) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", userid);
        client.post(Urls.url_delete_user, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,
                                  cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String aa = response.getString("success");

                    if (aa.equals("1")) {
                        Intent intent = new Intent(activity, HomeAdminActivity.class);
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, "Unable to delete User", Toast.LENGTH_SHORT).show();
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

    class  ViewHolder
    {
        ImageView ivUserImage;
        TextView tvName,tvMobileNo,tvEmailid,tvGender,tvAge,tvAddress,tvUsername;

        Button btnDeleteUser;
    }
}
