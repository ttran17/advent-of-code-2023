package com.github.ttran17.day22;

public class Brick
{
    protected final int id;

    protected final int startX;
    protected final int endX;

    protected final int startY;
    protected final int endY;

    protected int startZ;
    protected int endZ;

    protected Brick( int id, int startX, int endX, int startY, int endY, int startZ, int endZ )
    {
        this.id = id;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.startZ = startZ;
        this.endZ = endZ;
    }
}
