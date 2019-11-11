import org.apache.commons.math3.linear.*;

import java.util.Arrays;

public class TestOdometry {
    final Vector3 startPos;
    final Vector3[] setup;
    final double[] startEncoderPos;

    public TestOdometry() {
        startPos = new Vector3(0,0,0);
//        setup = new Vector3[] {
//                new Vector3(0, 145, 0),
//                new Vector3(180, 45, Math.PI/2),
//                new Vector3(-180, 45, Math.PI/3)
        setup = new Vector3[] {
                new Vector3(0, 145, 0),
                new Vector3(180, -45, -Math.PI/2),
                new Vector3(-180, -45, Math.PI/2)
        };

        startEncoderPos = new double[] {0,0,0};
    }

//    public double[] encoderChange(final double[] prevEncoder, final Vector3[] setup, final Vector3 changePos, final Vector3 startPos) {
//        return new double[] {
//                prevEncoder[0] + /*(int) Math.floor*/(
//                        changePos.x*Math.cos(setup[0].theta+startPos.theta) + changePos.y*Math.sin(setup[0].theta+startPos.theta) + changePos.theta * Math.hypot(setup[0].x, setup[0].y) * Math.sin(setup[0].theta - Math.atan2(setup[0].y, setup[0].x))),
//                prevEncoder[1] + /*(int) Math.floor*/(
//                        changePos.x*Math.cos(setup[1].theta+startPos.theta) + changePos.y*Math.sin(setup[1].theta+startPos.theta) + changePos.theta * Math.hypot(setup[1].x, setup[1].y) * Math.sin(setup[1].theta - Math.atan2(setup[1].y, setup[1].x))),
//                prevEncoder[2] + /*(int) Math.floor*/(
//                        changePos.x*Math.cos(setup[2].theta+startPos.theta) + changePos.y*Math.sin(setup[2].theta+startPos.theta) + changePos.theta * Math.hypot(setup[2].x, setup[2].y) * Math.sin(setup[2].theta - Math.atan2(setup[2].y, setup[2].x))),
//        };
//    }

    public static RealVector dsOfDr(final Vector3[] setup, final Vector3 dr, final Vector3 r) {
        final RealMatrix A = matrix(setup, r);
        return A.operate(dr.toRealVector());
    }

    public static RealMatrix matrix(final Vector3[] setup, final Vector3 pos) {
        final double[][] A = OdometryMath.makeA(setup, pos.theta);
        final RealMatrix matrixA = MatrixUtils.createRealMatrix(A);
        return matrixA;
    }

    public double[] round(final double[] l) {
        return  new double[] {
                Math.round(l[0]*100d)/100d, Math.round(l[1]*100d)/100d, Math.round(l[2]*100d)/100d
        };
    }

    public void run() {
        final double ITERATIONS = 1000;
        RealVector encoders = new ArrayRealVector(new double[] {0,0,0});
        final Vector3 change = new Vector3(2442/ITERATIONS,3144.4/ITERATIONS, Math.PI*2/ITERATIONS);

        Vector3 newPos = startPos;
        Vector3 predictedPos = startPos;
        OdometryMath odometryMath = new OdometryMath(startPos, setup, encoders.toArray());
//        double[] simulatedEncoder = encoders;

        for (int i = 0; i < ITERATIONS; i++) {
            newPos = Vector3.AddVector(newPos, change);
            System.out.println("Position: " + newPos.toString());
//            encoders = encoderChange(encoders, setup, change, Vector3.SubtractVector(newPos, change));
            RealMatrix matrix = matrix(setup, newPos);
            SingularValueDecomposition svdDecomp = new SingularValueDecomposition(matrix);
            double conditionNum = svdDecomp.getConditionNumber();
            System.out.println("Condition number: " + conditionNum);
            System.out.println("Matrix:");
            for (int j = 0; j<3; j++) System.out.println(Arrays.toString(matrix.getRow(j)));
            RealVector encoderChange = dsOfDr(setup, change, newPos);
            encoders = encoderChange.add(encoders);


            System.out.println("Encoders: " + Arrays.toString(encoders.toArray()));

//            for (int n = 0; n < 11; n++) {
//                for (int j=0; j<3; j++) {
//                    simulatedEncoder[j] += encoders[j] / 10;
//                }
//                System.out.println("Encoders: " + Arrays.toString(round(simulatedEncoder)));
            odometryMath = odometryMath.update(round(encoders.toArray()));
            predictedPos = odometryMath.pos;
            System.out.println("Predicted: " + predictedPos);
            System.out.println();
//            }
        }
    }
}