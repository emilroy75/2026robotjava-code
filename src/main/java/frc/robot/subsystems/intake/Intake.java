package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
  private final IntakeIO io;
  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

  public Intake(IntakeIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Intake", inputs);
  }

  public void setPivotSpeed(double speed) {
    io.setPivotSpeed(speed);
  }

  public void setRollerSpeed(double speed) {
    io.setRollerSpeed(speed);
  }

  public void stop() {
    io.stop();
  }
  public void deploy(){
    io.setPivotSpeed(0.3);
  }
  public void retract(){
    io.setPivotSpeed(-0.3);
  }
  public double gtPivotPosition() {
    return inputs.pivotPositionRad;
  }
}
