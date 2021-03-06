package com.ss12.csun_mmg.peripheralmaze;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

/**
 * Created by Guss on 2/15/2015.
 */
public class Player {
    public String location;
    public int facing;
    public Tile currentTile;
    public int stepCounter;
    public MazeGame mMazeGame;

    public Player (MazeGame mazeGame, String l, int f) {
        mMazeGame = mazeGame;
        location = l;
        facing = f;
        currentTile = getTileFromLocation(l);
        stepCounter = 0;
    }

    public boolean look(int direction) {
        stepCounter += 1;
        return checkIfWall(direction);
    }

    public boolean move(int direction) {
        facing = direction;

        if (checkIfWall(direction))
            return false;

        int x = Integer.parseInt(""+location.charAt(0));
        int y = Integer.parseInt(""+location.charAt(1));

        switch (direction) {
            case 0:
                this.updateLocation((x-1) + "" + y);
                break;
            case 1:
                this.updateLocation(x + "" + (y+1));
                break;
            case 2:
                this.updateLocation((x+1) + "" + y);
                break;
            case 3:
                this.updateLocation(x + "" + (y-1));
                break;
        }
        stepCounter += 2;
        return true;
    }

    private void updateLocation(String newLocation) {
        location = newLocation;
        currentTile = getTileFromLocation(newLocation);
    }

    private boolean checkIfWall(int direction) {
        return (currentTile.walls[direction] > 0);
    }

    private Tile getTileFromLocation(String location) {
        int row = Integer.parseInt(location.substring(0,1));
        int col = Integer.parseInt(location.substring(1,2));
        return mMazeGame.getMaze().mazeTiles[row][col];
    }

    private void vibrate(Context c, boolean b) {
        long[] pattern;
        pattern = (b) ? new long[]{0,200,100,500} : new long[]{0,50};
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(pattern,1);
    }

    public void moveSoundEffect(Context c, boolean b) {
        MediaPlayer mediaPlayer;
        int audioFile = (b) ? R.raw.blip_wall : R.raw.blip_no_wall;
        mediaPlayer = MediaPlayer.create(c, audioFile);
        mediaPlayer.start();
    }
}