package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.agitator.Agitator;
import frc.robot.subsystems.shooter.Shooter;

public class BallHandlingCommands extends Command {
  private BallHandlingCommands() {}

  public static Command spinAgitator(Agitator agitator, Double SpeedSupplier) {
    return Commands.run(
        () -> {
          agitator.runAgitator(0.75);
        },
        agitator);
  }

  public static Command stopgitator(Agitator agitator) {
    return Commands.run(
        () -> {
          agitator.runAgitator(0.0);
        },
        agitator);
  }

  public static Command spinFeeder(Shooter shooter, Double SpeedSupplier) {
    return Commands.run(
        () -> {
          shooter.setFeedSpeed(SpeedSupplier);
        },
        shooter);
  }

  public static Command stopFeeder(Shooter shooter) {
    return Commands.run(
        () -> {
          shooter.setFeedSpeed(0.0);
        },
        shooter);
  }

  public static Command spinShooter(Shooter shooter, Double SpeedSupplier) {
    return Commands.run(
        () -> {
          shooter.setShootSpeed(SpeedSupplier);
        },
        shooter);
  }

  public static Command stopShooter(Shooter shooter) {
    return Commands.run(
        () -> {
          shooter.setShootSpeed(0.0);
        },
        shooter);
  }

  public static Command setRPM(Shooter shooter) {
    return Commands.sequence(
    Commands.run(
        () -> {
          shooter.setShootVelocity();
        },
        shooter).withTimeout(.5),
    Commands.run(()-> shooter.runFeeder(), shooter));
  }

  public static Command setShooterRpm(Shooter shooter, double rpm) {
    return Commands.sequence(
        BallHandlingCommands.spinShooter(shooter, 0.65).withTimeout(0.5),
        BallHandlingCommands.spinFeeder(shooter, 0.5).withTimeout(0.5));
  }
  // public static Command setMotorRpm(Shooter shooter, double rpm) {
  //   return Commands.sequence(
  //   Commands.run(
  //       () -> {
  //         shooter.setMotorRpm(rpm);
  //       },
  //       shooter).withTimeout(.5),
  //   Commands.run(()-> shooter.runFeeder(), shooter));
  // }
}
