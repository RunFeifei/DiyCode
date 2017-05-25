package com.example.root.okfit.view.slide;

/**
 * Created by PengFeifei on 17-5-25.
 */


        import android.content.Context;
        import android.graphics.Rect;
        import android.support.v4.view.ViewCompat;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.VelocityTracker;
        import android.view.View;
        import android.view.ViewConfiguration;
        import android.view.ViewGroup;
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

public class ScrollLayout extends ViewGroup {

    private static final int VIEW_COUNT = 6;

    private BubbleView[] bubbles = new BubbleView[VIEW_COUNT];

    private Context context;
    private Scroller scroller;
    private VelocityTracker velocityTracker;
    /*最小滑动间距*/
    private float touchSlop;
    /*最大滑动速度*/
    private int maxVelocity;
    /*最小滑动速度*/
    private int minVelocity;
    private float touchX;
    private boolean isMoving;


    public ScrollLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.context = context;
        scroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        initBubbles(context);
        setClickable(true);
    }

    private void initBubbles(Context context) {
       /* for (BubbleView bubbleView : bubbles) {
            bubbleView = new BubbleView(context);
            bubbleView.setCircleColor(new RandomColor().randomColor());
        }*/
        if (bubbles.length < 6) {
            return;
        }
        bubbles[0] = new BubbleView(context);
        bubbles[0].setCircleColor(new RandomColor().randomColor());
        bubbles[0].setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        this.addView(bubbles[0]);


        bubbles[1] = new BubbleView(context);
        bubbles[1].setCircleColor(new RandomColor().randomColor());
        bubbles[1].setLayoutParams(new LinearLayout.LayoutParams(360, 360));
        bubbles[1].setClickable(true);
        this.addView(bubbles[1]);


        bubbles[2] = new BubbleView(context);
        bubbles[2].setCircleColor(new RandomColor().randomColor());
        bubbles[2].setLayoutParams(new LinearLayout.LayoutParams(400, 400));
        bubbles[2].setClickable(true);
        this.addView(bubbles[2]);


        bubbles[3] = new BubbleView(context);
        bubbles[3].setCircleColor(new RandomColor().randomColor());
        bubbles[3].setLayoutParams(new LinearLayout.LayoutParams(250, 250));
        bubbles[3].setClickable(true);
        this.addView(bubbles[3]);


        bubbles[4] = new BubbleView(context);
        bubbles[4].setCircleColor(new RandomColor().randomColor());
        bubbles[4].setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        bubbles[4].setClickable(true);
        this.addView(bubbles[4]);


        bubbles[5] = new BubbleView(context);
        bubbles[5].setCircleColor(new RandomColor().randomColor());
        bubbles[5].setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        bubbles[5].setClickable(true);
        this.addView(bubbles[5]);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
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

    private void popUp() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        SpringChain springChain = SpringChain.create(40, 6, 50, 7);

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
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    private void initVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
            velocityTracker.addMovement(event);
        }
    }

    private void destroyVelocityTracker() {
        if (null != velocityTracker) {
            velocityTracker.clear();
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTracker(event);
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
                int deltaX = (int) (touchX - lastX);
                scrollBy(deltaX, 0);
                touchX = lastX;
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = this.velocityTracker;
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                int velocityX = (int) velocityTracker.getXVelocity();
                Log.e("TAG----->", "ACTION_UP" + velocityX + ">" + minVelocity);
                if (velocityX > minVelocity) {
                    scroller.startScroll(getScrollX(), 0, -(int) Math.abs(touchX - lastX), 0, 1000);
                    invalidate();
                    // 向左移动
                } else if (velocityX < -minVelocity) {
                    scroller.startScroll(getScrollX(), 0, (int) Math.abs(touchX - lastX), 0, 1000);
                    invalidate();
                    // 向右移动
                }
                isMoving = false;
                destroyVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("TAG-->", "ACTION_CANCEL");
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
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(touchX - lastX);
                if (xDiff > touchSlop) {
                    Log.e("TAG-->", "onInterceptTouchEvent xDiff > touchSlop");
                    isMoving = true;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                touchX = lastX;
                isMoving = !scroller.isFinished();
                Log.e("TAG-->", "onInterceptTouchEvent ACTION_DOWN" + isMoving);
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isMoving = false;
                Log.e("TAG-->", "onInterceptTouchEvent ACTION_UP" + isMoving);
                break;
        }
        Log.e("TAG-->", "onInterceptTouchEvent" + isMoving);

        return isMoving;
    }
}
