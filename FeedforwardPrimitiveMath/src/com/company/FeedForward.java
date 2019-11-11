package com.company;

public class FeedForward {

    public static class PosV {
        public final Vector3 pos;
        public final Vector3 v;
        public PosV(final Vector3 pos, final Vector3 v) {
            this.pos = pos;
            this.v = v;
        }
    }

    final DifferentiableFunction1D fPosX; //Functions for x, y, and alpha, under they assumption everything is working alright
    final DifferentiableFunction1D fPosY;
    final DifferentiableFunction1D fPosAlpha;
    final RealFunction1D fVX;
    final RealFunction1D fVY;
    final RealFunction1D fVAlpha;

    public FeedForward(final DifferentiableFunction1D fPosX, final DifferentiableFunction1D fPosY, final DifferentiableFunction1D fPosAlpha) {
        this.fPosX = fPosX;
        this.fPosY = fPosY;
        this.fPosAlpha = fPosAlpha;
        this.fVX = fPosX.derivative();
        this.fVY = fPosY.derivative();
        this.fVAlpha = fPosAlpha.derivative();
    }

    public PosV current(final long t) {
        final Vector3 pos = new Vector3(fPosX.evaluate(t), fPosY.evaluate(t), fPosAlpha.evaluate(t));
        final Vector3 v = new Vector3(fVX.evaluate(t), fVY.evaluate(t), fVAlpha.evaluate(t));
        return new PosV(pos, v);
    }

}
