import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Comparator;

public class ImageSearch {
    final MatOfDouble dist = new MatOfDouble();
    final Mat mtx = new Mat(3,3, CvType.CV_64F);
    final MultivariateFunction multivariateFunction;
    final double staticImgMagnitude;
    final Mat frame;
    final Mat staticImg;
    final Comparator<PointValuePair> comparator;

    public ImageSearch() {
        dist.put(0,0,0.04487421523978265); //Temporary fix
        dist.put(0,1,-0.29995316212300066);
        dist.put(0,2,-0.005613453102160556);
        dist.put(0,3,0.008472930208135405);
        dist.put(0,4,0.52801555059531);

        mtx.put(0,0, 910.4593644295832d);
        mtx.put(0,1, 0.0d);
        mtx.put(0,2, 0.0d);
        mtx.put(1,0, 0.0d);
        mtx.put(1,1, 911.1676748321776d);
        mtx.put(1,2, 0.0d);
        mtx.put(2,0, 0.0d);
        mtx.put(2,1, 0.0d);
        mtx.put(2,2, 1.0d);


        staticImg = Imgcodecs.imread("/home/andrea/Desktop/rover.png");
        frame = Imgcodecs.imread("/home/andrea/Desktop/The_Fonz.jpg");
        staticImgMagnitude = Math.sqrt(staticImg.dot(staticImg));

        comparator = new Comparator<PointValuePair>() {
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

        multivariateFunction = new MultivariateFunction() {
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


    }
}
