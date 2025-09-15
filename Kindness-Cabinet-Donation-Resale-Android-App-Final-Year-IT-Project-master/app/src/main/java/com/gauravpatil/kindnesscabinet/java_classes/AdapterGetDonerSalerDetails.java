package com.gauravpatil.kindnesscabinet.java_classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.R;

import java.util.List;

public class AdapterGetDonerSalerDetails extends BaseAdapter
{
    // BaseAdapter => multiple view load show
    // AdapterGetAllCategoryDetails => show multiple view collect show ListView

    List<POJOGetDonerSalerDetails> POJOGetDonerSalerDetails;
    Activity activity;

    public AdapterGetDonerSalerDetails(List<POJOGetDonerSalerDetails> POJOGetDonerSalerDetails, Activity activity)
    {
        this.POJOGetDonerSalerDetails = POJOGetDonerSalerDetails;
        this.activity = activity;
    }

    @Override
    public int getCount()
    {
        return POJOGetDonerSalerDetails.size();
    }

    @Override
    public Object getItem(int position)
    {
        return POJOGetDonerSalerDetails.get(position);
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
            view = inflater.inflate(R.layout.viewmoredetails,null);
            holder.ivProductHomeCategoryImage = view.findViewById(R.id.ivProductHomeCategoryImage);
            holder.tvHomeCategoryName = view.findViewById(R.id.tvHomeProductName);

            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }

        final POJOGetDonerSalerDetails obj = POJOGetDonerSalerDetails.get(position);
        holder.tvHomeProductName.setText(obj.getProduct_name());
        holder.tvHomeUserName.setText(obj.getProduct_name());
        holder.tvHomeCategoryName.setText(obj.getPaid_status());

        Glide.with(activity)
                .load(Urls.image +obj.getProduct_image())
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(holder.ivProductHomeCategoryImage);

        return view;
    }
    class  ViewHolder
    {
        ImageView ivProductHomeCategoryImage;
        TextView tvHomeUserName,tvHomeProductName,tvHomeCategoryName;
    }
}
