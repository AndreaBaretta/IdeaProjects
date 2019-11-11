package com.company;

import org.apache.commons.math3.linear.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	    final double[][] data = new double[][] { {1,1}, {1,2}};
	    final double[] A1 = new double[] {4,6};
	    final double[] A2 = new double[] {4,5,6};
	    final double[] A3 = new double[] {7,8,8};
//	    final Mat test = new Mat(3,3, CvType.CV_64F, );

		final RealMatrix realMatrix = MatrixUtils.createRealMatrix(data);
		System.out.println(Arrays.toString(realMatrix.getRow(2)));

//		final Array2DRowRealMatrix abstractRealMatrix = new Array2DRowRealMatrix(data);
		final LUDecomposition singularValueDecomposition = new LUDecomposition(realMatrix);
		final DecompositionSolver decompositionSolver = singularValueDecomposition.getSolver();
		final ArrayRealVector arrayRealVector = new ArrayRealVector(A1);
		final RealVector solved = decompositionSolver.solve(arrayRealVector);

		System.out.println(Arrays.toString(solved.toArray()));

//	    final MatOfDouble A = new MatOfDouble();
//	    A.fromArray(A1);
//	    A.fromArray(A2);
//	    A.fromArray(A3);
//		test.copyTo(A);
//	    A.fromArray(Aarray);
//		Imgproc.resize(A, A, new Size(3,3));
//		System.out.println(A.height() + "x" + A.width());
//		Imgproc.resize(A, A, new Size(9,1));
//		System.out.println(Arrays.toString(A.toArray()));
    }
}
