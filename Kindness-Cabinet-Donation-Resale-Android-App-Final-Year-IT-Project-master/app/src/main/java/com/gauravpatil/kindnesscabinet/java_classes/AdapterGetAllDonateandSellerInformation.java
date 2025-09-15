package com.gauravpatil.kindnesscabinet.java_classes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.MoreDetailsAllDonateandSellerInformationActivity;
import com.gauravpatil.kindnesscabinet.R;

import java.util.List;

public class AdapterGetAllDonateandSellerInformation extends BaseAdapter
{
    // BaseAdapter => multiple view load show
    // AdapterGetAllCategoryDetails => show multiple view collect show ListView

    List<POJOGetAllDonateandSellerInformation> pojoGetAllDonateandSellerInformations;
    Activity activity;

    public AdapterGetAllDonateandSellerInformation(List<POJOGetAllDonateandSellerInformation> pojoGetAllDonateandSellerInformations,
                                                   Activity activity)
    {
        this.pojoGetAllDonateandSellerInformations = pojoGetAllDonateandSellerInformations;
        this.activity = activity;
    }


    @Override
    public int getCount()
    {
        return pojoGetAllDonateandSellerInformations.size();
    }

    @Override
    public Object getItem(int position)
    {
        return pojoGetAllDonateandSellerInformations.get(position);
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
            view = inflater.inflate(R.layout.lv_getalldonateandsellerinformation,null);
            holder.ivProductHomeCategoryImage = view.findViewById(R.id.ivProductHomeCategoryImage);
            holder.tvHomeRole = view.findViewById(R.id.tvHomeRole);
            holder.tvHomeName = view.findViewById(R.id.tvHomeName);
            holder.tvHomeProductName = view.findViewById(R.id.tvHomeProductName);
            holder.tvHomeCategoryName = view.findViewById(R.id.tvHomeCategoryName);
            holder.btnAllDonateandSellerInformationViewDetails = view.findViewById(R.id.btnAllDonateandSellerInformationViewDetails);

            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }

        final POJOGetAllDonateandSellerInformation obj = pojoGetAllDonateandSellerInformations.get(position);
        holder.tvHomeName.setText(obj.getName());
        holder.tvHomeProductName.setText(obj.getProduct_name());
        holder.tvHomeCategoryName.setText(obj.getProduct_cat());

        if (obj.getRole().equals("Doner")) {
            holder.tvHomeRole.setTextColor(Color.GREEN);
            holder.tvHomeRole.setText(obj.getRole());
        } else if (obj.getRole().equals("Seller")) {
            holder.tvHomeRole.setTextColor(Color.RED);
            holder.tvHomeRole.setText(obj.getRole());
        }

        Glide.with(activity)
                .load(Urls.image +obj.getProduct_image())
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(holder.ivProductHomeCategoryImage);

        holder.btnAllDonateandSellerInformationViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(activity, MoreDetailsAllDonateandSellerInformationActivity.class);
                intent1.putExtra("id",obj.getId());
                intent1.putExtra("name",obj.getName());
                intent1.putExtra("mobile_no",obj.getMobile_no());
                intent1.putExtra("product_cat",obj.getProduct_cat());
                intent1.putExtra("product_image",obj.getProduct_image());
                intent1.putExtra("product_name",obj.getProduct_name());
                intent1.putExtra("paid_status",obj.getPaid_status());
                intent1.putExtra("productrating",obj.getProductrating());
                intent1.putExtra("quantity",obj.getQuantity());
                intent1.putExtra("descrition",obj.getDescrition());
                intent1.putExtra("pickup_location",obj.getPickup_location());
                intent1.putExtra("pickup_option",obj.getPickup_option());
                intent1.putExtra("role",obj.getRole());
                activity.startActivity(intent1);
            }
        });

        return view;
    }
    class  ViewHolder
    {
        ImageView ivProductHomeCategoryImage;
        TextView tvHomeRole, tvHomeName,tvHomeCategoryName,tvHomeProductName;

        Button btnAllDonateandSellerInformationViewDetails;
    }
}
