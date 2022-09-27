package com.example.cvs11;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageURL;
    public TextView tag;
    public TextView name;
    public TextView price;

    ViewHolder(Context context, View itemView) {
        super(itemView);

        imageURL = itemView.findViewById(R.id.image);
        tag = itemView.findViewById(R.id.tag);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
    }
}
