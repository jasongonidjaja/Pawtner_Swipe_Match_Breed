package com.example.pawtner.ui.home;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawtner.R;
import com.example.pawtner.adapters.PostOtherAdapter; // ✅ GANTI di sini
import com.example.pawtner.model.PostItem;
import com.example.pawtner.ui.mypets.PetProfileFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PetProfileOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetProfileOtherFragment extends Fragment {

    private RecyclerView postsRecyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PetProfileOtherFragment() {
        // Required empty public constructor
    }

    public static PetProfileOtherFragment newInstance(String param1, String param2) {
        PetProfileOtherFragment fragment = new PetProfileOtherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet_profile_other, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        // Siapkan dummy post
        List<PostItem> postItemList = getDummyPosts();

        // Setup RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        postsRecyclerView.setLayoutManager(layoutManager);
        postsRecyclerView.setHasFixedSize(true);
        postsRecyclerView.addItemDecoration(new PetProfileFragment.GridSpacingItemDecoration(3, 2));

        PostOtherAdapter adapter = new PostOtherAdapter(postItemList); // ✅ GANTI di sini
        postsRecyclerView.setAdapter(adapter);
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
