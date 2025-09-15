package com.gauravpatil.kindnesscabinet.Favorites;

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
import com.gauravpatil.kindnesscabinet.History.MoreDetailsAllHistory;
import com.gauravpatil.kindnesscabinet.History.POJOGetAllHistory;
import com.gauravpatil.kindnesscabinet.R;

import java.util.List;

public class AdapterGetAllFavourite extends BaseAdapter
{
    // BaseAdapter => multiple view load show
    // AdapterGetAllCategoryDetails => show multiple view collect show ListView

    List<POJOGetAllFavourite> pojoGetAllFavourites;
    Activity activity;

    public AdapterGetAllFavourite(List<POJOGetAllFavourite> pojoGetAllFavourites,
                                  Activity activity)
    {
        this.pojoGetAllFavourites = pojoGetAllFavourites;
        this.activity = activity;
    }


    @Override
    public int getCount()
    {
        return pojoGetAllFavourites.size();
    }

    @Override
    public Object getItem(int position)
    {
        return pojoGetAllFavourites.get(position);
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
            view = inflater.inflate(R.layout.lv_getall_favourite,null);
            holder.ivFavouriteProductCategoryImage = view.findViewById(R.id.ivFavouriteProductCategoryImage);
            holder.tvRole = view.findViewById(R.id.tvRole);
            holder.tvFavouriteCategoryName = view.findViewById(R.id.tvFavouriteCategoryName);
            holder.tvFavouriteProductName = view.findViewById(R.id.tvFavouriteProductName);
            holder.btnAllFavouriteViewDetails = view.findViewById(R.id.btnAllFavouriteViewDetails);

            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }

        final POJOGetAllFavourite obj = pojoGetAllFavourites.get(position);
        holder.tvFavouriteCategoryName.setText(obj.getProduct_category());
        holder.tvFavouriteProductName.setText(obj.getProduct_name());

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
                .into(holder.ivFavouriteProductCategoryImage);

        holder.btnAllFavouriteViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(activity, MoreDetailsFavouriteActivity.class);
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
        ImageView ivFavouriteProductCategoryImage;
        TextView tvRole,tvFavouriteCategoryName,tvFavouriteProductName;

        Button btnAllFavouriteViewDetails;
    }
}
