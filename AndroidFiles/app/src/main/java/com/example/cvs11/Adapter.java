package com.example.cvs11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<item_info> arrayList;

    public Adapter() {
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_layout, parent, false);

        ViewHolder viewholder = new ViewHolder(context, view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageURL = arrayList.get(position).imageURL;
        String name = arrayList.get(position).name;
        String tag = arrayList.get(position).tag;
        String price = arrayList.get(position).price;

        Glide.with(holder.itemView).load(imageURL).into(holder.imageURL);
        holder.name.setText(name);
        holder.tag.setText(tag);
        holder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayData(item_info item) {
        arrayList.add(item);
    }
}
