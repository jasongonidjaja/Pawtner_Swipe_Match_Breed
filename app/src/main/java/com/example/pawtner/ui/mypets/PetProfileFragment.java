package com.example.pawtner.ui.mypets;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;
import com.example.pawtner.adapters.PostAdapter;
import com.example.pawtner.model.PostItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PetProfileFragment extends Fragment {

    private ImageView petImageView;
    private Button btnChangeImage;
    private FloatingActionButton floatingBtn;
    private TextView tvPetName, tvDescription;
    private RecyclerView postsRecyclerView;

    public PetProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_petprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petImageView = view.findViewById(R.id.petImageView);
        btnChangeImage = view.findViewById(R.id.btnChangeImage);
        floatingBtn = view.findViewById(R.id.floatingBtn);
        tvPetName = view.findViewById(R.id.tvPetName);
        tvDescription = view.findViewById(R.id.tvDescription);
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        // Siapkan dummy post
        List<PostItem> postItemList = getDummyPosts();

        // Setup RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        postsRecyclerView.setLayoutManager(layoutManager);
        postsRecyclerView.setHasFixedSize(true);
        postsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 2));

        PostAdapter adapter = new PostAdapter(postItemList);
        postsRecyclerView.setAdapter(adapter);

        btnChangeImage.setOnClickListener(v -> {
            // Aksi ubah gambar profil
        });

        floatingBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_petprofile_to_newpost);
        });
    }

    private List<PostItem> getDummyPosts() {
        List<PostItem> items = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            items.add(new PostItem(R.drawable.cat_sample));
        }
        return items;
    }

    // Kelas helper untuk spacing antar item grid
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int spanCount;
        private final int spacing;

        public GridSpacingItemDecoration(int spanCount, int spacing) {
            this.spanCount = spanCount;
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;
            int row = position / spanCount;
            RecyclerView.Adapter adapter = parent.getAdapter();
            if (adapter == null) return;

            int totalRows = (int) Math.ceil((double) adapter.getItemCount() / spanCount);

            if (column > 0) {
                outRect.left = spacing / 2;
            }
            if (column < spanCount - 1) {
                outRect.right = spacing / 2;
            }
            if (row > 0) {
                outRect.top = spacing / 2;
            }
            if (row < totalRows - 1) {
                outRect.bottom = spacing / 2;
            }
        }
    }
}
