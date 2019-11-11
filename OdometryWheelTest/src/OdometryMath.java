import org.apache.commons.math3.linear.*;
import org.opencv.core.Mat;

import java.util.Arrays;

public class OdometryMath {

    //    final LinearOpMode opMode;
    final Vector3[] setup;
    final double[] encoderPosition;
//    final LUDecomposition luDecomposition;
//    final DecompositionSolver decompositionSolver;
    public final Vector3 pos;
//    final RealMatrix matrixA;
//    final double[][] arrayA;

    public OdometryMath(final Vector3 pos, final Vector3[] setup, final double[] encoderPosition) {
        this.pos = pos;
        this.setup = setup;
        this.encoderPosition = encoderPosition;

        final double alpha = pos.theta;

//        arrayA = new double[][] {
//                {Math.cos(alpha+setup[0].theta), Math.sin(alpha+setup[0].theta), Math.hypot(setup[0].x, setup[0].y)*Math.sin(setup[0].theta-Math.atan2(setup[0].y, setup[0].x))},
//                {Math.cos(alpha+setup[1].theta), Math.sin(alpha+setup[1].theta), Math.hypot(setup[1].x, setup[1].y)*Math.sin(setup[1].theta-Math.atan2(setup[1].y, setup[1].x))},
//                {Math.cos(alpha+setup[2].theta), Math.sin(alpha+setup[2].theta), Math.hypot(setup[2].x, setup[2].y)*Math.sin(setup[2].theta-Math.atan2(setup[2].y, setup[2].x))}
//        };
//        arrayA = makeA(setup, alpha);
//        matrixA = MatrixUtils.createRealMatrix(arrayA);
//        matrixA = makeA(setup, alpha);
//        luDecomposition = new LUDecomposition(matrixA);
//        decompositionSolver = luDecomposition.getSolver();

    }

    public static double[][] makeA(final Vector3[] setup, final double alpha) {
        final double[][] A = new double[][]{
                {Math.cos(alpha + setup[0].theta), Math.sin(alpha + setup[0].theta), Math.hypot(setup[0].x, setup[0].y) * Math.sin(setup[0].theta - Math.atan2(setup[0].y, setup[0].x))},
                {Math.cos(alpha + setup[1].theta), Math.sin(alpha + setup[1].theta), Math.hypot(setup[1].x, setup[1].y) * Math.sin(setup[1].theta - Math.atan2(setup[1].y, setup[1].x))},
                {Math.cos(alpha + setup[2].theta), Math.sin(alpha + setup[2].theta), Math.hypot(setup[2].x, setup[2].y) * Math.sin(setup[2].theta - Math.atan2(setup[2].y, setup[2].x))}
        };
        return A;
    }

    protected double[][] makeA2(final Vector3[] setup, final double prevAlpha, final double dAlpha) {
        final double[][] A = new double[][] {
                {(2*Math.cos(setup[0].theta+prevAlpha+(dAlpha/2))*Math.sin(dAlpha/2))/dAlpha, (2*Math.sin(setup[0].theta+prevAlpha+(dAlpha/2))*Math.sin(dAlpha/2)/dAlpha), Math.hypot(setup[0].x, setup[0].y) * Math.sin(setup[0].theta - Math.atan2(setup[0].y, setup[0].x))},
                {(2*Math.cos(setup[1].theta+prevAlpha+(dAlpha/2))*Math.sin(dAlpha/2))/dAlpha, (2*Math.sin(setup[1].theta+prevAlpha+(dAlpha/2))*Math.sin(dAlpha/2)/dAlpha), Math.hypot(setup[1].x, setup[1].y) * Math.sin(setup[1].theta - Math.atan2(setup[1].y, setup[1].x))},
                {(2*Math.cos(setup[2].theta+prevAlpha+(dAlpha/2))*Math.sin(dAlpha/2))/dAlpha, (2*Math.sin(setup[2].theta+prevAlpha+(dAlpha/2))*Math.sin(dAlpha/2)/dAlpha), Math.hypot(setup[2].x, setup[2].y) * Math.sin(setup[2].theta - Math.atan2(setup[2].y, setup[2].x))}
        };
        return A;
    }

    protected double[] solve(final RealMatrix matrixA, final double[] b) {
        final ArrayRealVector vectorb = new ArrayRealVector(b);
        final LUDecomposition luDecomposition = new LUDecomposition(matrixA);
        final DecompositionSolver decompositionSolver = luDecomposition.getSolver();
        final RealVector solved = decompositionSolver.solve(vectorb);
        final double[] solvedArray = solved.toArray();
//        final Vector3 newPos = new Vector3(solvedArray2[0], solvedArray2[1], solvedArray2[2]);
//        return newPos;
        return solvedArray;
    }

    public OdometryMath update(final double[] newEncoderCount) {
        final double[] deltaCount = new double[3];
        for (int i = 0; i < 3; i++) {
            deltaCount[i] =  newEncoderCount[i] - encoderPosition[i];
        }


        final double[][] A = makeA(setup, pos.theta);
        final RealMatrix matrixA = MatrixUtils.createRealMatrix(A);
        final double[] x = solve(matrixA, deltaCount);
        final Vector3 newPos = new Vector3(x[0], x[1], x[2]);

        final double[][] A2;
        if (Math.abs(newPos.theta) < 1e-10d ) {
            A2 = makeA(setup, pos.theta + newPos.theta/2);
        } else {
            A2 = makeA2(setup, pos.theta, newPos.theta);
        }
//        final double[][] A2 = makeA(setup, pos.theta + newPos.theta/2);
        final RealMatrix matrixA2 = MatrixUtils.createRealMatrix(A2);
        final double[] x2 = solve(matrixA2, deltaCount);
        final Vector3 newPos2 = new Vector3(x2[0], x2[1], x2[2]);


        return new OdometryMath(Vector3.AddVector(newPos2, pos), setup, newEncoderCount);
//        return new OdometryMath(Vector3.AddVector(newPos, pos), setup, newEncoderCount);
    }
}