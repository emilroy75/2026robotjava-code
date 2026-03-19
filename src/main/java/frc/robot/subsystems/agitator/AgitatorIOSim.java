package frc.robot.subsystems.agitator;

import static frc.robot.subsystems.agitator.AgitatorConstants.*;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class AgitatorIOSim implements AgitatorIO {
  private final DCMotorSim sim =
      new DCMotorSim(
          LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.01, gearReduction),
          DCMotor.getNEO(1));

  private double appliedVolts = 0.0;

  @Override
  public void updateInputs(AgitatorIOInputs inputs) {
    sim.update(0.02);
    inputs.velocityRPM = sim.getAngularVelocityRPM();
    inputs.appliedVolts = appliedVolts;
    inputs.currentAmps = Math.abs(sim.getCurrentDrawAmps());
  }

  @Override
  public void setSpeed(double speed) {
    appliedVolts = speed * 12.0;
    sim.setInputVoltage(appliedVolts);
  }

  @Override
  public void stop() {
    appliedVolts = 0.0;
    sim.setInputVoltage(0.0);
  }
}
