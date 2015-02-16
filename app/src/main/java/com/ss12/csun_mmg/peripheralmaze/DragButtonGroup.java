package com.ss12.csun_mmg.peripheralmaze;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cmcateer on 2/15/15.
 */
public class DragButtonGroup extends View {
    Context context;

    private MotionEvent mStartDragEvent;
    private MotionEvent mStopDragEvent;

    private Paint mLinePaint;

    private float mRadius=50;

    private View[] mViewChildren;
    private float mWedgeSize;
    private Point[] mWedgePositions;
    private int mSelectedIndex;

    public DragButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setFocusableInTouchMode(true);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DragButtonGroup,
                0, 0);
        Resources res = getResources();

        try {
            int childrenArrayId = a.getResourceId(R.styleable.DragButtonGroup_children, 0);
            if (childrenArrayId == R.array.main_menu_controls) {
                Log.i("DragButton", "Loaded CORRECT array resource ID: "+childrenArrayId);
            } else {
                Log.e("DragButton", "Loaded INCORRECT array resource ID: "+childrenArrayId+", should be ID: "+R.array.main_menu_controls);
            }
            if (childrenArrayId != 0) {
                TypedArray viewIds = getResources().obtainTypedArray(childrenArrayId);
                Log.i("DragButton", "Loading " + viewIds.length() + " view children");

                mViewChildren = new View[viewIds.length()];
                for (int i=0; i<viewIds.length(); i++) {
                    int layoutId = viewIds.getResourceId(i,0);
                    if (i==0 && layoutId==R.layout.btn_drag_exit) {
                        Log.i("DragButton", "Loaded CORRECT layout ID: "+layoutId);
                    } else {
                        Log.w("DragButton", "Loaded INCORRECT layout Id: "+layoutId+", should be ID: "+R.layout.btn_drag_exit);
                    }
                    if (layoutId != 0) {
                        Log.d("DragButton","Loading layout ID: "+layoutId);
                        mViewChildren[i] = findViewById(layoutId);
                        View testView = getRootView();
                        Log.d("DragButton", "rooted view: "+testView.toString());
                        if (mViewChildren[i] == null) {
                            Log.e("DragButton", "Loaded null view ID: "+layoutId);
                        }
                    }
                }
            }
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(0xff000000);

        mRadius = 30 * getResources().getDisplayMetrics().density;

        // init wedge sizes and positions
        if (mViewChildren != null && mViewChildren.length>0) {
            mWedgeSize = (float)(360.0/mViewChildren.length);
            double wedgeRad = Math.toRadians(mWedgeSize);
            mWedgePositions = new Point[mViewChildren.length];
            for (int i=0; i<mViewChildren.length; i++) {
                double x = Math.sin(wedgeRad*i) * mRadius;
                double y = Math.cos(wedgeRad*i) * mRadius;
                mWedgePositions[i] = new Point((int)x,(int)y);
            }
        } else {
            mWedgeSize = 0;
            mWedgePositions = null;
        }
    }

    public boolean isDragging() {
        return mStartDragEvent != null;
    }

    public void startDragging(MotionEvent event) {
        if (event == null) {
            return;
        }
        mStartDragEvent = MotionEvent.obtain(event);

        positionViewChildren();
        showViewChildren();

        onDrag(event);
    }

    public void onDrag(MotionEvent event) {
        if (event==null) {
            return;
        }
        if (!isDragging()) {
            startDragging(event);
            return;
        }
        mStopDragEvent = MotionEvent.obtain(event);

        if (mViewChildren != null) {
            // check which label is closest
            mSelectedIndex = -1;
            if (event.getX() != mStartDragEvent.getX() || event.getY() != mStartDragEvent.getY()) {
                double shortest = Double.POSITIVE_INFINITY;
                double dx, dy, thisShort;
                for (int i = 0; i < mWedgePositions.length; i++) {
                    dx = event.getX() - (mStartDragEvent.getX() + mWedgePositions[i].x);
                    dy = event.getY() - (mStartDragEvent.getY() + mWedgePositions[i].y);
                    thisShort = Math.sqrt((dx * dx) + (dy * dy));
                    if (thisShort < shortest) {
                        shortest = thisShort;
                        mSelectedIndex = i;
                    }
                }
            }
        }

        // tell UI to redraw our view
        invalidate();
    }

    public void stopDragging(MotionEvent event) {
        mStartDragEvent = null;
        mStopDragEvent = MotionEvent.obtain(event);
        hideViewChildren();
        invalidate();
    }

    private void positionViewChildren() {
        if (mViewChildren == null) {
            return;
        }
        for (int i=0; i<mViewChildren.length; i++) {
            if (mViewChildren[i] == null) {
                continue;
            }
            mViewChildren[i].setTranslationX(mStartDragEvent.getX() + mWedgePositions[i].x);
            mViewChildren[i].setTranslationY(mStartDragEvent.getY() + mWedgePositions[i].y);
        }
    }

    private void hideViewChildren() {
        if (mViewChildren == null) {
            return;
        }
        for (int i=0; i<mViewChildren.length; i++) {
            if (mViewChildren[i] == null) {
                continue;
            }
            mViewChildren[i].setVisibility(View.INVISIBLE);
        }
    }

    private void showViewChildren() {
        if (mViewChildren == null) {
            return;
        }
        for (int i=0; i<mViewChildren.length; i++) {
            if (mViewChildren[i] == null) {
                continue;
            }
            mViewChildren[i].setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isDragging()) {
            return;
        }
        // draw a line from the start drag point to the stop drag point
        Log.d("DragButton", "["+mStartDragEvent.getX()+","+mStartDragEvent.getY()+"] -> ["+mStopDragEvent.getX()+","+mStopDragEvent.getY()+"]");
        canvas.drawLine(mStartDragEvent.getX(), mStartDragEvent.getY(), mStopDragEvent.getX(), mStopDragEvent.getY(), mLinePaint);
        canvas.drawCircle(mStartDragEvent.getX(), mStartDragEvent.getY(), 5, mLinePaint);
        canvas.drawCircle(mStopDragEvent.getX(), mStopDragEvent.getY(), 5, mLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("DragButton","Motion: "+event.toString());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDragging(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onDrag(event);
                break;
            case MotionEvent.ACTION_UP:
                stopDragging(event);
                break;
        }
        return super.onTouchEvent(event);
    }
}
