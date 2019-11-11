package com.company;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

public class ImageTransformation {
    protected final double k;
    protected final double c;
    protected final double h;
    public ImageTransformation(final double k, final double c, final double h) {
        this.k = k;
        this.c = c;
        this.h = h;
    }

    public Mat transform(final Mat image) {

        final Mat dst = new Mat();
        Imgproc.resize(image, dst, new Size(image.width()*k, image.height()*k));

        final Mat imageCropped = dst;




        final MatOfPoint2f newSrcPoints = new MatOfPoint2f(
                new Point(0, 0), new Point(imageCropped.cols()-1, 0), new Point(imageCropped.cols()-1, imageCropped.rows()-1), new Point(0, imageCropped.rows()-1)
        );

        final int newLeftHeight = (int) Math.floor(imageCropped.rows()*Math.min(1,h));
        final int newRightHeight = (int) Math.floor(imageCropped.rows()*Math.min(1,1/h));
        final int pinchSizeHeightRight = (imageCropped.rows()-newRightHeight)/2;
        final int pinchSizeHeightLeft = (imageCropped.rows()-newLeftHeight)/2;

        final int newTopWidth = (int) Math.floor(imageCropped.cols()*Math.min(1, c));
        final int newBottomWidth = (int) Math.floor(imageCropped.cols()*Math.min(1, 1/c));
        final int pinchSizeWidthTop = (imageCropped.cols()-newTopWidth)/2;
        final int pinchSizeWidthBottom = (imageCropped.cols()-newBottomWidth)/2;
        final MatOfPoint2f dstPoints = new MatOfPoint2f(
                new Point(pinchSizeWidthTop, pinchSizeHeightLeft),
                new Point(imageCropped.cols()-pinchSizeWidthTop, pinchSizeHeightRight),
                new Point(imageCropped.cols()-pinchSizeWidthBottom, imageCropped.rows()-pinchSizeHeightRight),
                new Point(pinchSizeWidthBottom, imageCropped.rows()-pinchSizeHeightLeft)
        );
        final Mat dst3 = new Mat();
        final Mat M2 = Calib3d.findHomography(newSrcPoints, dstPoints);
        Imgproc.warpPerspective(imageCropped, dst3, M2, imageCropped.size());


        return dst3;
    }
}
