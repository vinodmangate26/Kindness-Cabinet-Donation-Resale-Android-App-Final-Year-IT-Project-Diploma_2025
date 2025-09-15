package com.gauravpatil.kindnesscabinet.History;

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
import com.gauravpatil.kindnesscabinet.java_classes.POJOGetAllDonateandSellerInformation;

import java.util.List;

public class AdapterGetAllHistory extends BaseAdapter
{
    // BaseAdapter => multiple view load show
    // AdapterGetAllCategoryDetails => show multiple view collect show ListView

    List<POJOGetAllHistory> pojoGetAllHistories;
    Activity activity;

    public AdapterGetAllHistory(List<POJOGetAllHistory> pojoGetAllHistories,
                                Activity activity)
    {
        this.pojoGetAllHistories = pojoGetAllHistories;
        this.activity = activity;
    }


    @Override
    public int getCount()
    {
        return pojoGetAllHistories.size();
    }

    @Override
    public Object getItem(int position)
    {
        return pojoGetAllHistories.get(position);
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
            view = inflater.inflate(R.layout.lv_getall_history,null);
            holder.ivHistoryProductCategoryImage = view.findViewById(R.id.ivHistoryProductCategoryImage);
            holder.tvRole = view.findViewById(R.id.tvRole);
            holder.tvHistoryCategoryName = view.findViewById(R.id.tvHistoryCategoryName);
            holder.tvHistoryProductName = view.findViewById(R.id.tvHistoryProductName);
            holder.btnAllHistoryViewDetails = view.findViewById(R.id.btnAllHistoryViewDetails);

            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }

        final POJOGetAllHistory obj = pojoGetAllHistories.get(position);
        holder.tvHistoryCategoryName.setText(obj.getProduct_category());
        holder.tvHistoryProductName.setText(obj.getProduct_name());

        if (obj.getRole().equals("Doner")) {
            holder.tvRole.setTextColor(Color.GREEN);
            holder.tvRole.setText(obj.getRole());
        } else if (obj.getRole().equals("Seller")) {
            holder.tvRole.setTextColor(Color.RED);
            holder.tvRole.setText(obj.getRole());
        }

        Glide.with(activity)
                .load(Urls.image +obj.getProduct_image())
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(holder.ivHistoryProductCategoryImage);

        holder.btnAllHistoryViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(activity, MoreDetailsAllHistory.class);
                intent1.putExtra("id",obj.getId());
                intent1.putExtra("name",obj.getUsername());
                intent1.putExtra("product_cat",obj.getProduct_category());
                intent1.putExtra("product_image",obj.getProduct_image());
                intent1.putExtra("product_name",obj.getProduct_name());
                intent1.putExtra("paid_status",obj.getProduct_cost());
                intent1.putExtra("productrating",obj.getProduct_rating());
                intent1.putExtra("quantity",obj.getProduct_quantity());
                intent1.putExtra("descrition",obj.getProduct_description());
                intent1.putExtra("pickup_location",obj.getProduct_location());
                intent1.putExtra("pickup_option",obj.getProduct_pickup_option());
                intent1.putExtra("date_and_time",obj.getProduct_date_and_time());
                intent1.putExtra("role",obj.getRole());
                activity.startActivity(intent1);
            }
        });

        return view;
    }
    class  ViewHolder
    {
        ImageView ivHistoryProductCategoryImage;
        TextView tvRole,tvHistoryCategoryName,tvHistoryProductName;

        Button btnAllHistoryViewDetails;
    }
}
