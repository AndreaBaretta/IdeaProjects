package com.company;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.DFT_INVERSE;
import static org.opencv.core.Core.DFT_SCALE;

public class Convolution {
    final Mat kernel;
    final Mat target;
    public final Mat finalConvolution;
    public final double max;
    public final Point maxLoc;
    public Convolution(final Mat kernel8bit, final Mat target8bit) {
        kernel = new Mat();
        kernel8bit.convertTo(kernel, CvType.CV_32FC3, 1.0/255.0);
        Core.subtract(kernel, Core.mean(kernel), kernel);

        target = new Mat();
        target8bit.convertTo(target, CvType.CV_32FC3, 1.0/255.0);

        final Mat convolutionColor = new Mat();
        Imgproc.filter2D(target, convolutionColor, CvType.CV_32FC3, kernel);
//        Core.dft(target, convolutionColor);

        final Mat convolution = new Mat();
        convolutionColor.copyTo(convolution);

        final List<Mat> convolutionChannels = new ArrayList<>(3);
        Core.split(convolution, convolutionChannels);

        for (int i = 0; i < 3; i++) {
            Core.multiply(convolutionChannels.get(i), convolutionChannels.get(i), convolutionChannels.get(i));
        }

        final Mat normConvolution = new Mat();
        Core.add(convolutionChannels.get(0), convolutionChannels.get(1), normConvolution);
        Core.add(normConvolution, convolutionChannels.get(2), normConvolution);

//        System.out.println(normConvolution.channels());
        final Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(normConvolution);
        final double cmin, cmax;
        cmin = minMaxLocResult.minVal;
        cmax = minMaxLocResult.maxVal;

        System.out.println("Max before screwy part: " + cmax);
        System.out.println("Min before screwy part: " + cmin);

//        Core.subtract(normConvolution, new Scalar(cmin), normConvolution);
//        Core.divide(normConvolution, new Scalar(cmax - cmin), normConvolution);

        Core.divide(normConvolution, new Scalar( Math.sqrt(kernel.dot(kernel))*Math.sqrt(target.dot(target)) ), normConvolution);


        final Core.MinMaxLocResult minMaxLocResultEnd = Core.minMaxLoc(normConvolution);
        final double cmin2 = minMaxLocResultEnd.minVal;

        final double cmax2 = minMaxLocResultEnd.maxVal;

        System.out.println("Max after screwy part: " + cmax2);
        System.out.println("Min after screwy part: " + cmin2);



        normConvolution.convertTo(normConvolution, CvType.CV_8U, 255);


        finalConvolution = normConvolution;
        max = cmax2;
        maxLoc = minMaxLocResultEnd.maxLoc;
    }

    public double getMax() {
        return max;
    }

    public static void convolveDFT(final Mat A, final Mat B, final Mat C) {
        C.create(Math.abs(A.rows() - B.rows())+1, Math.abs(A.cols() - B.cols())+1, A.type());
        final Size dftSize = new Size();
        // calculate the size of DFT transform
        dftSize.width = Core.getOptimalDFTSize(A.cols() + B.cols() - 1);
        dftSize.height = Core.getOptimalDFTSize(A.rows() + B.rows() - 1);

        // allocate temporary buffers and initialize them with 0's
        final Mat tempA = new Mat(dftSize, A.type(), Scalar.all(0));
        final Mat tempB = new Mat(dftSize, B.type(), Scalar.all(0));

        // copy A and B to the top-left corners of tempA and tempB, respectively
        final Mat roiA = new Mat();
        final Mat roiB = new Mat();
        System.out.println(dftSize.height + "x" + dftSize.width);

        System.out.println(Math.abs(A.height()-dftSize.height)/2);
        System.out.println(Math.abs(B.height()-dftSize.height)/2);
        System.out.println(Math.abs(A.width()-dftSize.width)/2);
        System.out.println(Math.abs(B.width()-dftSize.width)/2);

        assert (A.height()-dftSize.height >= 0);
        assert (B.height()-dftSize.height >= 0);
        assert (A.width()-dftSize.width >= 0);
        assert (B.width()-dftSize.width >= 0);

        Core.copyMakeBorder(A, roiA, (int)(Math.abs(A.height()-dftSize.height)/2), (int) (Math.abs(A.height()-dftSize.height)/2), (int)(Math.abs(A.width()-dftSize.width)/2), (int)(Math.abs(A.width()-dftSize.width)/2), Core.BORDER_CONSTANT, new Scalar(0,0,0));
        Core.copyMakeBorder(B, roiB, (int)(Math.abs(B.height()-dftSize.height)/2), (int) (Math.abs(B.height()-dftSize.height)/2), (int)(Math.abs(B.width()-dftSize.width)/2), (int)(Math.abs(B.width()-dftSize.width)/2), Core.BORDER_CONSTANT, new Scalar(0,0,0));
//        Core.copyMakeBorder(A, roiA, 50, 50, 50, (int)50, Core.BORDER_CONSTANT, new Scalar(0,0,0));
//        Core.copyMakeBorder(B, roiB, 417, 417, 561, 561, Core.BORDER_CONSTANT, new Scalar(0,0,0));


        HighGui.imshow("roiA", roiA);
        HighGui.imshow("roiB", roiB);

        System.out.println("CvType: " + tempA.type());

        final Mat test = new Mat();
        tempA.convertTo(tempA, CvType.CV_32FC2);
        tempB.convertTo(tempB, CvType.CV_32FC2);

        System.out.println(tempA.type());

        System.out.println(tempA.type() ==  CvType.CV_32FC2);

//        final List<Mat> channels = new ArrayList<>();
//        Core.split(tempA, channels);
//        HighGui.imshow("channel 1", channels.get(1));



        HighGui.waitKey(1);


        // now transform the padded A & B in-place;
        // use "nonzeroRows" hint for faster processing
        Core.dft(tempA, tempA, 0, A.rows());
        Core.dft(tempB, tempB, 0, B.rows());

        // multiply the spectrums;
        // the function handles packed spectrum representations well
        Core.mulSpectrums(tempA, tempB, tempA, 0);

        // transform the product back from the frequency domain.
        // Even though all the result rows will be non-zero,
        // you need only the first C.rows of them, and thus you
        // pass nonzeroRows == C.rows
        Core.dft(tempA, tempA, DFT_INVERSE + DFT_SCALE, C.rows());

        // now copy the result back to C.
        tempA.submat(new Rect(0, 0, C.cols(), C.rows())).copyTo(C);

    }
}