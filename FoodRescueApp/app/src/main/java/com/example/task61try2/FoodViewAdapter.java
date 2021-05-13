package com.example.task61try2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61try2.model.Food;

import java.io.File;
import java.util.List;

public class FoodViewAdapter extends RecyclerView.Adapter<FoodViewAdapter.FoodViewHolder> {
    private List<Food> foodList;
    private Context context;
    private OnRowClickListener listener;

    public FoodViewAdapter(List<Food> foodList, Context context, OnRowClickListener listener) {
        this.foodList = foodList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.food_row, parent, false);
        return new FoodViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        String path = foodList.get(position).getImage();
        if (path != null) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.rfoodImageView.setImageBitmap(myBitmap);
            } else {
                holder.rfoodImageView.setImageResource(R.drawable.image);
            }
        } else {
            holder.rfoodImageView.setImageResource(R.drawable.image);
        }
        holder.rfoodNameTextView.setText(foodList.get(position).getTitle());
        holder.rfoodDescriptionTextView.setText(foodList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView rfoodImageView;
        public TextView rfoodNameTextView;
        public TextView rfoodDescriptionTextView;
        public ImageButton shareButton;
        public OnRowClickListener onRowClickListener;

        public FoodViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);
            rfoodImageView = itemView.findViewById(R.id.rfoodImageView);
            rfoodNameTextView = itemView.findViewById(R.id.rfoodNameTextView);
            rfoodDescriptionTextView = itemView.findViewById(R.id.rfoodDescriptionTextView);
            shareButton = itemView.findViewById(R.id.shareButton);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRowClickListener.onItemClick(getAdapterPosition());
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRowClickListener.onShareButtonClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnRowClickListener {
        void onItemClick(int position);

        void onShareButtonClick(int position);
    }
}
