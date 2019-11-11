import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.MultiDirectionalSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void drawPoly(final Mat frame, final double[] doubles, final Scalar scalar) {
        Imgproc.line(frame, new Point(doubles[0], doubles[1]), new Point(doubles[2], doubles[3]), scalar, 5);
        Imgproc.line(frame, new Point(doubles[2], doubles[3]), new Point(doubles[6], doubles[7]), scalar, 5);
        Imgproc.line(frame, new Point(doubles[6], doubles[7]), new Point(doubles[4], doubles[5]), scalar, 5);
        Imgproc.line(frame, new Point(doubles[4], doubles[5]), new Point(doubles[0], doubles[1]), scalar, 5);
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Hello World!");

        final MatOfDouble dist = new MatOfDouble();
        dist.put(0,0,0.04487421523978265); //Temporary fix
        dist.put(0,1,-0.29995316212300066);
        dist.put(0,2,-0.005613453102160556);
        dist.put(0,3,0.008472930208135405);
        dist.put(0,4,0.52801555059531);

//        dist.put(0,0,0.25914934776336845);
//        dist.put(0,1,-1.2418293673310672);
//        dist.put(0,2,-0.004796792777258449);
//        dist.put(0,3,0.0017042559733992787);
//        dist.put(0,4,1.7213300321262446);

        final Mat mtx = new Mat(3,3,CvType.CV_64F);
        mtx.put(0,0, 910.4593644295832d);
        mtx.put(0,1, 0.0d);
//        mtx.put(0,2, 629.7548686838691d);
        mtx.put(0,2, 0.0d);
        mtx.put(1,0, 0.0d);
        mtx.put(1,1, 911.1676748321776d);
//        mtx.put(1,2, 376.29649395967107d);
        mtx.put(1,2, 0.0d);
        mtx.put(2,0, 0.0d);
        mtx.put(2,1, 0.0d);
        mtx.put(2,2, 1.0d);


//        mtx.put(0,0, 922.8883642217764d);
//        mtx.put(0,1, 0.0d);
//        mtx.put(0,2, 649.0906636112965d);
////        mtx.put(0,2, 0.0d);
//        mtx.put(1,0, 0.0d);
//        mtx.put(1,1, 893.144369813485d);
//        mtx.put(1,2, 354.5748673471632d);
////        mtx.put(1,2, 0.0d);
//        mtx.put(2,0, 0.0d);
//        mtx.put(2,1, 0.0d);
//        mtx.put(2,2, 1.0d);


        final int width = 640;
        final int height = 480;
        final int verticalPinch = 160;
        final int horizontalPinch = 213;
        final Mat frame = new Mat();
        final Mat staticImg = Imgcodecs.imread("/home/andrea/Desktop/rover.png");
        Imgproc.resize(staticImg, staticImg, new Size(640, 480));

        final double staticImgMagnitude = Math.sqrt(staticImg.dot(staticImg));

        final Comparator<PointValuePair> compMin = new Comparator<PointValuePair>() {
            @Override
            public int compare(final PointValuePair o1, final PointValuePair o2) {
                if (o1.getValue() < o2.getValue()) {
                    return -1;
                } else if (o1.getValue() > o2.getValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        final Comparator<PointValuePair> compMax = new Comparator<PointValuePair>() {
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

        final MultivariateFunction multivariateFunction = new MultivariateFunction() {
            @Override
            public double value(double[] doubles) {
                final MatOfPoint2f dst = new MatOfPoint2f(
                        new Point(0, 0),
                        new Point(639, 0),
                        new Point(0, 479),
                        new Point(639, 479)
                );

                final MatOfPoint2f src = new MatOfPoint2f(
                        new Point(doubles[0], doubles[1]),
                        new Point(doubles[2], doubles[3]),
                        new Point(doubles[4], doubles[5]),
                        new Point(doubles[6], doubles[7])
                );

                final Mat M = Imgproc.getPerspectiveTransform(src, dst);
                final Mat result = new Mat(640, 480, CvType.CV_64F);
//                Log.d("ERROR", "" + result.type());
                Imgproc.warpPerspective(frame, result, M, staticImg.size());

//                result.convertTo(result, CvType.CV_64F);

//                Log.d("ERROR", "static : " + staticImg.size().toString() + "  " + staticImg.type());
//                Log.d("ERROR", "result : " + result.size().toString() + "  " + result.type());

                return staticImg.dot(result) / Math.sqrt(result.dot(result)) * staticImgMagnitude;
            }
        };

        final double[][] refSimplex = new double[][]{ //Setting up NM simplex, order is: x1, y1, x2, y2, x3, y3, x4, y4
                {0, 0, width-1, 0, 0, height-1, width-1, height-1}, // #1 Whole frame (opencv has origin at top left of the frame)
                {0, 0, width-1, 0, 0, height/2, width-1, height/2}, // #2 Top half
                {0, height/2, width-1, height/2, 0, height-1, width-1, height-1}, // #3 Bottom half
                {0, 0, width/2, 0, 0, height-1, width/2, height-1}, // #4 Right half
                {width/2, 0, width-1, 0, width/2, height-1, width-1, height-1}, // #5 Left half
                {horizontalPinch, 0, width-horizontalPinch, 0, 0, height-1, width-1, height-1}, // #6 Trapezoid, large base bottom of the frame, small one on top
                {0, 0, width-1, 0, horizontalPinch, height-1, width-horizontalPinch, height-1}, // #7 Trapezoid, large base top of the frame, small one on the bottom
                {0, 0, width-1, verticalPinch, 0, height-1, width-1, height-verticalPinch}, // #8 Trapezoid, large base left side of the frame, small one on the right
                {0, verticalPinch, width-1, 0, 0, height-verticalPinch, width-1, height-1} //#9 Trapezoid, large base right side of the frame, small one on the left

        };

//        final double[][] refSimplex = new double[][] { //Setting up NM simplex, down to 6 dimensions, order: x, y, x, rot1, rot2, rot3 (tvec then rvec)
//                {0, 0, 0, 0, 0, 0},
//                {1, 0, 0, 0, 0, 0},
//                {0, 1, 0, 0, 0, 0},
//                {0, 0, 1, 0, 0, 0},
//                {0, 0, 0, 1, 0, 0},
//                {0, 0, 0, 0, 1, 0},
//                {0, 0, 0, 0, 0, 1}
//        };


//        final double[][] generators = new double[8][];
//        final double[] refGuess = refSimplex[0];
//        for (int i_guess = 1; i_guess < 9; i_guess++) {
//            final double[] guess = refSimplex[i_guess];
//            double[] generator = new double[8];
//            for (int j_component = 0; j_component < 8; j_component++) {
//                generator[j_component] = guess[j_component] - refGuess[j_component];
//            }
//            generators[i_guess - 1] = generator;
//        }
//        final RealMatrix genMatrix = new Array2DRowRealMatrix(generators);
//        final SingularValueDecomposition svd = new SingularValueDecomposition(genMatrix);
//        System.out.println(Arrays.toString(svd.getSingularValues()));
//        assert (svd.getRank() == 8);

        final NelderMeadSimplex nms = new NelderMeadSimplex(refSimplex);
//        nms.build(new double[]{
//                0, 0, 50, 0, 0, 0
//        });

        nms.build(new double[] {
                0, 0, width-1, 0, 0, height-1, width-1, height-1
        });

        final VideoCapture video = new VideoCapture(1);
        final double[] doubles = new double[] {0, 0, 50, 0, 0, 0};

        video.read(frame);
//        for (int iteration = 0; iteration<350; iteration++) {
        while (true) {
//            Imgproc.resize(frame, frame, new Size(width, height));
            final Mat drawn = new Mat();

            nms.iterate(multivariateFunction, compMax);

            final PointValuePair[] pairs = nms.getPoints();
            PointValuePair bestPair = pairs[0];
            for (int i = 1; i < pairs.length; i++) {
                if (bestPair.getValue() > pairs[i].getValue()) {
                    bestPair = pairs[i];

                }
            }

            frame.copyTo(drawn);

            for (final PointValuePair guess : nms.getPoints()) {
                drawPoly(drawn, guess.getPoint(), new Scalar(255, 0, 0));
            }

            //System.out.println("
            // " + frame.size() + " best points: " + Arrays.toString(bestPair.getPoint()) + " value = " + bestPair.getValue());


//
//
//
//            final MatOfPoint2f dst = new MatOfPoint2f(
////                    new Point(0,0),
////                    new Point(639, 0),
////                    new Point(0, 479),
////                    new Point(639, 479)
//                    new Point(0, 479),
//                    new Point(639, 479),
//                    new Point(0,0),
//                    new Point(639, 0)
//            );
//
//
//            final Mat M = Imgproc.getPerspectiveTransform(reprojectPts, dst);
//            final Mat result = new Mat();
//            Imgproc.warpPerspective(frame, result, M, staticImg.size());
//            HighGui.imshow("warped", result);
//
//            final double cosAngle = staticImg.dot(result) / (Math.sqrt(result.dot(result)) * staticImgMagnitude);
//
//            System.out.println("simplex = " + Arrays.toString(doubles) + " reprojected points = " + Arrays.toString(doubleArrayPts) + " value = " + cosAngle);










//            final double[] reprojectPts = new double[] {
//                    arrayPts[0].x, arrayPts[0].y, arrayPts[1].x, arrayPts[1].y, arrayPts[2].x, arrayPts[2].y, arrayPts[3].x, arrayPts[3].y
//            };

//            final MatOfPoint3f objectPoints = new MatOfPoint3f(
//                    new Point3(-13.97,10.795,0),
//                    new Point3(13.97,10.795,0),
//                    new Point3(-13.97,-10.795,0),
//                    new Point3(13.97,-10.795,0)
//            );

//            final MatOfPoint3f objectPoints = new MatOfPoint3f(
//                    new Point3(-13.97,10.795,0),
//                    new Point3(13.97,10.795,0),
//                    new Point3(-13.97,-10.795,0),
//                    new Point3(13.97,-10.795,0)
//            );
//
//            final MatOfPoint2f imagePoints = new MatOfPoint2f(
//                    new Point(bestPair.getPoint()[0]+dx, bestPair.getPoint()[1]+dy),
//                    new Point(bestPair.getPoint()[2]+dx, bestPair.getPoint()[3]+dy),
//                    new Point(bestPair.getPoint()[4]+dx, bestPair.getPoint()[5]+dy),
//                    new Point(bestPair.getPoint()[6]+dx, bestPair.getPoint()[7]+dy)
//            );
//
//            final Mat tvec = new Mat(3,1,CvType.CV_64F);
//            final Mat rvec = new Mat(3,1,CvType.CV_64F);
//
//            Calib3d.solvePnP(objectPoints, imagePoints, mtx, dist, rvec, tvec);
//
//            final MatOfPoint2f check = new MatOfPoint2f();
//            Calib3d.projectPoints(objectPoints, rvec, tvec, mtx, dist, check);
//            final Point[] array = check.toArray();
//            final double[] toBeDrawn = new double[] {
//                    array[0].x-dx, array[0].y-dy, array[1].x-dx, array[1].y-dy, array[2].x-dx, array[2].y-dy, array[3].x-dx, array[3].y-dy,
//            };
//
//            final Mat tvec1 = new Mat(3,1,CvType.CV_64F);
//            final Mat rvec1 = new Mat(3,1,CvType.CV_64F);
//
//            Calib3d.solvePnP(objectPoints, check, mtx, dist, rvec1, tvec1);
//
//            final double s = 40/57.4866;
//
//            System.out.println("tvec = [" + tvec.get(0,0)[0]*s + ", " + tvec.get(1,0)[0]*s + ", " + tvec.get(2,0)[0]*s + "]");
////            drawPoly(drawn, toBeDrawn, new Scalar(0,0,255));
//
//
//
//            for (final PointValuePair guess : nms.getPoints()) {
////                final Mat tvec = new Mat(3,1,CvType.CV_64F);
////                tvec.put(0,0, guess.getPoint()[0]);
////                tvec.put(1,0, guess.getPoint()[1]);
////                tvec.put(2,0, guess.getPoint()[2]);
////                final Mat rvec = new Mat(3,1,CvType.CV_64F);
////                rvec.put(0,0, guess.getPoint()[3]);
////                rvec.put(1,0, guess.getPoint()[4]);
////                rvec.put(2,0, guess.getPoint()[5]);
////
////                final MatOfPoint2f points = new MatOfPoint2f();
////
////                Calib3d.projectPoints(objectPoints, rvec, tvec, mtx, dist, points);
////
////                final Point[] arrayPts = points.toArray();
////
////                final double[] reprojectPts = new double[] {
////                        arrayPts[0].x+dx, arrayPts[0].y+dy, arrayPts[1].x+dx, arrayPts[1].y+dy,
////                        arrayPts[2].x+dx, arrayPts[2].y+dy, arrayPts[3].x+dx, arrayPts[3].y+dy
////                };
//
//
//
//                drawPoly(drawn, guess.getPoint(), new Scalar(255,0,0));
//            }

            HighGui.imshow("reprojected", drawn);
//            final Mat warped = new Mat();
//            HighGui.imshow("warped", warped);
            HighGui.waitKey(1);
        }
    }
}