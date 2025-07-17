package com.example.pawtner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;
import com.example.pawtner.model.PostItem;

import java.util.List;

public class GalleryAdapterPet extends RecyclerView.Adapter<GalleryAdapterPet.ViewHolder> {

    private final List<PostItem> postList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCameraClick();
        void onImageClick(PostItem item);
    }

    public GalleryAdapterPet(List<PostItem> postList, OnItemClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Buat rasio 1:1
        holder.imagePost.post(() -> {
            int width = holder.imagePost.getWidth();
            ViewGroup.LayoutParams params = holder.imagePost.getLayoutParams();
            params.height = width;
            holder.imagePost.setLayoutParams(params);
        });

        if (position == 0) {
            holder.imagePost.setImageResource(R.drawable.ic_camera_pet); // kamera di pojok kiri atas
            holder.imagePost.setScaleType(ImageView.ScaleType.CENTER);
            holder.imagePost.setBackgroundResource(R.drawable.image_border); // opsional
            holder.imagePost.setOnClickListener(v -> listener.onCameraClick());
        } else {
            PostItem postItem = postList.get(position - 1);
            holder.imagePost.setImageResource(postItem.getImageResId());
            holder.imagePost.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imagePost.setOnClickListener(v -> listener.onImageClick(postItem));
        }
    }

    @Override
    public int getItemCount() {
        return postList.size() + 1; // +1 for camera
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.imagePost);
        }
    }
}
