package com.ss12.csun_mmg.peripheralmaze;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ss12.csun_mmg.peripheralmaze.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class GameModeMenu extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = false;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    GestureDetector.SimpleOnGestureListener swipeDetector;
    GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_mode_menu);

        final View contentView = findViewById(R.id.mode_selector_layout);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.hide();

        // TODO attach callback for swipe movements here
        mDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("debug","swipe: ["+velocityX+","+velocityY+"]");

                double swipeMag = Math.sqrt((velocityX*velocityX) + (velocityY*velocityY));
                if (swipeMag < 25 || velocityX==0 || velocityY==0) {
                    return super.onFling(e1, e2, velocityX, velocityX);
                }

                float slope = velocityY/velocityX;
                float slopeMag = Math.abs(slope);
                if (slopeMag >= 1.0) {
                    // swiped up or down
                    if (slope > 0) {
                        // swiped up
                        Log.i(GameModeMenu.class.getName(), "Setting game mode: " + GameMode.MODE.AUDIO_MODE);
                        GameMode.setMode(GameMode.MODE.AUDIO_MODE);
                    } else {
                        // swiped down
                        Log.i(GameModeMenu.class.getName(), "Setting game mode: " + GameMode.MODE.VISUAL_MODE);
                        GameMode.setMode(GameMode.MODE.VISUAL_MODE);
                    }
                } else {
                    // swiped left or right
                    Log.i(GameModeMenu.class.getName(), "Setting game mode: " + GameMode.MODE.DEFAULT_MODE);
                    GameMode.setMode(GameMode.MODE.DEFAULT_MODE);
                }

                // TODO go to main menu screen

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
