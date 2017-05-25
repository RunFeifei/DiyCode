package com.example.root.okfit.view.slide;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.root.okfit.view.bubbleview.BubbleView;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.List;

/**
 * Created by PengFeifei on 17-5-25.
 */

public class SlideViewGroup extends ViewGroup implements View.OnTouchListener {

    private static final int VIEW_COUNT = 6;
    private static final int LEFT_RIGHT_MARGIN = 30;

    private BubbleView[] bubbles;

    private Scroller scroller;
    private float touchSlop;
    private float touchX;
    private boolean isMoving;
    private int totalX = 0;

    private OnBubbleClick onBubbleClick;

    public SlideViewGroup(Context context) {
        super(context);
        init(context, null);
    }

    public SlideViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        scroller = new Scroller(context, new BounceInterpolator());
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initBubbles(context);
        setClickable(true);
    }

    private void initBubbles(Context context) {
        bubbles = new BubbleView[VIEW_COUNT];
        int[] bubbleRadius={200,360,400,250,150,310};
        for (int i = 0; i < bubbles.length; i++) {
            bubbles[i] = new BubbleView(context);
            bubbles[i].setCircleColor(new RandomColor().randomColor());
            bubbles[i].setLayoutParams(new LinearLayout.LayoutParams(bubbleRadius[i], bubbleRadius[i]));
            bubbles[i].setClickable(true);
            bubbles[i].setOnTouchListener(this);
            this.addView(bubbles[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (bubbles.length < 6) {
            return;
        }
        Rect rect00 = getBounds(21, 129, bubbles[0].getMeasuredWidth(), bubbles[0].getMeasuredHeight());
        bubbles[0].layout(rect00.left, rect00.top, rect00.right, rect00.bottom);


        Rect rect01 = getBounds(-34, 417, bubbles[1].getMeasuredWidth(), bubbles[1].getMeasuredHeight());
        bubbles[1].layout(rect01.left, rect01.top, rect01.right, rect01.bottom);


        Rect rect02 = getBounds(226, 98, bubbles[2].getMeasuredWidth(), bubbles[2].getMeasuredHeight());
        bubbles[2].layout(rect02.left, rect02.top, rect02.right, rect02.bottom);


        Rect rect03 = getBounds(282, 652, bubbles[3].getMeasuredWidth(), bubbles[3].getMeasuredHeight());
        bubbles[3].layout(rect03.left, rect03.top, rect03.right, rect03.bottom);

        Rect rect04 = getBounds(586, 25, bubbles[4].getMeasuredWidth(), bubbles[4].getMeasuredHeight());
        bubbles[4].layout(rect04.left, rect04.top, rect04.right, rect04.bottom);


        Rect rect05 = getBounds(482, 383, bubbles[5].getMeasuredWidth(), bubbles[5].getMeasuredHeight());
        bubbles[5].layout(rect05.left, rect05.top, rect05.right, rect05.bottom);

    }

    private Rect getBounds(int left, int top, int width, int height) {
        return new Rect(left, top, left + width, top + height);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float lastX = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG-->", "ACTION_DOWN");
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                touchX = lastX;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("TAG-->", "ACTION_MOVE");
                float deltaX = (touchX - lastX) / 5;
                if (Math.abs(totalX) > LEFT_RIGHT_MARGIN) {
                    deltaX = deltaX > 0 ? 1 : -1;
                    totalX += (int) deltaX;
                }
                scrollBy((int) deltaX, 0);
                touchX = lastX;
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG-->", "ACTION_UP");
                scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 1000);
                invalidate();
                totalX = 0;
                isMoving = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_MOVE && isMoving) {
            Log.e("TAG-->", "onInterceptTouchEvent ACTION_MOVE && isMoving");
            return true;
        }

        final float lastX = event.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchX = lastX;
                isMoving = !scroller.isFinished();
                if (isMoving) {
                    return true;
                }
                Log.e("TAG-->", "onInterceptTouchEvent---DOWN" + isMoving);
                break;

            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(touchX - lastX);
                if (xDiff > touchSlop) {
                    Log.e("TAG-->", "xDiff > touchSlop--true");
                    isMoving = true;
                    return true;
                }
                break;
        }
        Log.e("TAG-->", "onInterceptTouchEvent" + isMoving);
        return super.onInterceptTouchEvent(event);
    }


    public void popUp() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        SpringChain springChain = SpringChain.create(40, 9, 50, 7);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = getChildAt(i);

            springChain.addSpring(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    view.setTranslationY((float) spring.getCurrentValue());
                }
            });
        }

        List<Spring> springs = springChain.getAllSprings();
        for (int i = 0; i < springs.size(); i++) {
            springs.get(i).setCurrentValue(screenHeight);
        }

        springChain.setControlSpringIndex(2).getControlSpring().setEndValue(0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!(v instanceof BubbleView)) {
            return false;
        }
        BubbleView bubbleView = (BubbleView) v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.e("TAG-->", "------------------------------------  MotionEvent.ACTION_DOWN");
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.e("TAG-->", "------------------------------------  MotionEvent.ACTION_MOVE");
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.e("TAG-->", "------------------------------------  MotionEvent.ACTION_UP");
                performBubbleClick(bubbleView.getCircleColor() + "", bubbles[0] == bubbleView ? "0" : "other");
                break;
            }
        }
        return false;
    }

    public interface OnBubbleClick {
        void onBubbleClick(String content, String num);
    }

    public void setOnBubbleClick(OnBubbleClick onBubbleClick) {
        this.onBubbleClick = onBubbleClick;
    }

    private void performBubbleClick(String content, String num) {
        if (onBubbleClick != null) {
            onBubbleClick.onBubbleClick(content, num);
        }
    }
}
