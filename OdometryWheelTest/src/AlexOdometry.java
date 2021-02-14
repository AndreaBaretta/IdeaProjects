public class AlexOdometry {
    public static final double DRIVE_ENCODER_MM_PER_COUNT = 35 * Math.PI / 4000.0 * (2476.0 / 2483.0); // TODO tune
    public static final double LEFT_RIGHT_DRIVE_ENCODER_FROM_CENTER_MM = 181.0 * (3610.0 / 3600.0); //TODO tune
    public static final double CENTER_DRIVE_ENCODER_FROM_CENTER_MM = 181.0; // TODO tune
    public static double[] position = new double[]{0,0,0};
    public void update(final double leftEncDelta, final double rightEncDelta, final double centerEncDelta) {
        double leftEncMM = rightEncDelta * DRIVE_ENCODER_MM_PER_COUNT;
        double rightEncMM = rightEncDelta * DRIVE_ENCODER_MM_PER_COUNT;
        double centerEncMM = centerEncDelta * DRIVE_ENCODER_MM_PER_COUNT;

        // variables to simplify the expressions
        double L = leftEncMM;
        double R = rightEncMM;
        double C = centerEncMM;
        double P = LEFT_RIGHT_DRIVE_ENCODER_FROM_CENTER_MM;
        double Q = CENTER_DRIVE_ENCODER_FROM_CENTER_MM;

        // calculate the robot's new position and heading
        double curHeadingRad = position[2];
        double theta = (R - L) / (2 * P);

        double dXIntrinsMM = R * sinOverX(theta) - P * Math.sin(theta) + C * cosMinus1OverX(theta) - Q * (Math.cos(theta) - 1);
        double dYIntrinsMM = C * sinOverX(theta) - Q * Math.sin(theta) - R * cosMinus1OverX(theta) + P * (Math.cos(theta) - 1);

        position[2] = curHeadingRad + theta;

        position[0] += dXIntrinsMM * Math.cos(curHeadingRad) - dYIntrinsMM * Math.sin(curHeadingRad);
        position[1] += dYIntrinsMM * Math.cos(curHeadingRad) + dXIntrinsMM * Math.sin(curHeadingRad);
    }

    private double sinOverX(double x) {
        double threshold = 2;
        if (Math.abs(x) < threshold) {
            int n = 9;
            double result = 0;
            double numer = 1;
            double denom = 1;
            for (int i = 0; i < n; i++) {
                result += numer / denom;
                numer *= -x * x;
                denom *= (2 * i + 2) * (2 * i + 3);
            }
            return result;
        } else {
            return Math.sin(x) / x;
        }
    }

    /**
     * This will use a taylor series to compute (cos(x)-1)/x if x is small.
     * Otherwise, this will compute it using the built in trig functions.
     */
    private double cosMinus1OverX(double x) {
        double threshold = 2;
        if (Math.abs(x) < threshold) {
            int n = 9;
            double result = 0;
            double numer = -x;
            double denom = 2;
            for (int i = 0; i < n; i++) {
                result += numer / denom;
                numer *= -x * x;
                denom *= (2 * i + 3) * (2 * i + 4);
            }
            return result;
        } else {
            return (Math.cos(x) - 1) / x;
        }
    }
}
