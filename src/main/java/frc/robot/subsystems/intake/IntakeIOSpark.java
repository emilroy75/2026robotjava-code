package frc.robot.subsystems.intake;

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
    pivotConfig.idleMode(IdleMode.kBrake).smartCurrentLimit(30);

    var rollerConfig = new SparkMaxConfig();
    rollerConfig.idleMode(IdleMode.kBrake).smartCurrentLimit(30);

    pivotMotor.configure(
        pivotConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rollerMotor.configure(
        rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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
    pivotMotor.set(0);
    pivotMotor.set(0);
  }
}
