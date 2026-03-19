package frc.robot.subsystems.agitator;

import static frc.robot.subsystems.agitator.AgitatorConstants.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class AgitatorIOSpark implements AgitatorIO {
  private final SparkMax motor;

  public AgitatorIOSpark() {
    motor = new SparkMax(agitatorId, MotorType.kBrushless);
    var config = new SparkMaxConfig();
    config.smartCurrentLimit(currentLimitAmps);
    motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void updateInputs(AgitatorIOInputs inputs) {
    inputs.velocityRPM = motor.getEncoder().getVelocity();
    inputs.appliedVolts = motor.getAppliedOutput() * motor.getBusVoltage();
    inputs.currentAmps = motor.getOutputCurrent();
  }

  @Override
  public void setSpeed(double speed) {
    motor.set(speed);
  }

  @Override
  public void stop() {
    motor.set(0.0);
  }
}
