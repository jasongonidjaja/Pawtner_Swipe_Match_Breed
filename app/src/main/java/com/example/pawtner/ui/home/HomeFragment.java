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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pawtner.R;
import com.example.pawtner.databinding.FragmentHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final int SWIPE_THRESHOLD = 100;
    private float initialX, initialY;
    private boolean isAnimating = false;
    private Toast badgeToast;

    private class Pet {
        String name;
        int photo;
        String sex;
        String age;
        String color;
        String weight;
        String description;

        Pet(String name, int photo, String sex, String age, String color, String weight, String description) {
            this.name = name;
            this.photo = photo;
            this.sex = sex;
            this.age = age;
            this.color = color;
            this.weight = weight;
            this.description = description;
        }
    }

    private Pet[] pets = {
            new Pet("Yuki", R.drawable.pet1, "Female", "7", "Brown", "5 Kg",
                    "Fun loving Dalmatian who loves to cuddle"),
            new Pet("Buddy", R.drawable.pet4, "Female", "5",  "White", "4 kg",
                    "Playful and energetic dog"),
            new Pet("Max", R.drawable.pet6, "Female", "3", "Black", "5 kg",
                    "Pug who enjoys quiet evenings")
    };

    private int currentPetIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSwipeDetection();
        setupActionButtons();
        updateCurrentPet();
        setupProfileDropdown();
        setupFilterButton();
        setupInfoButton();
    }

    private void setupSwipeDetection() {
        binding.swipeLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isAnimating) return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        initialY = event.getY();
                        // Sembunyikan badge saat mulai swipe baru
                        binding.ivLikeBadge.setVisibility(View.GONE);
                        binding.ivDislikeBadge.setVisibility(View.GONE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float moveDeltaX = event.getX() - initialX; // Changed variable name
                        if (Math.abs(moveDeltaX) > 10) {
                            binding.petCard.setTranslationX(moveDeltaX);
                            binding.petCard.setRotation(moveDeltaX / 15f);

                            if (moveDeltaX > 0) {
                                showLikeBadge();
                            } else {
                                showDislikeBadge();
                            }

                            // Atur opacity berdasarkan seberapa jauh swipe-nya
                            float alpha = Math.min(1f, Math.abs(moveDeltaX) / SWIPE_THRESHOLD);
                            binding.ivLikeBadge.setAlpha(alpha);
                            binding.ivDislikeBadge.setAlpha(alpha);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        float finalX = event.getX();
                        float finalY = event.getY();
                        binding.ivLikeBadge.setVisibility(View.GONE);
                        binding.ivDislikeBadge.setVisibility(View.GONE);

                        float deltaX = finalX - initialX; // This is now the only declaration
                        float deltaY = finalY - initialY;

                        // Horizontal swipe detection
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                                if (deltaX > 0) {
                                    performLikeAction();
                                } else {
                                    performDislikeAction();
                                }
                            } else {
                                resetCardPosition();
                            }
                            return true;
                        }
                        // Vertical swipe up for info
                        else if (deltaY < -SWIPE_THRESHOLD) {
                            showToast("Show pet info");
                            showPetInfoBottomSheet();
                            return true;
                        } else {
                            resetCardPosition();
                        }
                        return false;
                }
                return false;
            }
        });
    }

    private void showLikeBadge() {
        binding.ivLikeBadge.setVisibility(View.VISIBLE);
        binding.ivDislikeBadge.setVisibility(View.GONE);

        // Tampilkan toast sementara
        if (badgeToast != null) badgeToast.cancel();
        badgeToast = Toast.makeText(requireContext(), "Like badge muncul", Toast.LENGTH_SHORT);
        badgeToast.show();
    }

    private void showDislikeBadge() {
        binding.ivLikeBadge.setVisibility(View.GONE);
        binding.ivDislikeBadge.setVisibility(View.VISIBLE);

        // Tampilkan toast sementara
        if (badgeToast != null) badgeToast.cancel();
        badgeToast = Toast.makeText(requireContext(), "Dislike badge muncul", Toast.LENGTH_SHORT);
        badgeToast.show();
    }
    private void setupActionButtons() {
        binding.btnLike.setOnClickListener(v -> {
            if (!isAnimating) performLikeAction();
        });

        binding.btnDislike.setOnClickListener(v -> {
            if (!isAnimating) performDislikeAction();
        });

        binding.btnInfo.setOnClickListener(v -> {
            showToast("Show pet info");
        });
    }

    private void performLikeAction() {
        isAnimating = true;
        showLikeBadge();
        binding.ivLikeBadge.setAlpha(1f);
//        Toast.makeText(requireContext(), "Liked " + pets[currentPetIndex].name + "! ❤️", Toast.LENGTH_SHORT).show();
        animateCardSwipe(true);
    }

    private void performDislikeAction() {
        isAnimating = true;
        showDislikeBadge();
        binding.ivDislikeBadge.setAlpha(1f);
//        Toast.makeText(requireContext(), "Disliked " + pets[currentPetIndex].name + "! ✖️", Toast.LENGTH_SHORT).show();
        animateCardSwipe(false);
    }

    private void animateCardSwipe(boolean isLike) {
        float translationX = isLike ? 1000f : -1000f;
        float rotation = isLike ? 20f : -20f;

        if (isLike) {
            showLikeBadge();
        } else {
            showDislikeBadge();
        }
        binding.ivLikeBadge.setAlpha(1f);
        binding.ivDislikeBadge.setAlpha(1f);

        binding.petCard.animate()
                .translationX(translationX)
                .rotation(rotation)
                .alpha(0.5f)
                .setDuration(300)
                .withEndAction(() -> {
                    // Batalkan toast saat animasi selesai
                    if (badgeToast != null) {
                        badgeToast.cancel();
                    }
                    binding.ivLikeBadge.setVisibility(View.GONE);
                    binding.ivDislikeBadge.setVisibility(View.GONE);
                    resetCardPosition();
                    moveToNextPet();
                    isAnimating = false;
                })
                .start();
    }

    private void resetCardPosition() {
        binding.ivLikeBadge.setVisibility(View.GONE);
        binding.ivDislikeBadge.setVisibility(View.GONE);
        binding.petCard.animate()
                .translationX(0)
                .rotation(0)
                .alpha(1f)
                .setDuration(200)
                .start();
    }

    private void moveToNextPet() {
        if (currentPetIndex < pets.length - 1) {
            currentPetIndex++;
        } else {
            currentPetIndex = 0;
            Toast.makeText(requireContext(), "Starting over with pets", Toast.LENGTH_SHORT).show();
        }
        updateCurrentPet();
    }

    private void updateCurrentPet() {
        Pet currentPet = pets[currentPetIndex];
        binding.imagePet.setImageResource(currentPet.photo);
        binding.petName.setText(currentPet.name);
        binding.petGender.setText(currentPet.sex);
        binding.petAge.setText(currentPet.age + " Y.O");
    }

    private void setupInfoButton() {
        binding.btnInfo.setOnClickListener(v -> showPetInfoBottomSheet());
    }

    private void setupProfileDropdown() {
        binding.cardProfileSelector.setOnClickListener(this::showPetDropdownPopup);
    }

    private void setupFilterButton() {
        binding.settingsButton.setOnClickListener(this::showFilterPopup);
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
        Button btnPetDetail = bottomSheetView.findViewById(R.id.btnPetDetail); // <-- ini ditambahkan

        Pet currentPet = pets[currentPetIndex];
        tvPetName.setText(currentPet.name);
        tvSex.setText(currentPet.sex);
        tvAge.setText(currentPet.age);
        tvColor.setText(currentPet.color);
        tvWeight.setText(currentPet.weight);
        tvDescription.setText(currentPet.description);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);

        // Navigasi saat tombol ditekan
        btnPetDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss(); // Tutup bottom sheet

                // Akses NavController dari activity dan navigate
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_home_to_petprofileother);
            }
        });

        // Atur behavior (opsional)
        bottomSheetDialog.show();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(800);
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        Log.d("SWIPE_DEBUG", message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (badgeToast != null) {
            badgeToast.cancel();
        }
        binding = null;
    }
}