package com.company;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	// write your code here
        final VideoCapture video = new VideoCapture(1);
        final SamplingCV samplingCV = new SamplingCV(300, 400, new Scalar(15, 100, 60), new Scalar(85, 255, 255));
        while (true) {
            final long t1 = System.currentTimeMillis();
            final Mat frame = new Mat();
            video.read(frame);

            samplingCV.search(frame);
//            final Mat rangeFrame = frame.clone();
//
//            Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(5,5), 1);
//            Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(7,7), 1);
//            Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(13,13), 1);
//            final Mat blur = rangeFrame.clone();
//            HighGui.imshow("blur", blur);
//
//            Core.inRange(rangeFrame, new Scalar(15, 100, 60), new Scalar(85, 255, 255), rangeFrame);
//
//            final Mat hierarchy = new Mat();
//            final List<MatOfPoint> contours = new ArrayList<>();
//
//            Imgproc.findContours(rangeFrame, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//            Imgproc.drawContours(rangeFrame, contours, -5, new Scalar(255,0,0));
//            HighGui.imshow("rangeframe", rangeFrame);
//
//            final Mat rectContour = frame.clone();
//
//            final int cutoffUpper = 200;
//            final int cutoffLower = 400;
//            Imgproc.line(rectContour, new Point(0,cutoffUpper), new Point(rectContour.width()-1, 200), new Scalar(255,0,0), 10);
//            Imgproc.line(rectContour, new Point(0,cutoffLower), new Point(rectContour.width()-1, 400), new Scalar(255,0,0), 10);
//
//
//            final List<Rect> rects = new ArrayList<>();
//            final double areaCap = 500;
//            for (final MatOfPoint contour : contours) {
//                final Rect rect = Imgproc.boundingRect(contour);
//                if (rect.height*rect.height >= areaCap && rect.y <= cutoffLower && rect.y >= cutoffUpper) {
//                    rects.add(rect);
//                    Imgproc.rectangle(rectContour, rect.tl(), rect.br(), new Scalar(0, 255, 0), 10);
//                }
//            }
//            HighGui.imshow("rectContour", rectContour);
//
//            HighGui.imshow("frame", frame);
//            HighGui.waitKey(1);
//
//            final long t2 = System.currentTimeMillis();
//            System.out.println("Time taken in milliseconds:" + (t2-t1));
        }
    }
}
