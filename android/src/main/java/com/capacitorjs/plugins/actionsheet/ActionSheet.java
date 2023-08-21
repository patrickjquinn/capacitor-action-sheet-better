package com.capacitorjs.plugins.actionsheet;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.getcapacitor.Logger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ActionSheet extends BottomSheetDialogFragment {

    public interface OnSelectListener {
        void onSelect(int index);
    }

    private String title;
    private ActionSheetOption[] options;

    private OnSelectListener listener;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptions(ActionSheetOption[] options) {
        this.options = options;
    }

    public void setOnSelectedListener(OnSelectListener listener) {
        this.listener = listener;
    }

    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final float scale = getResources().getDisplayMetrics().density;
        int layoutPaddingPx16 = (int) (16 * scale + 0.5f);
        int layoutPaddingPx12 = (int) (12 * scale + 0.5f);
        int layoutPaddingPx8 = (int) (8 * scale + 0.5f);

        CoordinatorLayout parentLayout = new CoordinatorLayout(requireContext());
        parentLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
        parentLayout.setBackgroundColor(0x00000000);  // Fully transparent color

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
        layout.setPadding(layoutPaddingPx16, layoutPaddingPx16, layoutPaddingPx16, layoutPaddingPx16);
        layout.setBackgroundColor(Color.BLACK);

        if (title != null) {
            TextView ttv = new TextView(getContext());
            ttv.setTextColor(Color.parseColor("#757575"));
            ttv.setPadding(layoutPaddingPx8, layoutPaddingPx8, layoutPaddingPx8, layoutPaddingPx8);
            ttv.setText(title);
            layout.addView(ttv);
        }

        for (int i = 0; i < options.length; i++) {
            final int optionIndex = i;
            final String optionStyle = options[i].getStyle();

            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv.setTextColor(Color.WHITE);  // Set the default text color to white

            if (optionStyle != null) {
                if (optionStyle.equals("CANCEL")) {
                    tv.setTypeface(null,Typeface.BOLD);
                } else if (optionStyle.equals("DESTRUCTIVE")) {
                    tv.setTextColor(Color.parseColor("#f44336"));
                }
            }

            tv.setLayoutParams(params);
            tv.setPadding(layoutPaddingPx12, layoutPaddingPx12, layoutPaddingPx12, layoutPaddingPx12);
            tv.setText(options[i].getTitle());
            tv.setOnClickListener(
                    view -> {
                        Logger.debug("Clicked: " + optionIndex);
                        if (listener != null) {
                            listener.onSelect(optionIndex);
                        }
                    }
            );
            layout.addView(tv);
        }

        parentLayout.addView(layout);

        return parentLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View bottomSheetInternal = view.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheetInternal != null) {
            bottomSheetInternal.setBackgroundColor(Color.TRANSPARENT);
        }

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior<?> behavior = params.getBehavior();
        if (behavior instanceof BottomSheetBehavior<?>) {
            BottomSheetBehavior<View> bottomSheetBehavior = (BottomSheetBehavior<View>) behavior;
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.addBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        if (getDialog() != null) {
            getDialog().setCancelable(true);
            getDialog().setCanceledOnTouchOutside(true);
        }
    }
}