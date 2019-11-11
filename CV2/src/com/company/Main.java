package com.company;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        final Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(final PointValuePair o1, final PointValuePair o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                } else if (o1.getValue() < o2.getValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        final Mat image = Imgcodecs.imread("/home/andrea/Desktop/query.png");
        final VideoCapture video = new VideoCapture(0);

        final double[][] simplex = {
                {1,1,1},
                {1.2, 1, 1},
                {1, 1.2, 1},
                {1, 1, 1.2}
        };
        final double[] startSimplex = {1,1,1};

//        final Mat frame = new Mat();
        final Mat frame = Imgcodecs.imread("/home/andrea/Desktop/target.png");

        final MultivariateFunction multivariateFunction = new MultivariateFunction() {
            @Override
            public double value(double[] doubles) {
                final long t1 = System.currentTimeMillis();
                final ImageTransformation imageTransformation = new ImageTransformation(doubles[0], doubles[1], doubles[2]);
                final Mat transformed = imageTransformation.transform(image);
                final long t2 = System.currentTimeMillis();
                System.out.println("Time for transformation: " + (t2-t1));
                final Convolution convolution = new Convolution(transformed, frame);
                System.out.println("Time for convolution: " + (System.currentTimeMillis()-t2));
                System.out.println("Time for iteration: " + (System.currentTimeMillis()-t1));
                return convolution.getMax();
            }
        };

        final NelderMeadSimplex nms = new NelderMeadSimplex(simplex);
        nms.build(startSimplex);

//        video.read(frame);
        while (true) {
//            video.read(frame);
//            HighGui.imshow("frame", frame);
//
//            PointValuePair best = new PointValuePair(null, 0);
//            final ImageTransformation imageTransformation = new ImageTransformation(0.8,1,1);
//            final Mat transformed = imageTransformation.transform(image);
//            HighGui.imshow("transformed", transformed);
//
//            final Convolution convolution = new Convolution(transformed, frame);
//            final Mat convolved = convolution.finalConvolution;
//            HighGui.imshow("convolved", convolved);
//            for (int i = 0; i < 30; i++) {
//                final long t1 = System.currentTimeMillis();
//                nms.iterate(multivariateFunction, comparator);
//
//                best = nms.getPoint(0);
//                final PointValuePair[] pointValuePair = nms.getPoints();
//                for (final PointValuePair point : pointValuePair) {
//                    if (point.getValue() > best.getValue()) {
//                        best = point;
//                    }
//                    System.out.println("Point: " + Arrays.toString(point.getPoint()) + "  Value: " + point.getValue());
//                }
//                final long t2 = System.currentTimeMillis();
//                System.out.println("Time taken: " + (t2-t1));
//            }
//
//            final ImageTransformation transformation = new ImageTransformation(best.getPoint()[0], best.getPoint()[1], best.getPoint()[2]);
//            final Convolution convolution1 = new Convolution(transformation.transform(image), frame);
//            Imgproc.circle(frame, convolution1.maxLoc, 1, new Scalar(255, 0, 0), 10);



//            final long t1 = System.currentTimeMillis();
//            final double k = 2;
//            final double c = 0.8;
//            final double h = 1.5;
//            final ImageTransformation imageTransformation = new ImageTransformation(k, c, h);
//            final Mat transformed = imageTransformation.transform(image);
//            Imgproc.resize(transformed, transformed, new Size(256, 256));
//            HighGui.imshow("transformed", transformed);
//
//
//            final long t2 = System.currentTimeMillis();
//            System.out.println("Time taken for transformation: " + (t2-t1));
////            final Convolution convolution = new Convolution(transformed, frame);
////            HighGui.imshow("convolution", convolution.finalConvolution);
////            System.out.println("Max of convolution: " + convolution.max);
//
//            final Mat kernel = new Mat();
//            transformed.convertTo(kernel, CvType.CV_32FC3, 1.0/255.0);
//            Core.subtract(kernel, Core.mean(kernel), kernel);
//
//            final Mat target = new Mat();
//            frame.convertTo(target, CvType.CV_32FC3, 1.0/255.0);
//
//            System.out.println(kernel.rows() + "x" + kernel.cols());
//
//            final Mat convolutionColor = new Mat();
////            Imgproc.filter2D(target, convolutionColor, CvType.CV_32FC3, kernel);
////            Core.getOptimalDFTSize()
////            Core.dft(target, convolutionColor);
////            final Mat convolution = new Mat();
////            convolutionColor.copyTo(convolution);
//
//            final Mat padded = new Mat();
//            Core.copyMakeBorder(frame, padded, 10, 10, 10, 10, Core.BORDER_CONSTANT, new Scalar(0,0,0));
//            HighGui.imshow("padded", padded);
//
////            HighGui.imshow("convolution", convolutionColor);
//
//
//            System.out.println("Time taken for convolutin: " + (System.currentTimeMillis()-t2));
//            System.out.println("Time taken: " + (System.currentTimeMillis()-t1));

//            HighGui.waitKey(1);

//            Core.copyMakeBorder(frame, frame, 20, 20, 20, 20, Core.BORDER_CONSTANT, new Scalar(0,0,0));
            HighGui.imshow("frame", frame);
            System.out.println(frame.height() + "x" + frame.width());
            final Mat C = new Mat();
            Convolution.convolveDFT(frame, image, C);
            HighGui.imshow("C", C);


            HighGui.waitKey();
        }
    }
}
