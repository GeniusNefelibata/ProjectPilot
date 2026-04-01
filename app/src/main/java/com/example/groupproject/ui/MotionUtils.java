package com.example.groupproject.ui;

import android.view.MotionEvent;
import android.view.View;

public class MotionUtils {

    public static void applyPressAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().cancel();
                    v.animate()
                            .alpha(0.85f)
                            .setDuration(80)
                            .start();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_MOVE:
                    v.animate().cancel();
                    v.animate()
                            .alpha(1f)
                            .setDuration(140)
                            .start();
                    break;
            }
            return false;
        });
    }
}