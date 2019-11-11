package com.company;

import javafx.geometry.Pos;

public class FeedForward1 {
    final DifferentiableFunction1D functionX; //Functions for x, y, and alpha, under they assumption everything is working alright
    final DifferentiableFunction1D functionY;
    final DifferentiableFunction1D functionAlpha;
    public final Position position;
    public final Position velocity;

    public FeedForward1(final DifferentiableFunction1D functionX, final DifferentiableFunction1D functionY, final DifferentiableFunction1D functionAlpha) {
        this.functionX = functionX;
        this.functionY = functionY;
        this.functionAlpha = functionAlpha;
        position = new Position(0,0,0);
        velocity = new Position(0,0,0);
    }

    public void updatePos() {
        final long t1 = System.currentTimeMillis();

        position.addPosition(new Position(functionX.evaluate(t1), functionY.evaluate(t1),functionAlpha.evaluate(t1)));
        velocity.addPosition(new Position(functionX.derivative().evaluate(t1), functionY.derivative().evaluate(t1), functionAlpha.derivative().evaluate(t1)));

    }

}
