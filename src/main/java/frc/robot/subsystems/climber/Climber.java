package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Climber extends SubsystemBase {
  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();

  public Climber(ClimberIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(null);
    Logger.processInputs("climber", inputs);
  }

  public void runClimber(double speed) {
    io.setSpeed(speed);
  }

  public void stop() {
    io.setSpeed(0);
  }
}
