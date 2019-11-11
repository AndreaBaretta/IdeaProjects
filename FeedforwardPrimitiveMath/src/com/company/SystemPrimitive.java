package com.company;

public class SystemPrimitive {
    final double a;
    final double d;
    final double V;
    final double x1;
    final double t1;
    final double t2;
    final double t3;
    final double[] translation; // {Translation in value, translation in time}
    public final double ETA;
    public SystemPrimitive(final double acceleration, final double deceleration, final double cruiseVelocity, final double targetDestination, final double[] translation) {
        x1 = targetDestination;
        a = acceleration;
        d = deceleration;
        V = cruiseVelocity;
        t1 = V/a;
        t3 = V/d;
        t2 = (x1-(0.5*a*Math.pow(t1,2))+(0.5*d*Math.pow(t3,2))-V*t3)/V;
        this.translation = translation;
        ETA = t1 + t2 + t3 + translation[1];
    }

    public PiecewisePolynomial build() {
        return new PiecewisePolynomial(new Polynomial[] {
                new Polynomial(new double[] {a/2, 0, 0 + translation[0]}),
                new Polynomial(new double[] {V, -V*t1 + a * Math.pow(t1, 2) / 2 + translation[0]}),
                new Polynomial(new double[] {-d / 2, V+d*(t1+t2), (a * Math.pow(t1,2) / 2) - V*(t1) - 0.5*d*Math.pow(t1+t2,2) + translation[0]})
        }, new double[][] {
                new double[] {0 + translation[1], t1 + translation[1]},
                new double[] {t1 + translation[1], t1+t2 + translation[1]},
                new double[] {t1+t2 + translation[1], t1+t2+t3 + translation[1]}
        });
    }
}
