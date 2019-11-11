import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

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

        System.out.println(normConvolution.channels());
        final Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(normConvolution);
        final double cmin, cmax;
        cmin = minMaxLocResult.minVal;
        cmax = minMaxLocResult.maxVal;

        Core.subtract(normConvolution, new Scalar(cmin), normConvolution);
        Core.divide(normConvolution, new Scalar(cmax - cmin), normConvolution);


        normConvolution.convertTo(normConvolution, CvType.CV_8U, 255);

        final Core.MinMaxLocResult minMaxLocResultEnd = Core.minMaxLoc(normConvolution);
//        final double cmin2, cmax2;
//        final double cmin2 = minMaxLocResultEnd.minVal;
        final double cmax2 = minMaxLocResultEnd.maxVal;

        Core.inRange(normConvolution, new Scalar(200), new Scalar(255), normConvolution);
//        Imgproc.circle(normConvolution, minMaxLocResult.maxLoc, 10, new Scalar(0,0,255));

        finalConvolution = normConvolution;
        max = cmax2;
        maxLoc = minMaxLocResultEnd.maxLoc;


    }
}