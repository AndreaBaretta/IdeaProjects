package com.company;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;


public class LinearDynamicalSystem {
    // Documentation: Rinaldi, S., _I_sistemi_lineari:_teoria,_modelli,_applicazioni, UTET, 1997
    final protected Array2DRowRealMatrix A;
    final protected Array2DRowRealMatrix B;
    final protected Array2DRowRealMatrix C;
    final protected Array2DRowRealMatrix D;

    protected RealMatrix x; // state vector
    protected long t; // time
    public RealMatrix y; // output vector

    public LinearDynamicalSystem(
            final Array2DRowRealMatrix A,
            final Array2DRowRealMatrix B,
            final Array2DRowRealMatrix C,
            final Array2DRowRealMatrix D,
            final Array2DRowRealMatrix x,
            final long t) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.x = x;
        this.t = t;
    }

    public long getTime() { return t;}
    public  RealMatrix update(final Array2DRowRealMatrix u, final long next_t) {
        final long dt = next_t - t;
        assert dt > 0: "And the Lord said, 'You shan't update a dynamical system over a zero or negative time interval.' Book of Linear Dynamics, 8:14";

        final RealMatrix dx_dt = A.multiply(x).add(B.multiply(u)); // A*x + B*u
        final RealMatrix next_x = x.add(dx_dt.scalarMultiply(dt));
        final RealMatrix next_y = C.multiply(x).add(D.multiply(u));
        t = next_t;
        x = next_x;
        y = next_y;
        return next_y;
    }
}
