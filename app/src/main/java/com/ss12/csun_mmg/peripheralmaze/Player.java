package com.ss12.csun_mmg.peripheralmaze;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Guss on 2/15/2015.
 */
public class Player {
    private String location;
    private int facing;
    private Tile currentTile;

    public Player (String l, int f) {
        location = l;
        facing = f;
        currentTile = getTileFromLocation(l);
    }

    public void updateLocation(String newLocation) {
        location = newLocation;
        currentTile = getTileFromLocation(newLocation);
    }

    public boolean checkIfWall(int direction) {
        return (currentTile.walls[direction] > 0);
    }

    public void look(Context context, int direction) {
        vibrate(context, checkIfWall(direction));
    }

    public void move(int direction) {
        if (!checkIfWall(direction))
        {
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
        }
    }

    private Tile getTileFromLocation(String location) {
        return  Board.board[location.charAt(0)][location.charAt(1)];
    }

    private void vibrate(Context c, boolean b) {
        long[] pattern;
        pattern = (b) ? new long[]{0,200,100,500} : new long[]{0,1};
        Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
