package com.example.pawtner.ui.home;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pawtner.R;
import com.example.pawtner.databinding.FragmentHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private ImageView imagePet;
    private View tapLeft, tapRight;
    private LinearLayout storyIndicator;

    private class Pet {
        String name;
        int[] photos;
        int currentPhotoIndex = 0;
        String sex;
        String age;
        String color;
        String weight;
        String description;

        Pet(String name, int[] photos, String sex, String age, String color, String weight, String description) {
            this.name = name;
            this.photos = photos;
            this.sex = sex;
            this.age = age;
            this.color = color;
            this.weight = weight;
            this.description = description;
        }
    }

    private Pet[] pets = {
            new Pet("Yuki",
                    new int[]{R.drawable.pet1, R.drawable.pet2, R.drawable.pet3},
                    "Female", "7", "Brown", "5 kg",
                    "Yuki is a fun loving, sweet Dalmatian, rescued from overseas. Loves to cuddle and play with other dogs."),
            new Pet("Buddy",
                    new int[]{R.drawable.pet4, R.drawable.pet5},
                    "Female", "5", "White", "4 kg",
                    "Buddy is a playful and energetic dog who loves long walks and fetch games."),
            new Pet("Max",
                    new int[]{R.drawable.pet6, R.drawable.pet7, R.drawable.pet8, R.drawable.pet9},
                    "Female", "3", "Black", "5 kg",
                    "Max is a pug who enjoys quiet evenings and belly rubs.")
    };

    private int currentPetIndex = 0;
    private GestureDetector gestureDetector;
    private boolean isAnimating = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imagePet = binding.imagePet;
        tapLeft = binding.tapLeft;
        tapRight = binding.tapRight;
        storyIndicator = binding.storyIndicator;

        setupInfoButton();
        setupStoryNavigation();
        setupSwipeGestures();
        setupProfileDropdown();
        setupFilterButton();
        setupActionButtons();

        updateCurrentPhoto();
        updateProgressBar();
        updatePetInfo();
    }

    private void setupInfoButton() {
        binding.btnInfo.setOnClickListener(v -> showPetInfoBottomSheet());
    }

    private void setupStoryNavigation() {
        tapLeft.setOnClickListener(v -> {
            if (isAnimating) return;
            Pet currentPet = pets[currentPetIndex];
            if (currentPet.currentPhotoIndex > 0) {
                currentPet.currentPhotoIndex--;
                updateCurrentPhoto();
                updateProgressBar();
            }
        });

        tapRight.setOnClickListener(v -> {
            if (isAnimating) return;
            Pet currentPet = pets[currentPetIndex];
            if (currentPet.currentPhotoIndex < currentPet.photos.length - 1) {
                currentPet.currentPhotoIndex++;
                updateCurrentPhoto();
                updateProgressBar();
            } else {
                Toast.makeText(requireContext(), "No more photos for this pet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSwipeGestures() {
        gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (isAnimating) return false;

                float deltaX = e2.getX() - e1.getX();
                float deltaY = e2.getY() - e1.getY();

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (deltaX > 0) performLikeAction();
                        else performDislikeAction();
                        return true;
                    }
                } else {
                    if (Math.abs(deltaY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (deltaY < 0) showPetInfoBottomSheet();
                        return true;
                    }
                }
                return false;
            }
        });

        binding.petCard.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getX() - event.getHistoricalX(0);
                    float deltaY = event.getY() - event.getHistoricalY(0);
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        binding.petCard.setTranslationX(deltaX);
                        binding.petCard.setRotation(deltaX / 15f);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - event.getDownTime();
                    float dy = event.getY() - event.getDownTime();
                    if (dx > SWIPE_THRESHOLD) performLikeAction();
                    else if (dx < -SWIPE_THRESHOLD) performDislikeAction();
                    else if (dy < -SWIPE_THRESHOLD) showPetInfoBottomSheet();
                    else binding.petCard.animate().translationX(0).rotation(0).setDuration(200).start();
                    return true;
            }
            return false;
        });
    }

    private void setupActionButtons() {
        binding.btnLike.setOnClickListener(v -> { if (!isAnimating) performLikeAction(); });
        binding.btnDislike.setOnClickListener(v -> { if (!isAnimating) performDislikeAction(); });
    }

    private void performLikeAction() {
        Pet currentPet = pets[currentPetIndex];
        Toast.makeText(requireContext(), "Liked " + currentPet.name + "! ❤️", Toast.LENGTH_SHORT).show();
        animateCardSwipe(true);
    }

    private void performDislikeAction() {
        Pet currentPet = pets[currentPetIndex];
        Toast.makeText(requireContext(), "Disliked " + currentPet.name + "! ✖️", Toast.LENGTH_SHORT).show();
        animateCardSwipe(false);
    }

    private void animateCardSwipe(boolean isLike) {
        isAnimating = true;
        float translationX = isLike ? 1000f : -1000f;
        float rotation = isLike ? 20f : -20f;
        binding.petCard.animate()
                .translationX(translationX)
                .rotation(rotation)
                .alpha(0.5f)
                .setDuration(300)
                .withEndAction(() -> {
                    binding.petCard.setTranslationX(0);
                    binding.petCard.setRotation(0);
                    binding.petCard.setAlpha(1f);
                    moveToNextPet();
                    isAnimating = false;
                })
                .start();
    }

    private void moveToNextPet() {
        if (currentPetIndex < pets.length - 1) currentPetIndex++;
        else {
            currentPetIndex = 0;
            Toast.makeText(requireContext(), "Starting over with pets", Toast.LENGTH_SHORT).show();
        }
        pets[currentPetIndex].currentPhotoIndex = 0;
        updateCurrentPhoto();
        updateProgressBar();
        updatePetInfo();
    }

    private void setupProfileDropdown() {
        binding.cardProfileSelector.setOnClickListener(this::showPetDropdownPopup);
    }

    private void setupFilterButton() {
        binding.settingsButton.setOnClickListener(this::showFilterPopup);
    }

    private void updateCurrentPhoto() {
        Pet currentPet = pets[currentPetIndex];
        imagePet.setAlpha(0f);
        imagePet.setImageResource(currentPet.photos[currentPet.currentPhotoIndex]);
        imagePet.animate().alpha(1f).setDuration(200).start();
    }

    private void updateProgressBar() {
        storyIndicator.removeAllViews();
        Pet currentPet = pets[currentPetIndex];
        for (int i = 0; i < currentPet.photos.length; i++) {
            ProgressBar progressBar = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            progressBar.setMax(100);
            progressBar.setProgress(i == currentPet.currentPhotoIndex ? 100 : 0);
            storyIndicator.addView(progressBar);
        }
    }

    private void updatePetInfo() {
        Pet currentPet = pets[currentPetIndex];
        binding.petName.setText(currentPet.name);
        binding.petGender.setText(currentPet.sex);
        binding.petAge.setText(currentPet.age + " Y.O");
    }

    private void showPetDropdownPopup(View anchorView) {
        try {
            View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_pet_list, null);
            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int[] location = new int[2];
            anchorView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1] + anchorView.getHeight();
            popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error showing pet dropdown", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilterPopup(View anchorView) {
        try {
            View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter, null);
            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            popupView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int[] location = new int[2];
            anchorView.getLocationOnScreen(location);
            int marginRight = 16;
            int x = location[0] + anchorView.getWidth() - popupView.getMeasuredWidth() - marginRight;
            int y = location[1] + anchorView.getHeight();
            popupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, x, y);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error showing filter popup", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPetInfoBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_pet_info, null);
        TextView tvPetName = bottomSheetView.findViewById(R.id.tvPetName);
        TextView tvSex = bottomSheetView.findViewById(R.id.tvSex);
        TextView tvAge = bottomSheetView.findViewById(R.id.tvAge);
        TextView tvColor = bottomSheetView.findViewById(R.id.tvColor);
        TextView tvWeight = bottomSheetView.findViewById(R.id.tvWeight);
        TextView tvDescription = bottomSheetView.findViewById(R.id.tvDescription);

        Pet currentPet = pets[currentPetIndex];
        tvPetName.setText(currentPet.name);
        tvSex.setText(currentPet.sex);
        tvAge.setText(currentPet.age);
        tvColor.setText(currentPet.color);
        tvWeight.setText(currentPet.weight);
        tvDescription.setText(currentPet.description);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(800);
        bottomSheetDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
