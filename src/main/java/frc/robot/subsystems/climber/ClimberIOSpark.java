package frc.robot.subsystems.climber;

import static frc.robot.subsystems.climber.ClimberConstants.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberIOSpark implements ClimberIO {

  private final SparkMax motor;

  public ClimberIOSpark() {
    motor = new SparkMax(climberId, MotorType.kBrushless);

    var config = new SparkMaxConfig();
    config.idleMode(IdleMode.kBrake).smartCurrentLimit(climberCurrentLimitAmps);
    motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    inputs.positionRad = motor.getEncoder().getPosition() * 2.0 * Math.PI / gearReduction;
    inputs.velocityRadPerSec =
        motor.getEncoder().getVelocity() * 2.0 * Math.PI / 60.0 / gearReduction;
    inputs.appliedVolts =
        motor.getAppliedOutput() * motor.getAppliedOutput() * motor.getBusVoltage();
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
