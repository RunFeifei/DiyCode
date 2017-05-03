package com.example.root.okfit.view.bubbleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.okfit.R;

import java.util.Formatter;

/**
 * Created by PengFeifei on 17-5-3.
 */

public class BubbleView extends LinearLayout {

    private TextView keyWordText;
    private TextView indexText;
    private Paint circlePaint;
    private int maxSize;
    private int pading = 20;

    private BubbleInfo bubbleInfo;
    private ViewConfiguration viewConfiguration;
    private VelocityTracker velocityTracker;
    private int lastX;
    private int lastY;

    private boolean isMoving;
    private MoveListener moveListener;

    private int index;

    public BubbleView(Context context) {
        super(context);
        init(context);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void initTextView() {
        keyWordText = (TextView) findViewById(R.id.textKeyword);
        keyWordText.setTextColor(Color.BLACK);
        keyWordText.setGravity(Gravity.CENTER);

        indexText = (TextView) findViewById(R.id.textIndex);
        indexText.setTextColor(Color.BLACK);
        indexText.setGravity(Gravity.CENTER);
    }

    private void initPaint() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_bubble, this);
        setWillNotDraw(false);
        initTextView();
        initPaint();
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);
        viewConfiguration = ViewConfiguration.get(context);
    }

    public void setText(String keyWord, String indexInput) {
        keyWordText.setText(keyWord);
        indexText.setText(indexInput);
        try {
            index = Integer.parseInt(indexInput.toString());
        } catch (NumberFormatException ex) {
            index = 0;
        }
    }

    public void setCircleColor(@ColorInt int color) {
        circlePaint.setColor(color);
    }

    public void setBubbleInfo(BubbleInfo bubbleInfo) {
        this.bubbleInfo = bubbleInfo;
    }

    public BubbleInfo getBubbleInfo() {
        return bubbleInfo;
    }

    public int getIndex() {
        return index;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);

        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();

        int[] location = new int[2];
        getLocationOnScreen(location);
        Rect rect = new Rect(location[0], location[1], location[0] + getWidth(), location[1] + getHeight());

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = rawX;
                lastY = rawY;
                if (rect.contains(lastX, lastY)) {
                    isMoving = true;
                }

                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);

                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(10);
                if (isMoving) {
                    int deltaX = rawX - lastX;
                    int deltaY = rawY - lastY;
                    if (Math.abs(deltaX) > viewConfiguration.getScaledTouchSlop()
                            || Math.abs(deltaY) > viewConfiguration.getScaledTouchSlop()) {
                        if (moveListener != null) {
                            int centerX = getLeft() + getWidth() / 2;
                            int centerY = getTop() + getHeight() / 2;
                            float velocityX = velocityTracker.getXVelocity(pointerId);
                            float velocityY = velocityTracker.getYVelocity(pointerId);
                            double velocity = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
                            moveListener.onMove(bubbleInfo, centerX, centerY, deltaX, deltaY, velocity);
                        }
                        lastX = rawX;
                        lastY = rawY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                isMoving = false;
                velocityTracker.recycle();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.drawCircle(width / 2, height / 2, (Math.max(width, height)) / 2, circlePaint);
        super.onDraw(canvas);
    }


    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int minSize = Math.max(getTextWidths(keyWordText),getTextWidths(indexText));
        minSize+=pading*5;
        int size = Math.max(minSize, maxSize);
        setMeasuredDimension(size, size);
        Log("size->%1$s<<<%2$s",minSize+"",maxSize+"");
    }*/

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public interface MoveListener {
        void onMove(BubbleInfo bubbleInfo, int centerX, int centerY, int deltaX, int deltaY, double velocity);
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        resetViewSize();
    }

    private static void Log(String format, Object... args) {
        Log.e("TAG-->", new Formatter().format(format, args).toString());
    }

    private int getTextWidths(TextView textView) {
        return (int) textView.getPaint().measureText(textView.getText().toString());
    }

    private void resetViewSize() {
        int minSize = Math.max(getTextWidths(keyWordText), getTextWidths(indexText));
        int size = Math.max(minSize, maxSize);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        setLayoutParams(layoutParams);
        Log("size->%1$s<<<%2$s", maxSize + "", size + "");
    }
}
