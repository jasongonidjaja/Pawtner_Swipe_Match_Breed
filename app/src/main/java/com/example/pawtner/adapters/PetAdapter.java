package com.example.pawtner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawtner.R;
import com.example.pawtner.model.Pet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private final Context context;
    private final List<Pet> originalList;
    private final List<Pet> filteredList;
    private final OnPetClickListener listener;

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;
    public static final int TYPE_THREE = 2;

    public interface OnPetClickListener {
        void onPetClick(Pet pet);
    }

    public PetAdapter(Context context, List<Pet> petList, OnPetClickListener listener) {
        this.context = context;
        this.originalList = new ArrayList<>(petList);
        this.filteredList = new ArrayList<>(petList);
        this.listener = listener;
    }

    // Dapatkan index dari original list
    private int getOriginalIndex(Pet pet) {
        return originalList.indexOf(pet);
    }

    @Override
    public int getItemViewType(int position) {
        Pet pet = filteredList.get(position);
        int originalIndex = getOriginalIndex(pet);
        if (originalIndex == -1) return TYPE_ONE; // fallback
        return originalIndex % 3; // layout berbeda tiap urutan
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if (viewType == TYPE_ONE) {
            layout = R.layout.item_pet;
        } else if (viewType == TYPE_TWO) {
            layout = R.layout.item_pet_2;
        } else {
            layout = R.layout.item_pet_3;
        }

        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = filteredList.get(position);
        holder.txtName.setText(pet.getName());
        holder.txtDetails.setText(pet.getDetails());
        holder.imgPet.setImageResource(pet.getProfileImageRes());
        holder.bgImage.setImageResource(pet.getBackgroundImageRes());
        holder.bind(pet, listener);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String keyword) {
        filteredList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String lower = keyword.toLowerCase();
            for (Pet pet : originalList) {
                if (pet.getName().toLowerCase().contains(lower) ||
                        pet.getDetails().toLowerCase().contains(lower)) {
                    filteredList.add(pet);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByType(String type) {
        filteredList.clear();
        if (type.equalsIgnoreCase("All")) {
            filteredList.addAll(originalList);
        } else {
            for (Pet pet : originalList) {
                if (pet.getType().equalsIgnoreCase(type)) {
                    filteredList.add(pet);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bgImage;
        CircleImageView imgPet;
        TextView txtName, txtDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bgImage = itemView.findViewById(R.id.bgImage);
            imgPet = itemView.findViewById(R.id.imgPet);
            txtName = itemView.findViewById(R.id.txtName);
            txtDetails = itemView.findViewById(R.id.txtDetails);
        }

        public void bind(final Pet pet, final OnPetClickListener listener) {
            if (listener != null) {
                itemView.setOnClickListener(v -> listener.onPetClick(pet));
            }
        }
    }
}
