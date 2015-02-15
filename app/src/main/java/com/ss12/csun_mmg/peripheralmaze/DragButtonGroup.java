package com.ss12.csun_mmg.peripheralmaze;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cmcateer on 2/15/15.
 */
public class DragButtonGroup extends View {
    Context context;

    private Point startDrag;
    private Point stopDrag;

    private Paint mLinePaint;
    private Paint mTextPaint;

    private float mRadius=50;

    private String[] mButtonLabels;
    private float mWedgeSize;
    private Point[] mWedgePositions;
    private int mSelectedIndex;

    public DragButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        stopDrag = new Point();
        this.setFocusableInTouchMode(true);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DragButtonGroup,
                0, 0);

        try {
            int id = a.getResourceId(R.styleable.DragButtonGroup_button_labels, 0);
            if (id != 0) {
                mButtonLabels = getResources().getStringArray(id);
            }
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(0xff000000);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(0xff000000);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);

        mRadius = 30 * getResources().getDisplayMetrics().density;

        // init wedge sizes and positions
        if (mButtonLabels != null && mButtonLabels.length>0) {
            mWedgeSize = (float)(360.0/mButtonLabels.length);
            double wedgeRad = Math.toRadians(mWedgeSize);
            mWedgePositions = new Point[mButtonLabels.length];
            for (int i=0; i<mButtonLabels.length; i++) {
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
        return startDrag != null;
    }

    public void startDragging(Point start) {
        if (start == null) {
            return;
        }
        startDrag = new Point(start.x, start.y);
        onDrag(start);
    }

    public void onDrag(Point dragPoint) {
        if (dragPoint==null) {
            return;
        }
        if (!isDragging()) {
            startDragging(dragPoint);
        }
        stopDrag.set(dragPoint.x, dragPoint.y);

        // check which label is closest
        mSelectedIndex = -1;
        if (stopDrag.x!=startDrag.x || stopDrag.y!=startDrag.y) {
            double shortest=Double.POSITIVE_INFINITY;
            double dx, dy, thisShort;
            for (int i=0; i<mWedgePositions.length; i++) {
                dx = stopDrag.x-(startDrag.x+mWedgePositions[i].x);
                dy = stopDrag.y-(startDrag.y+mWedgePositions[i].y);
                thisShort = Math.sqrt((dx*dx)+(dy*dy));
                if (thisShort < shortest) {
                    shortest = thisShort;
                    mSelectedIndex = i;
                }
            }
        }

        // tell UI to redraw our view
        invalidate();
    }

    public void stopDragging(Point stop) {
        startDrag = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isDragging()) {
            return;
        }
        // draw a line from the start drag point to the stop drag point
        canvas.drawLine(startDrag.x, startDrag.y, stopDrag.x, stopDrag.y, mLinePaint);
        canvas.drawCircle(startDrag.x, startDrag.y, 5, mLinePaint);
        canvas.drawCircle(stopDrag.x, stopDrag.y, 5, mLinePaint);

        // draw button labels
        if (mButtonLabels != null) {
            for (int i=0; i<mButtonLabels.length; i++) {
                if (i == mSelectedIndex) {
                    mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                } else {
                    mTextPaint.setTypeface(null);
                }
                Point labelPoint = mWedgePositions[i];
                canvas.drawText(
                        mButtonLabels[i],
                        startDrag.x+labelPoint.x, startDrag.y+labelPoint.y,
                        mTextPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Point point = new Point((int) event.getX(), (int) event.getY());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startDragging(point);
                break;
            case MotionEvent.ACTION_MOVE:
                onDrag(point);
                break;
            case MotionEvent.ACTION_UP:
                stopDragging(point);
        }
        return super.onTouchEvent(event);
    }
}
