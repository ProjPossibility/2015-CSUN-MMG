package com.ss12.csun_mmg.peripheralmaze;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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

    GestureDetectorCompat mDetector;
    MediaPlayer mpAudio;
    MediaPlayer mpVisual;
    MediaPlayer mpRepeat;
    MediaPlayer[] audios;

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
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(GameModeMenu.class.getName(), "User tapped to replay options audio");
                GameModeMenu.this.playInstructions();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();
                dx = dx==0 ? 1 : dx;
                dy = dy==0 ? 1 : dy;
                Log.d("debug","swipe: ["+dx+","+dy+"]");

                if (Math.abs(dy) >= Math.abs(dx)) {
                    // swiped up or down
                    if (dy <= 0) {
                        // swiped up
                        Log.i(GameModeMenu.class.getName(), "Setting game mode: " + GameMode.MODE.AUDIO_MODE);
                        GameMode.setMode(GameMode.MODE.AUDIO_MODE);
                    } else {
                        // swiped down
                        Log.i(GameModeMenu.class.getName(), "Setting game mode: " + GameMode.MODE.VISUAL_MODE);
                        GameMode.setMode(GameMode.MODE.VISUAL_MODE);
                    }
                }

                // TODO go to main menu screen
                Intent intent = new Intent(GameModeMenu.this, MainMenu.class);
                startActivity(intent);

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mpAudio = MediaPlayer.create(this, R.raw.mode_vocals_audio);
        mpVisual = MediaPlayer.create(this, R.raw.mode_vocals_visual);
        mpRepeat = MediaPlayer.create(this, R.raw.mode_vocals_repeat);
        audios = new MediaPlayer[]{mpAudio,mpVisual,mpRepeat};
        mpAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpVisual.start();
            }
        });
        mpVisual.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpRepeat.start();
            }
        });
        mpRepeat.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpRepeat.reset();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopInstructions();
        mpAudio.release();
        mpVisual.release();
        mpRepeat.release();
    }

    @Override
    protected void onResume() {
        super.onResume();

        playInstructions();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    private void playInstructions() {
        for (MediaPlayer mp : audios) {
            if (mp.isPlaying()) {
                return;
            }
        }

        mpAudio.start();
    }

    private void stopInstructions() {
        for (MediaPlayer mp : audios) {
            if (mp.isPlaying()) {
                mp.stop();
                mp.reset();
            }
        }
    }
}
