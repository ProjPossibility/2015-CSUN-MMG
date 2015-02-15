package com.ss12.csun_mmg.peripheralmaze;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Guss on 2/15/2015.
 */
public class Player {
    public String location;
    public int facing;
    public Tile currentTile;
    public int stepCounter;

    public Player (String l, int f) {
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
        return  Board.board[location.charAt(0)][location.charAt(1)];
    }

    private void vibrate(Context c, boolean b) {
        long[] pattern;
        pattern = (b) ? new long[]{0,200,100,500} : new long[]{0,50};
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(pattern,1);
    }
}