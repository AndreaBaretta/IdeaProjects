import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;

import java.util.Comparator;

public class IterativeSearch {
    final Mat staticImg;
    final Mat searchImg;
    final NelderMeadSimplex nms;
    final MultivariateFunction multivariateFunction;
    final Comparator comp;

    public IterativeSearch(final Mat staticImg, final Mat searchImg, final MultivariateFunction multivariateFunction, final Comparator comp,
                           final double[][] startSimplex, final double[] startGuess) {

        this.staticImg = staticImg;
        this.searchImg = searchImg;
        this.comp = comp;
        this.multivariateFunction = multivariateFunction;
        nms = new NelderMeadSimplex(startSimplex);
        nms.build(startGuess);
    }

    public PointValuePair searchMaxIter(final int maxIterations) {
        PointValuePair bestPair = null;
        for (int i = 0; i < maxIterations; i++) {
            nms.iterate(multivariateFunction, comp);

            final PointValuePair[] pairs = nms.getPoints();
            bestPair = pairs[0];
            for (int n = 1; n < pairs.length; n++) {
                if (bestPair.getValue() > pairs[n].getValue()) {
                    bestPair = pairs[n];
                }
            }
        }

        return bestPair;
    }

    public PointValuePair searchMaxTime(final double millis) {
        PointValuePair bestPair = null;
        final long t1 = System.currentTimeMillis();
        while (System.currentTimeMillis() - t1 <= millis) {
            nms.iterate(multivariateFunction, comp);

            final PointValuePair[] pairs = nms.getPoints();
            bestPair = pairs[0];
            for (int n = 1; n < pairs.length; n++) {
                if (bestPair.getValue() > pairs[n].getValue()) {
                    bestPair = pairs[n];
                }
            }
        }

        return bestPair;
    }
}
