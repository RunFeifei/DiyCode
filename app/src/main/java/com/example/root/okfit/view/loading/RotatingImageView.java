package com.example.root.okfit.view.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class RotatingImageView extends AppCompatImageView {

    private float rotateDegrees;
    private boolean needRefresh;
    private Runnable drawRunnable;

    public RotatingImageView(Context context) {
        super(context);
        init();
    }

    public RotatingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setImageResource(com.example.root.okfit.R.drawable.icon_loading);
        setFocusable(true);
        setFocusableInTouchMode(true);
        initRunable();
    }

    private void initRunable() {
        final int frameTime = 50;
        drawRunnable = new Runnable() {
            @Override
            public void run() {
                rotateDegrees += 30;
                rotateDegrees = rotateDegrees < 360 ? rotateDegrees : rotateDegrees - 360;
                invalidate();
                if (needRefresh) {
                    postDelayed(this, frameTime);
                }
            }
        };
    }

    private void doRotate() {
        needRefresh = true;
        if (drawRunnable == null) {
            initRunable();
        }
        post(drawRunnable);
    }

    private void stopRotate() {
        needRefresh = false;
        drawRunnable = null;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(rotateDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        doRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopRotate();
        super.onDetachedFromWindow();
    }
}
