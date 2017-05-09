package com.example.root.okfit.view.bubbleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.okfit.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BubbleLayout extends ViewGroup implements BubbleView.MoveListener {

    public static final int DEFAULT_PADDING = 10;
    public static final int DEFAULT_MIN_SPEED = 200;
    public static final int DEFAULT_MAX_SPEED = 500;

    private int padding = DEFAULT_PADDING;
    private int minPxPerTenMilliseconds = DEFAULT_MIN_SPEED;
    private int maxPxPerTenMilliseconds = DEFAULT_MAX_SPEED;

    private List<BubbleInfo> mBubbleInfos = new ArrayList<>();
    private Timer mTimer;

    private MyHandler mHandler;

    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.bubbleview_BubbleLayout, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int id = typedArray.getIndex(i);
            if (id == R.styleable.bubbleview_BubbleLayout_bubbleview_minSpeed) {
                minPxPerTenMilliseconds = typedArray.getDimensionPixelSize(id, DEFAULT_MIN_SPEED);
            } else if (id == R.styleable.bubbleview_BubbleLayout_bubbleview_maxSpeed) {
                maxPxPerTenMilliseconds = typedArray.getDimensionPixelSize(id, DEFAULT_MAX_SPEED);
            } else if (id == R.styleable.bubbleview_BubbleLayout_bubbleview_padding) {
                padding = typedArray.getDimensionPixelSize(id, DEFAULT_PADDING);
            }
        }
        typedArray.recycle();

        mHandler = new MyHandler(this);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setupBubbleInfoList();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        List<BubbleView> sortResult = sort();
        for (int i = 0; i < sortResult.size(); i++) {
            View child = sortResult.get(i);

            BubbleInfo bubbleInfo = getBubbleInfoByView(child);
            if (bubbleInfo == null) {
                return;
            }
            BubbleView bubbleView = (BubbleView) child;
            bubbleView.setBubbleInfo(bubbleInfo);
            bubbleView.setMoveListener(this);
            if (i == 0) {
                Rect newRect = getBounds(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
                child.layout(newRect.left, newRect.top, newRect.right, newRect.bottom);
                bubbleInfo.setRect(newRect);
            }
            if (i == 1) {
                Rect lastRect = sortResult.get(i - 1).getBubbleInfo().getRect();
                Rect newRect = getBounds(2 * padding + lastRect.height(), lastRect.height() / 2 + 2 * padding, child.getMeasuredWidth(), child
                        .getMeasuredHeight());
                child.layout(newRect.left, newRect.top, newRect.right, newRect.bottom);
                bubbleInfo.setRect(newRect);
            }
            if (i == 2) {
                Rect lastRect = sortResult.get(i - 1).getBubbleInfo().getRect();
                Rect newRect = getBounds(padding + lastRect.height(), lastRect.bottom, child.getMeasuredWidth(), child.getMeasuredHeight());
                child.layout(newRect.left, newRect.top, newRect.right, newRect.bottom);
                bubbleInfo.setRect(newRect);
            }
            if (i == 3) {
                Rect lastRect = sortResult.get(i - 1).getBubbleInfo().getRect();
                Rect newRect = getBounds(0, lastRect.bottom - lastRect.height() - padding * 2, child.getMeasuredWidth(), child
                        .getMeasuredHeight());
                child.layout(newRect.left, newRect.top, newRect.right, newRect.bottom);
                bubbleInfo.setRect(newRect);
            }
            if (i == 4) {
                Rect lastRect = sortResult.get(i - 1).getBubbleInfo().getRect();
                Rect newRect = getBounds(lastRect.centerX() - padding, getMeasuredHeight() - bubbleView.getMeasuredWidth(), child.getMeasuredWidth(), child
                        .getMeasuredHeight());
                child.layout(newRect.left, newRect.top, newRect.right, newRect.bottom);
                bubbleInfo.setRect(newRect);
            }
            if (i == 5) {
                Rect lastRect = sortResult.get(2).getBubbleInfo().getRect();
                Rect newRect = getBounds(getMeasuredWidth() - padding - child.getMeasuredWidth(), lastRect.bottom - padding, child.getMeasuredWidth(), child
                        .getMeasuredHeight());
                child.layout(newRect.left, newRect.top, newRect.right, newRect.bottom);
                bubbleInfo.setRect(newRect);
            }
        }
    }

    /**
     * 计算球心
     *
     * @param amount 当前速度
     * @param x      当前球心X
     * @param y      当前球心Y
     * @param radian 弧度
     */
    private int[] getRadianPoint(int amount, int x, int y, double radian) {
        int resultX = x + (int) (amount * Math.cos(radian));
        int resultY = y + (int) (amount * Math.sin(radian));

        return new int[] {resultX, resultY};
    }

    /**
     * 初始化每个Bubble的Info
     */
    private void setupBubbleInfoList() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            BubbleInfo info = new BubbleInfo();
            info.setRadians(getRandomRadians());
            info.setSpeed(getRandomBetween(minPxPerTenMilliseconds, maxPxPerTenMilliseconds));
            info.setOldSpeed(info.getSpeed());
            info.setIndex(i);
            mBubbleInfos.add(info);
        }
    }

    /**
     * 根据BubbleInfo得到每个BubbleView
     */
    private BubbleInfo getBubbleInfoByView(View child) {
        for (BubbleInfo info : mBubbleInfos) {
            BubbleView bubbleView = (BubbleView) getChildAt(info.getIndex());
            if (bubbleView == child) {
                return info;
            }
        }
        return null;
    }

    private int getRandomBetween(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }


    private Rect getBounds(int left, int top, int width, int height) {
        return new Rect(left, top, left + width, top + height);
    }


    /**
     * 两圆是否相切
     */
    private boolean isCircleOverlap(Rect rect0, Rect rect1) {
        int x0 = rect0.centerX();
        int y0 = rect0.centerY();
        int r0 = rect0.width() / 2;
        int x1 = rect1.centerX();
        int y1 = rect1.centerY();
        int r1 = rect1.width() / 2;

        return Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2) <= Math.pow(r0 + r1, 2);
    }

    /**
     * 按照热点的程度进行排序
     */
    private List<BubbleView> sort() {
        List<BubbleView> allBubbleChild = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view != null && view instanceof BubbleView) {
                allBubbleChild.add((BubbleView) view);
            }
        }
        //按照宽度大小降序
        Collections.sort(allBubbleChild, new Comparator<BubbleView>() {
            @Override
            public int compare(BubbleView o1, BubbleView o2) {
                return o1.getIndex() > o2.getIndex() ? -1 : 1;
            }
        });
        initViewSize(allBubbleChild);
        return allBubbleChild;
    }

    /**
     * @param allBubbleChild 经过排序的views
     */
    private void initViewSize(List<BubbleView> allBubbleChild) {
        if (allBubbleChild == null) {
            return;
        }
        int size = allBubbleChild.size();
        if (size < 0) {
            return;
        }
        float[] factors = new float[size];
        factors[0] = 1;
        for (int i = 1; i < size; i++) {
            float factor = i < 2 ? 0.75f : 0.85f;
            factors[i] = factor * factors[i - 1];
        }
        int maxDiameter = (int) (getMeasuredWidth() / 1.8);
        for (int i = 0; i < size; i++) {
            int diameter = (int) (maxDiameter * factors[i]);
            allBubbleChild.get(i).setMaxSize(diameter);
            //            LayoutParams layoutParams = new LinearLayout.LayoutParams(diameter, diameter);
            //            allBubbleChild.get(i).setLayoutParams(layoutParams);
        }
    }

    //刷新间隔
    private void startAnimate() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
                this.cancel();
            }
        }, 15);
    }

    private static class MyHandler extends Handler {
        private WeakReference<BubbleLayout> layoutWeakReference;

        public MyHandler(BubbleLayout layout) {
            this.layoutWeakReference = new WeakReference<>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            BubbleLayout layout = layoutWeakReference.get();
            if (layout == null) {
                return;
            }
            int count = layout.getChildCount();
            for (int i = 0; i < count && layout.mBubbleInfos.size() > 0; i++) {

                BubbleInfo bubbleInfo = layout.mBubbleInfos.get(i);

                List<BubbleInfo> overlapList = layout.hasOverlap(bubbleInfo);

                Point overlapPoint = layout.ifOverlapBounds(bubbleInfo);
                if (overlapPoint != null) {
                    layout.reverseIfOverlapBounds(bubbleInfo);
                } else if (overlapList.size() > 0) {
                    layout.dealWithOverlap();
                }

                layout.moveBubble(bubbleInfo);
            }
            layout.startAnimate();
        }
    }

    private void moveBubble(BubbleInfo info) {
        View child = getChildAt(info.getIndex());
        int[] center = getRadianPoint(info.getSpeed(), child.getLeft() + child.getWidth() / 2, child.getTop() + child.getWidth() / 2, info.getRadians());
        Rect rect = getBounds(center[0] - child.getWidth() / 2, center[1] - child.getWidth() / 2, child.getMeasuredWidth(), child.getMeasuredHeight());
        info.setRect(rect);
        child.layout(rect.left, rect.top, rect.right, rect.bottom);
    }

    private void slowerBubbleIfNeeded(BubbleInfo info) {
        if (info.getOldSpeed() > 0 && info.getSpeed() > info.getOldSpeed()) {
            info.setSpeed(info.getSpeed() - 1);
        }
    }

    /**
     * @param bubbleInfo  当前圆
     * @param child       当前view
     * @param overlapRect 相切的其他圆
     * @return
     */
    private BubbleInfo getNewMoveInfo(BubbleInfo bubbleInfo, View child, List<BubbleInfo> overlapRect) {
        Rect oldRect = bubbleInfo.getRect();

        Point cooperate = getCooperatePoint(overlapRect);
        Point overlapBoundPoint = ifOverlapBounds(bubbleInfo);
        if (overlapBoundPoint != null) {
            cooperate = new Point((cooperate.x + overlapBoundPoint.x) / 2, (cooperate.y + overlapBoundPoint.y) / 2);
        }

        float overlapRadians = (float) getRadians(new float[] {oldRect.exactCenterX(), oldRect.exactCenterY()}, new float[] {cooperate.x, cooperate.y});
        double reverseRadians = getReverseRadians(overlapRadians);

        int[] centerNew = getRadianPoint(bubbleInfo.getSpeed(), child.getLeft() + child.getWidth() / 2, child.getTop() + child.getWidth() / 2, reverseRadians);
        Rect rectNew = getBounds(centerNew[0] - child.getWidth() / 2, centerNew[1] - child.getWidth() / 2, child.getMeasuredWidth(), child.getMeasuredHeight());

        BubbleInfo bubbleInfoNew = new BubbleInfo();
        bubbleInfoNew.setIndex(bubbleInfo.getIndex());
        bubbleInfoNew.setSpeed(bubbleInfo.getSpeed());
        bubbleInfoNew.setOldSpeed(bubbleInfo.getOldSpeed());
        bubbleInfoNew.setRadians(reverseRadians);
        bubbleInfoNew.setRect(rectNew);

        return bubbleInfoNew;

    }

    /**
     * 处理重合&相切Case
     */
    private void dealWithOverlap() {
        List<BubbleInfo> tempBubbleInfoList = new ArrayList<>();
        for (BubbleInfo info : mBubbleInfos) {
            //所有和当前圆有重合&相切的圆
            List<BubbleInfo> overlapList = hasOverlap(info);
            if (overlapList.size() > 0) {
                BubbleInfo bubbleInfoNew = getNewMoveInfo(info, getChildAt(info.getIndex()), overlapList);
                slowerBubbleIfNeeded(bubbleInfoNew);
                tempBubbleInfoList.add(bubbleInfoNew);
            }
        }

        for (int i = 0; i < tempBubbleInfoList.size(); i++) {
            BubbleInfo tempBubbleInfo = tempBubbleInfoList.get(i);
            BubbleInfo oldBubbleInfo = mBubbleInfos.get(tempBubbleInfo.getIndex());
            oldBubbleInfo.setRadians(tempBubbleInfo.getRadians());
            oldBubbleInfo.setOldSpeed(tempBubbleInfo.getOldSpeed());
            oldBubbleInfo.setIndex(tempBubbleInfo.getIndex());
            oldBubbleInfo.setSpeed(tempBubbleInfo.getSpeed());
            oldBubbleInfo.setRect(tempBubbleInfo.getRect());
        }

    }

    private Point getCooperatePoint(List<BubbleInfo> overlapRect) {
        int totalX = 0;
        int totalY = 0;
        for (BubbleInfo info : overlapRect) {
            totalX += info.getRect().exactCenterX();
            totalY += info.getRect().exactCenterY();
        }

        return new Point(totalX / overlapRect.size(), totalY / overlapRect.size());
    }

    /**
     * 弧度反向
     *
     * @param radians
     * @return
     */
    private double getReverseRadians(double radians) {
        double reverseRadians;
        if (radians > Math.PI) {
            reverseRadians = radians - Math.PI;
        } else {
            reverseRadians = radians + Math.PI;
        }

        return reverseRadians;
    }

    /**
     * @return 所有和当前圆有重合&相切的圆
     */
    private List<BubbleInfo> hasOverlap(BubbleInfo bubbleInfo) {
        int count = mBubbleInfos.size();
        List<BubbleInfo> overlapList = new ArrayList<>();
        if (bubbleInfo.getRect() != null) {
            for (int i = 0; i < count; i++) {
                BubbleInfo otherInfo = mBubbleInfos.get(i);
                if (i != bubbleInfo.getIndex()) {
                    if (otherInfo.getRect() != null) {
                        if (isCircleOverlap(otherInfo.getRect(), bubbleInfo.getRect())) {
                            overlapList.add(otherInfo);
                        }
                    }
                }
            }
        }
        return overlapList;
    }

    /**
     * 如果碰撞,进行反向
     *
     * @param bubbleInfo
     */
    private void reverseIfOverlapBounds(BubbleInfo bubbleInfo) {
        Point overlapPoint = ifOverlapBounds(bubbleInfo);
        Rect totalRect = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        if (overlapPoint != null) {
            float overlapRadians = (float) getRadians(new float[] {bubbleInfo.getRect().exactCenterX(), bubbleInfo.getRect().exactCenterY()}, new float[]
                    {overlapPoint.x, overlapPoint.y});
            if (!totalRect.contains(bubbleInfo.getRect().centerX(), bubbleInfo.getRect().centerY())) {
                bubbleInfo.setRadians(overlapRadians);
            } else {
                double reverseRadians = getReverseRadians(overlapRadians);
                bubbleInfo.setRadians(reverseRadians);
            }
            slowerBubbleIfNeeded(bubbleInfo);
        }
    }

    /**
     * 判断是否碰撞边界,如果没有碰撞边界返回null,否则返回
     *
     * @param bubbleInfo 当前圆的Info
     */
    private Point ifOverlapBounds(BubbleInfo bubbleInfo) {
        Rect rect = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getMeasuredHeight());
        if (bubbleInfo.getRect() != null) {
            Rect bubbleRect = bubbleInfo.getRect();
            List<Point> overlapPoints = new ArrayList<>();
            if (rect.left >= bubbleRect.left) {
                Point overlapPoint = new Point(rect.left, bubbleRect.centerY());
                overlapPoints.add(overlapPoint);
            }
            // NOTE: 17-5-3 0 replace rect.top
            if (0 >= bubbleRect.top) {
                Point overlapPoint = new Point(bubbleRect.centerX(), rect.top);
                overlapPoints.add(overlapPoint);
            }
            if (rect.right <= bubbleRect.right) {
                Point overlapPoint = new Point(rect.right, bubbleRect.centerY());
                overlapPoints.add(overlapPoint);
            }
            if (rect.bottom <= bubbleRect.bottom) {
                Point overlapPoint = new Point(bubbleRect.centerX(), rect.bottom);
                overlapPoints.add(overlapPoint);
            }

            if (overlapPoints.size() > 0) {
                int totalX = 0;
                int totalY = 0;
                for (Point point : overlapPoints) {
                    totalX += point.x;
                    totalY += point.y;
                }
                //当前圆的
                return new Point(totalX / overlapPoints.size(), totalY / overlapPoints.size());
            }
        }

        return null;
    }

    private double getRandomRadians() {
        return Math.random() * 2 * Math.PI;
    }

    private double getRadians(float[] fromPoint, float[] toPoint) {
        return Math.atan2(toPoint[1] - fromPoint[1], toPoint[0] - fromPoint[0]);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onMove(BubbleInfo bubbleInfo, int centerX, int centerY, int deltaX, int deltaY, double velocity) {
        velocity /= 6;
        if (velocity > bubbleInfo.getSpeed()) {
            float radians = (float) getRadians(new float[] {centerX, centerY}, new float[] {centerX + deltaX, centerY + deltaY});
            bubbleInfo.setRadians(radians);
            bubbleInfo.setSpeed((int) velocity);
        }
    }

}
