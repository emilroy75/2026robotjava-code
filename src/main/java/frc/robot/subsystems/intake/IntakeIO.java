package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
  @AutoLog
  class IntakeIOInputs {
    public double pivotPositionRad = 0.0;
    public double pivotVelocityRadPerSec = 0.0;
    public double rollerVelocityRPM = 0.0;
    public double pivotAppliedVolts = 0.0;
    public double rollerAppliedVolts = 0.0;
  }

  default void updateInputs(IntakeIOInputs inputs) {}

  default void setPivotSpeed(double speed) {}

  default void setRollerSpeed(double speed) {}

  default void stop() {}
}
