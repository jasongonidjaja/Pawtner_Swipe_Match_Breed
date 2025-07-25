package com.example.pawtner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;
import com.example.pawtner.model.PostItem;
import com.example.pawtner.ui.home.OtherPetPopupDialogFragment;

import java.util.List;

public class PostOtherAdapter extends RecyclerView.Adapter<PostOtherAdapter.PostViewHolder> {

    private final List<PostItem> postList;

    public PostOtherAdapter(List<PostItem> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostItem postItem = postList.get(position);
        holder.imagePost.setImageResource(postItem.getImageResId());

        // Buat rasio 1:1
        holder.imagePost.post(() -> {
            int width = holder.imagePost.getWidth();
            ViewGroup.LayoutParams params = holder.imagePost.getLayoutParams();
            params.height = width;
            holder.imagePost.setLayoutParams(params);
        });

        holder.imagePost.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) v.getContext();
            OtherPetPopupDialogFragment.newInstance(postItem.getImageResId())
                    .show(activity.getSupportFragmentManager(), "OtherPetPopupDialog");
        });



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.imagePost);
        }
    }
}
