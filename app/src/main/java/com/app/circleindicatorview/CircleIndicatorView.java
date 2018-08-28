package com.app.circleindicatorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CircleIndicatorView extends View implements GestureDetector.OnGestureListener {

    private final static int DEFAULT_RADIUS_SELECT = 4;
    private final static int DEFAULT_RADIUS_NORMAL = 3;
    private final static int DEFAULT_SPACE = 6;
    private final static int HORIZONTAL = 0;
    private final static int VERTICAL = 1;

    private int mSelectedIndex = 0;
    private int mIndicatorCount = 0;
    private int mViewWidth = 0;
    private int mViewHeight = 0;

    private int mNormalRadius;
    private int mSelectRadius;
    private int mDirection;
    private Paint mPaint;
    private int mSpace;

    private GestureDetector mGestureDetector;
    private List<Point> mCirclePoints = new ArrayList<>();
    private IndicatorClickListener mIndicatorClickListener;

    public CircleIndicatorView(Context context) {
        this(context, null);
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(context, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicatorView);
        int circleColor = typedArray.getColor(R.styleable.CircleIndicatorView_circle_color, Color.LTGRAY);
        mPaint = getPaint(circleColor);
        mSelectRadius = typedArray.getInt(R.styleable.CircleIndicatorView_circle_radius_select, DEFAULT_RADIUS_SELECT);
        mNormalRadius = typedArray.getInt(R.styleable.CircleIndicatorView_circle_radius_normal, DEFAULT_RADIUS_NORMAL);
        mSpace = typedArray.getInt(R.styleable.CircleIndicatorView_space, DEFAULT_SPACE);
        mDirection = typedArray.getInteger(R.styleable.CircleIndicatorView_direction, HORIZONTAL);
        typedArray.recycle();

    }

    public void setIndicatorCount(int count) {
        if (count == mIndicatorCount) {
            return;
        }
        mSelectedIndex = 0;
        mIndicatorCount = count;
        mViewWidth = (mDirection == VERTICAL) ? mSelectRadius * 2 : (mSelectRadius * 2 + (mNormalRadius * 2 + mSpace) * (count - 1));
        mViewHeight = (mDirection == VERTICAL) ? mSelectRadius * 2 + (mNormalRadius * 2 + mSpace) * (count - 1) : mSelectRadius * 2;
        requestLayout();
    }

    public void setSelectIndicator(int selectedIndex) {
        mSelectedIndex = selectedIndex;
        postInvalidate();
    }

    public void setIndicatorClickListener(IndicatorClickListener indicatorClickListener) {
        mIndicatorClickListener = indicatorClickListener;
    }

    private Paint getPaint(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        return paint;
    }


    private boolean isInCircle(MotionEvent event, Point point, int index) {
        double l = Math.sqrt((point.x - event.getX()) * (point.x - event.getX())
                + (point.y - event.getY()) * (point.y - event.getY()));
        if (index == mSelectedIndex) {
            if (l <= mSelectRadius) {
                return true;
            }
        } else {
            if (l <= mNormalRadius) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCirclePoints.clear();
        int startDistance = 0;
        for (int i = 0; i < mIndicatorCount; i++) {
            int paintRadius = mNormalRadius;
            if (mSelectedIndex == i) {
                paintRadius = mSelectRadius;
                startDistance = (mNormalRadius * 2 + mSpace) * (i) + mSelectRadius;
            } else if (mSelectedIndex < i) {
                startDistance = (mNormalRadius * 2 + mSpace) * (i - 1) + mSelectRadius * 2 + mSpace + mNormalRadius;
            } else if (mSelectedIndex > i) {
                startDistance = (mNormalRadius * 2 + mSpace) * (i) + mNormalRadius;
            }
            Point point = new Point();
            if (mDirection == VERTICAL) {
                canvas.drawCircle(mSelectRadius, startDistance, paintRadius, mPaint);
                point.set(mSelectRadius, startDistance);
            } else {
                canvas.drawCircle(startDistance, mSelectRadius, paintRadius, mPaint);
                point.set(startDistance, mSelectRadius);
            }
            mCirclePoints.add(point);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIndicatorCount > 0) {
            setMeasuredDimension(mViewWidth, mViewHeight);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent event) {
        for (int i = 0; i < mCirclePoints.size(); i++) {
            Point point = mCirclePoints.get(i);
            if (isInCircle(event, point, i)) {
                if (mIndicatorClickListener != null) {
                    mIndicatorClickListener.OnClick(i);
                    mSelectedIndex = i;
                    postInvalidate();
                }
            }
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface IndicatorClickListener {
        void OnClick(int position);
    }
}
