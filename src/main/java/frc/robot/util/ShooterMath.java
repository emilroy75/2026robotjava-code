package frc.robot.util;

import static frc.robot.subsystems.shooter.ShooterConstants.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Robot;

public class ShooterMath {

  public static double getDistanceToHub(Pose2d currentPose) {
    return currentPose.getTranslation().getDistance(Robot.HubPose.getTranslation());
  }

  public static double calculateTrajectoryVelocity(double distance) {
    double h = hubHeightMeters - shooterHeightMeters;
    double theta = launchAngleRad;

    double numerator = gravityMetersPerSec2 * Math.pow(distance, 2);
    double denominator = 2 * Math.pow(Math.cos(theta), 2) * (distance * Math.tan(theta) - h);
    return Math.sqrt(Math.abs(numerator / denominator));
  }

  public static double velocityToRPM(double velocity) {
    double wheelRPM = (velocity / wheelRadiusMeters) * 60.0 / (2.0 * Math.PI);
    return wheelRPM * shootBeltRatio;
  }

  public static double calculateEnergyCompensation(double velocity) {
    double ballEnergy = 0.5 * ballMassKg * Math.pow(velocity, 2);
    double requiredEnergy = ballEnergy / shooterEfficiency;
    double deltaOmega = Math.sqrt((2 * requiredEnergy) / totalWheelMOI);
    return (deltaOmega * 60.0) / (2.0 * Math.PI);
  }

  public static double getFinalRPM(double distance) {
    double v = calculateTrajectoryVelocity(distance);
    return velocityToRPM(v) + calculateEnergyCompensation(v);
  }

  public static Rotation2d getAngleToHub(Pose2d currentPose2d) {
    double dx = Robot.HubPose.getX() - currentPose2d.getX();
    double dy = Robot.HubPose.getY() - currentPose2d.getY();
    return new Rotation2d(Math.atan2(dy, dx));
  }
}
