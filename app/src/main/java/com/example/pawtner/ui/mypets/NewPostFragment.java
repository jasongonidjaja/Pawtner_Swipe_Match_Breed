package com.example.pawtner.ui.mypets;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawtner.R;
import com.example.pawtner.adapters.GalleryAdapterPet;
import com.example.pawtner.model.PostItem;
import com.example.pawtner.utils.GridSpacingItemDecoration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewPostFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private RecyclerView galleryRecyclerView;
    private ImageView imagePreview;
    private ScrollView scrollView;
    private Uri photoUri;

    public NewPostFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newpost, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        imagePreview = view.findViewById(R.id.imagePreview);
        scrollView = view.findViewById(R.id.scrollViewNewPost);

        ImageView btnClose = view.findViewById(R.id.btnClose);
        TextView btnNext = view.findViewById(R.id.btnNext);

        NavController navController = Navigation.findNavController(view);
        btnClose.setOnClickListener(v -> navController.popBackStack());
        btnNext.setOnClickListener(v -> navController.navigate(R.id.action_newPostFragment_to_captionFragment));

        List<PostItem> dummyImages = new ArrayList<>();
        dummyImages.add(new PostItem(R.drawable.cat_sample));
        dummyImages.add(new PostItem(R.drawable.cat_sample));
        dummyImages.add(new PostItem(R.drawable.cat_sample));
        dummyImages.add(new PostItem(R.drawable.cat_sample));

        GalleryAdapterPet adapter = new GalleryAdapterPet(dummyImages, new GalleryAdapterPet.OnItemClickListener() {
            @Override
            public void onCameraClick() {
                Toast.makeText(getContext(), "Camera clicked", Toast.LENGTH_SHORT).show();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Toast.makeText(getContext(), "Failed to create image file", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(
                                requireContext(),
                                requireContext().getPackageName() + ".provider",
                                photoFile
                        );

                        Log.d("Camera", "Photo URI: " + photoUri);

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                            Log.d("Camera", "Launching camera intent...");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to launch camera", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "photoFile is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No camera app found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onImageClick(PostItem item) {
                imagePreview.setImageResource(item.getImageResId());
                scrollView.post(() -> scrollView.smoothScrollTo(0, 0));
            }
        });

        int spanCount = 3;
        int spacing = dpToPx(2);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        galleryRecyclerView.setLayoutManager(layoutManager);
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setPadding(0, 0, 0, 0);
        galleryRecyclerView.setClipToPadding(false);
        galleryRecyclerView.setClipChildren(false);
        galleryRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));
        galleryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Camera", "onActivityResult called");

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imagePreview.setImageURI(photoUri);
            scrollView.post(() -> scrollView.smoothScrollTo(0, 0));
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (storageDir == null) {
            Toast.makeText(getContext(), "Storage directory is null", Toast.LENGTH_SHORT).show();
            return null;
        }

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
