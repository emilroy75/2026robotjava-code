package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class IntakeIOSim implements IntakeIO {
  private final DCMotorSim pivotSim =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.01, 25.0), DCMotor.getNEO(1));
  private final DCMotorSim rollerSim =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.001, 2.18), DCMotor.getNEO(1));

  private double pivotAppliedVolts = 0.0;
  private double rollerAppliedVolts = 0.0;

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    pivotSim.update(0.02);
    rollerSim.update(0.02);

    inputs.pivotPositionRad = pivotSim.getAngularPositionRad();
    inputs.pivotVelocityRadPerSec = pivotSim.getAngularVelocityRadPerSec();
    inputs.rollerVelocityRPM = rollerSim.getAngularVelocityRPM();
    inputs.pivotAppliedVolts = pivotAppliedVolts;
    inputs.rollerAppliedVolts = rollerAppliedVolts;
  }

  @Override
  public void setPivotSpeed(double speed) {
    pivotAppliedVolts = speed * 12.0;
    pivotSim.setInputVoltage(pivotAppliedVolts);
  }

  @Override
  public void setRollerSpeed(double speed) {
    rollerAppliedVolts = speed * 12.0;
    rollerSim.setInputVoltage(rollerAppliedVolts);
  }

  @Override
  public void stop() {
    pivotAppliedVolts = 0.0;
    rollerAppliedVolts = 0.0;
    pivotSim.setInputVoltage(0.0);
    rollerSim.setInputVoltage(0.0);
  }
}
