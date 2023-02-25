package com.example.cvs11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<item_info> arrayList;

    public Adapter() {
        arrayList = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageURL;
        public TextView tag;
        public TextView brand;
        public TextView name;
        public TextView price;

        ViewHolder(Context context, View itemView) {
            super(itemView);

            imageURL = itemView.findViewById(R.id.image);
            tag = itemView.findViewById(R.id.tag);
            brand = itemView.findViewById(R.id.brand);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        if(onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
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
        String brand = arrayList.get(position).brand;
        String price = arrayList.get(position).price;

        Glide.with(holder.itemView).load(imageURL).into(holder.imageURL);
        holder.name.setText(name);
        holder.tag.setText(tag);
        holder.brand.setText(brand);
        holder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayData(item_info item) {
        arrayList.add(item);
    }

    public String getPID(int position) {
        return arrayList.get(position).PID;
    }
}
