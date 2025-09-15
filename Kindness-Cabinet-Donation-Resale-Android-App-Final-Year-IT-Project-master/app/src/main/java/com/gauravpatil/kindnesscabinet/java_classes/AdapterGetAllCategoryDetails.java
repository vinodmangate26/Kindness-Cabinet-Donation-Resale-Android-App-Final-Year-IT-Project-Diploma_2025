package com.gauravpatil.kindnesscabinet.java_classes;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.CategorywiseProductActivity;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.gauravpatil.kindnesscabinet.R;
import java.util.List;

public class AdapterGetAllCategoryDetails  extends BaseAdapter
{
    // BaseAdapter => multiple view load show
    // AdapterGetAllCategoryDetails => show multiple view collect show ListView

    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetailsList;
    Activity activity;

    public AdapterGetAllCategoryDetails(List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetailsList, Activity activity)
    {
        this.pojoGetAllCategoryDetailsList = pojoGetAllCategoryDetailsList;
        this.activity = activity;
    }


    @Override
    public int getCount()
    {
        return pojoGetAllCategoryDetailsList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return pojoGetAllCategoryDetailsList.get(position);
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
            view = inflater.inflate(R.layout.lv_get_all_category,null);
            holder.cvCategories = view.findViewById(R.id.cvCategories);
            holder.ivCategoryImage = view.findViewById(R.id.ivCategoryImage);
            holder.tvCategoryName = view.findViewById(R.id.tvCategoryName);

            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }

        final POJOGetAllCategoryDetails obj = pojoGetAllCategoryDetailsList.get(position);
        holder.tvCategoryName.setText(obj.getCategoryname);

        Glide.with(activity)
                .load(Urls.image +obj.getCategoryimage())
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(holder.ivCategoryImage);

        holder.cvCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CategorywiseProductActivity.class);
                intent.putExtra("category",obj.getCategoryname);
                activity.startActivity(intent);
            }
        });

        return view;
    }
    class  ViewHolder
    {
        CardView cvCategories;
        ImageView ivCategoryImage;
        TextView tvCategoryName;
    }
}
