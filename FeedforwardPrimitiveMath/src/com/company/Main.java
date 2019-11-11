package com.company;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println(IntStream.range(0,3).toArray()[1]);
        System.out.println(new double[] {0,1,2}[1]);

        final double[] coefficients = new double[] {2,2,2,2};

        final double[] result = new double[coefficients.length-1];
        for (int i = 0; i < coefficients.length-1; i++) {
            result[i] = coefficients[i] * (coefficients.length-1-i);
        }
        System.out.println(Arrays.toString(result));

    }
}
