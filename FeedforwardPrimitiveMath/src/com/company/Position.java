package com.company;

public class Position {

    public double x;
    public double y;
    public double theta;

    public Position(final double x, final double y, final double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public static Position subtractPosition(final Position pos1, final Position pos2) {
        return new Position(pos1.x-pos2.x, pos1.y-pos2.y, pos1.theta-pos2.theta);
    }

    public static Position addPosition(final Position pos1, final Position pos2) {
        return new Position((double)(pos1.x+pos2.x), pos1.y+pos2.y, pos1.theta+pos2.theta);
    }

    public void addPosition(final Position pos) {
        x += pos.x;
        y += pos.y;
        theta += pos.theta;
    }

}
