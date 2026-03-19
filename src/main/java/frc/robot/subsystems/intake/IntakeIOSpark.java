package frc.robot.subsystems.intake;

import static frc.robot.subsystems.intake.IntakeConstants.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class IntakeIOSpark implements IntakeIO {
  private final SparkMax pivotMotor;
  private final SparkMax rollerMotor;

  public IntakeIOSpark(int pivotId, int rollerId) {
    pivotMotor = new SparkMax(pivotId, MotorType.kBrushless);
    rollerMotor = new SparkMax(rollerId, MotorType.kBrushless);

    var pivotConfig = new SparkMaxConfig();
    pivotConfig
        .idleMode(IdleMode.kBrake)
        .smartCurrentLimit(deployCurrentLimitAmps)
        .inverted(true); // Positive = Deploy (Down) per standard

    var rollerConfig = new SparkMaxConfig();
    rollerConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(rollerCurrentLimitAmps);

    pivotMotor.configure(
        pivotConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rollerMotor.configure(
        rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.pivotPositionRad = pivotMotor.getEncoder().getPosition();
    inputs.pivotVelocityRadPerSec = pivotMotor.getEncoder().getVelocity();
    inputs.rollerVelocityRPM = rollerMotor.getEncoder().getVelocity();
    inputs.pivotAppliedVolts = pivotMotor.getAppliedOutput() * pivotMotor.getBusVoltage();
    inputs.rollerAppliedVolts = rollerMotor.getAppliedOutput() * rollerMotor.getBusVoltage();
  }

  @Override
  public void setPivotSpeed(double speed) {
    pivotMotor.set(speed);
  }

  @Override
  public void setRollerSpeed(double speed) {
    rollerMotor.set(speed);
  }

  @Override
  public void stop() {
    pivotMotor.set(0.0);
    rollerMotor.set(0.0);
  }
}
