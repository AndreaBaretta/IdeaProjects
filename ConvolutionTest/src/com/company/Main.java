package com.company;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

//        final Mat kernel8bit = Imgcodecs.imread("/home/andrea/Desktop/skystone2.png");
//        final Mat kernel = new Mat();
//        Imgproc.resize(kernel8bit, kernel8bit, new Size((int)Math.round(kernel8bit.width()/3), (int) Math.round(kernel8bit.height()/3)));
//        kernel8bit.convertTo(kernel8bit, CvType.CV_8U, 0.6);
//        kernel8bit.convertTo(kernel, CvType.CV_32FC3, 1.0/255.0);
//        Core.subtract(kernel, Core.mean(kernel), kernel);
        final VideoCapture video = new VideoCapture(1);

        while (true) {
//            HighGui.imshow("kernel", kernel8bit);

            final Mat frame = new Mat();
            video.read(frame);
            System.out.println(frame.height() + "x" + frame.width());
            final Mat rangeFrame = new Mat();
            Core.inRange(frame, new Scalar(15, 100, 60), new Scalar(85, 255, 255), rangeFrame);
            Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(5,5), 1);
            Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(7,7), 1);
            Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(13,13), 1);
            final Mat hierarchy = new Mat();
            final List<MatOfPoint> contours = new ArrayList<>();
//            System.out.println(frame.empty());
            Imgproc.findContours(rangeFrame, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            Imgproc.drawContours(rangeFrame, contours, -5, new Scalar(255,0,0));
            HighGui.imshow("rangeframe", rangeFrame);
            HighGui.imshow("frame", frame);

//            System.out.println(rangeFrame.channels());
//
//
//            long t1 = System.currentTimeMillis();
//
//
//            final Mat target = new Mat();
//            frame.convertTo(target, CvType.CV_32FC3, 1.0 / 255.0);
//
////        final Mat target8bit = Imgcodecs.imread("/home/andrea/Desktop/target1.jpg");
////        Imgproc.resize(target8bit, target8bit, new Size(1024,1024));
////        final Mat target = new Mat();
////        target8bit.convertTo(target, CvType.CV_32FC3, 1.0/255.0);
////        HighGui.imshow("target", target8bit);
//
//            final Mat convolutionColor = new Mat();
//            Imgproc.filter2D(target, convolutionColor, CvType.CV_32FC3, kernel);
//
//            final Mat convolution = new Mat();
//
////        Imgproc.cvtColor(convolutionColor, convolution, Imgproc.COLOR_BGR2GRAY);
//            convolutionColor.copyTo(convolution);
//
//            final List<Mat> convolutionChannels = new ArrayList<>(3);
//            Core.split(convolution, convolutionChannels);
//
////        System.out.println(convolution.);
//
//            for (int i = 0; i < 3; i++) {
//                Core.multiply(convolutionChannels.get(i), convolutionChannels.get(i), convolutionChannels.get(i));
//            }
//
//            final Mat normConvolution = new Mat();
//            Core.add(convolutionChannels.get(0), convolutionChannels.get(1), normConvolution);
//            Core.add(normConvolution, convolutionChannels.get(2), normConvolution);
//
//            System.out.println(normConvolution.channels());
//            final Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(normConvolution);
//            final double cmin, cmax;
//            cmin = minMaxLocResult.minVal;
//            cmax = minMaxLocResult.maxVal;
//
//
//            System.out.println("" + cmax + " - " + cmin);
//            Core.subtract(normConvolution, new Scalar(cmin), normConvolution);
//            Core.divide(normConvolution, new Scalar(cmax - cmin), normConvolution);
//
//
//            normConvolution.convertTo(normConvolution, CvType.CV_8U, 255);
//
//            final Core.MinMaxLocResult minMaxLocResultEnd = Core.minMaxLoc(normConvolution);
//            final double cmin2, cmax2;
//            cmin2 = minMaxLocResultEnd.minVal;
//            cmax2 = minMaxLocResultEnd.maxVal;
//
//            Core.inRange(normConvolution, new Scalar(200), new Scalar(255), normConvolution);
//            Imgproc.circle(normConvolution, minMaxLocResult.maxLoc, 10, new Scalar(0,0,255));
//
//            HighGui.imshow("convolution", normConvolution);
//
//            long t2 = System.currentTimeMillis();
//
//            System.out.println("cmin2 = " + cmin2 + "  cmax2 = " + cmax2);

//            System.out.println("time taken : " + (t2 - t1) + "ms");

            HighGui.waitKey(1);
        }
    }
}
