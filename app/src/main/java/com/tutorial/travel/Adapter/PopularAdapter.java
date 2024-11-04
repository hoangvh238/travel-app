package com.tutorial.travel.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.tutorial.travel.Activity.DetailActivity;
import com.tutorial.travel.Domain.PopularDomain;
import com.tutorial.travel.Helpers.ImageHelpers;
import com.tutorial.travel.R;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    List<PopularDomain> items;
    // DecimalFormat formatter;

    public PopularAdapter(List<PopularDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewholder_popular,
                parent,
                false
        );
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        holder.placeNameTxt.setText(items.get(position).getPlaceName());
        holder.locationTxt.setText(items.get(position).getLocation());
        holder.categoryTxt.setText(items.get(position).getCategoryId()+"");

        int drawableResourceId = holder.itemView.getResources().getIdentifier(
                items.get(position).getImageUrl(),
                "drawable",
                holder.itemView.getContext().getPackageName());

        Bitmap image = ImageHelpers.getImageFromStorage(holder.itemView.getContext(), items.get(position).getImageUrl());
        Glide.with(holder.itemView.getContext())
                .asBitmap() // Indicate that we are loading a Bitmap
                .load(image)
                .transform(new CenterCrop(), new GranularRoundedCorners(40, 40, 40, 40))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("object", items.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView placeNameTxt, locationTxt, categoryTxt;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            placeNameTxt = itemView.findViewById(R.id.placeNameTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            categoryTxt = itemView.findViewById(R.id.categoryTxt);
            
            pic = itemView.findViewById(R.id.picImg);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Delete");
        }
    }
}
