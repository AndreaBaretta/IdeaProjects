package com.company;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SamplingCV {

    final int cutoffUpper; //200
    final int cutoffLower; //400
    final Scalar lowerBound; //new Scalar(15, 100, 60)
    final Scalar upperBound; //new Scalar(5, 255, 255)

    public SamplingCV(final int cutoffUpper, final int cutoffLower, final Scalar lowerBound, final Scalar upperBound) {
        this.cutoffUpper = cutoffUpper;
        this.cutoffLower = cutoffLower;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void search(final Mat frame) {
        final Mat rangeFrame = frame.clone();

        Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(5,5), 1);
        Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(7,7), 1);
        Imgproc.GaussianBlur(rangeFrame, rangeFrame, new Size(13,13), 1);
        final Mat blur = rangeFrame.clone();
        HighGui.imshow("blur", blur);


        Core.inRange(rangeFrame, lowerBound, upperBound, rangeFrame);

        final double firstThird = frame.width()/3;
        final double secondThird = frame.width()*2/3;
        final double thirdThird = frame.width();

        final Rect sample1Rect = new Rect(new Point(40, cutoffUpper), new Point(rangeFrame.width()/3 - 40, cutoffLower));
        final Mat sample1 = rangeFrame.submat(sample1Rect);
        final double sample1Value = Core.sumElems(sample1).val[0];
        Imgproc.rectangle(frame, sample1Rect.tl(), sample1Rect.br(), new Scalar(0,0,255));

        final Rect sample2Rect = new Rect(new Point(rangeFrame.width()/3 + 40, cutoffUpper), new Point(rangeFrame.width()*2/3 - 40, cutoffLower));
        final Mat sample2 = rangeFrame.submat(sample2Rect);
        final double sample2Value = Core.sumElems(sample2).val[0];
        Imgproc.rectangle(frame, sample2Rect.tl(), sample2Rect.br(), new Scalar(0,0,255));

        final Rect sample3Rect = new Rect(new Point(rangeFrame.width()*2/3 + 40, cutoffUpper), new Point(rangeFrame.width() - 40, cutoffLower));
        final Mat sample3 = rangeFrame.submat(sample3Rect);
        final double sample3Value = Core.sumElems(sample3).val[0];
        Imgproc.rectangle(frame, sample3Rect.tl(), sample3Rect.br(), new Scalar(0,0,255));

        Imgproc.line(frame, new Point(firstThird, 0), new Point(firstThird, frame.height()), new Scalar(0,255,0), 10);
        Imgproc.line(frame, new Point(secondThird, 0), new Point(secondThird, frame.height()), new Scalar(0,255,0), 10);

        double leastValue = sample1Value;
        int position = 1;

        if (sample2Value < leastValue) {
            leastValue = sample2Value;
            position = 2;
        }
        if (sample3Value < leastValue) {
            leastValue = sample3Value;
            position = 3;
        }

        System.out.println("Position 1: " + sample1Value + "   Position 2: " + sample2Value + "   Position 3: " + sample3Value);
//        System.out.println("Position: " + position + "  Value: " + leastValue);

//        final Mat hierarchy = new Mat();
//        final List<MatOfPoint> contours = new ArrayList<>();

//        Imgproc.findContours(croppedFrame, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
////        Imgproc.drawContours(croppedFrame, contours, -5, new Scalar(255,0,0));
//        Imgproc.resize(croppedFrame, croppedFrame, frame.size());
//        HighGui.imshow("croppedFrame", croppedFrame);
//
//        final Mat rectContour = frame.clone();
//
//        Imgproc.line(rectContour, new Point(0,cutoffUpper), new Point(rectContour.width()-1, cutoffUpper), new Scalar(255,0,0), 10);
//        Imgproc.line(rectContour, new Point(0,cutoffLower), new Point(rectContour.width()-1, cutoffLower), new Scalar(255,0,0), 10);
//
//        final double firstThird = rectContour.width()/3;
//        final double secondThird = rectContour.width()*2/3;
//        final double thirdThird = rectContour.width();
//
//        Imgproc.line(rectContour, new Point(firstThird, 0), new Point(firstThird, rectContour.height()), new Scalar(0,255,0), 10);
//        Imgproc.line(rectContour, new Point(secondThird, 0), new Point(secondThird, rectContour.height()), new Scalar(0,255,0), 10);
//
//
//        final List<Rect> rects = new ArrayList<>();
//        final double areaCap = 500;
//        for (final MatOfPoint contour : contours) {
//            final Rect rect = Imgproc.boundingRect(contour);
//            if (rect.area() >= areaCap) {
//            rects.add(rect);
////                Imgproc.rectangle(rectContour, rect.tl(), rect.br(), new Scalar(0, 255, 0), 10);
//                }
//            }
//
//        if (rects.size() >= 2) {
//
//            Rect largestRect = rects.get(0);
//            Rect secondLargestRect = rects.get(1);
//
//            for (final Rect rect : rects) {
//                if (rect.area() > largestRect.area()) {
//                    largestRect = rect;
//                } else if (rect.area() >= secondLargestRect.area()) {
//                    secondLargestRect = rect;
//                }
//            }
//
//            Imgproc.rectangle(rectContour, largestRect.tl(), largestRect.br(), new Scalar(0, 0, 255), 5);
//            Imgproc.rectangle(rectContour, secondLargestRect.tl(), secondLargestRect.br(), new Scalar(0, 0, 255), 5);
//
//
//        }

        HighGui.imshow("rangeFrame", rangeFrame);

        HighGui.imshow("frame", frame);
        HighGui.waitKey(1);
    }
}
