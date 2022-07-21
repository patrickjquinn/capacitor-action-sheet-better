package com.capacitorjs.plugins.actionsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.view.KeyEvent;
import android.graphics.Typeface;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.getcapacitor.Logger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ActionSheet extends BottomSheetDialogFragment {

    public interface OnSelectListener {
        void onSelect(int index);
    }

    public interface OnCancelListener {
        void onCancel();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        this.cancelListener.onCancel();
    }

    private String title;
    private ActionSheetOption[] options;

    private OnSelectListener listener;
    private OnCancelListener cancelListener;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptions(ActionSheetOption[] options) {
        this.options = options;
    }

    public void setOnSelectedListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public void setOnCancelListener(OnCancelListener listener) {
        this.cancelListener = listener;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
    };

    @Override
    @SuppressLint("RestrictedApi")
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        if (options == null) {
            return;
        }

        final float scale = getResources().getDisplayMetrics().density;

        float layoutPaddingDp16 = 16.0f;
        float layoutPaddingDp12 = 12.0f;
        float layoutPaddingDp8 = 8.0f;
        int layoutPaddingPx16 = (int) (layoutPaddingDp16 * scale + 0.5f);
        int layoutPaddingPx12 = (int) (layoutPaddingDp12 * scale + 0.5f);
        int layoutPaddingPx8 = (int) (layoutPaddingDp8 * scale + 0.5f);

        CoordinatorLayout parentLayout = new CoordinatorLayout(getContext());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(layoutPaddingPx16, layoutPaddingPx16, layoutPaddingPx16, layoutPaddingPx16);
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
            LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);
            tv.setTextColor(Color.parseColor("#000000"));

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
                    Logger.debug("CliCKED: " + optionIndex);

                    if (listener != null) {
                        listener.onSelect(optionIndex);
                    }
                }
            );
            layout.addView(tv);
        }

        parentLayout.addView(layout.getRootView());

        dialog.setOnKeyListener((arg0, keyCode, event) -> {            
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                dismiss();                
                return true;
            }
            return true;
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(parentLayout.getRootView());
        

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) parentLayout.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).addBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
