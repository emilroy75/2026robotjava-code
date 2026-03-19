package frc.robot.subsystems.agitator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Agitator extends SubsystemBase {
  private final AgitatorIO io;
  private final AgitatorIOInputsAutoLogged inputs = new AgitatorIOInputsAutoLogged();

  public Agitator(AgitatorIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Agitator", inputs);
  }

  public void runAgitator(double speed) {
    io.setSpeed(speed);
  }

  public void stop() {
    io.stop();
  }
}
