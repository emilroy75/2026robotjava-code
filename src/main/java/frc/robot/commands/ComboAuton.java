package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.agitator.Agitator;
import frc.robot.subsystems.shooter.Shooter;

public class ComboAuton {
  public static Command shootFuel(Shooter shooter, Agitator agitator) {
    return Commands.sequence(
        BallHandlingCommands.spinShooter(shooter, 0.75).withTimeout(3.0),
        BallHandlingCommands.spinFeeder(shooter, 0.5).withTimeout(3.0),
        BallHandlingCommands.spinAgitator(agitator, 0.5).withTimeout(3.0),
        BallHandlingCommands.spinAgitator(agitator, 0.8).withTimeout(7.0),
        BallHandlingCommands.stopgitator(agitator).withTimeout(0.1),
        BallHandlingCommands.stopFeeder(shooter).withTimeout(0.1),
        BallHandlingCommands.stopShooter(shooter).withTimeout(0.1));
  }
}
