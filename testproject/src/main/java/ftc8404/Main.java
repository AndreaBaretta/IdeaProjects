package ftc8404;

import edu.wpi.first.wpilibj.trajectory.*;
import edu.wpi.first.wpilibj.geometry.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main( String[] args) {



        final Pose2d tStart = new Pose2d(new Translation2d(10d,10d), new Rotation2d(0));
        final Pose2d tEnd = new Pose2d(new Translation2d(300, 50), new Rotation2d(0));
        final List<Translation2d> waypoints = new ArrayList<Translation2d>() {};
        waypoints.add(new Translation2d(20,20));
        waypoints.add(new Translation2d(200,0));
        waypoints.add(new Translation2d(300,50));
        waypoints.add(new Translation2d(200,70));
        final Rotation2d r = new Rotation2d(0d);
        final TrajectoryConfig trajectoryConfig = new TrajectoryConfig(2, 1);
        final Trajectory trajectory  = TrajectoryGenerator.generateTrajectory(tStart, waypoints, tEnd, trajectoryConfig);

        for (double i = 0; i <= trajectory.getTotalTimeSeconds(); i+=0.2) {
            System.out.println("(" + trajectory.sample(i).poseMeters.getTranslation().getX() + "," + trajectory.sample(i).poseMeters.getTranslation().getY() + ")");
        }
    }
}
