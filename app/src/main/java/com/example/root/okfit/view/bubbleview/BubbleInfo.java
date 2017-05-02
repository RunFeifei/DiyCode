package com.example.root.okfit.view.bubbleview;

import android.graphics.Rect;

public class BubbleInfo {
    private int index;
    private Rect rect;
    private double radians;
    private int speed;
    private int oldSpeed;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public double getRadians() {
        return radians;
    }

    public void setRadians(double radians) {
        this.radians = radians;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getOldSpeed() {
        return oldSpeed;
    }

    public void setOldSpeed(int oldSpeed) {
        this.oldSpeed = oldSpeed;
    }
}
