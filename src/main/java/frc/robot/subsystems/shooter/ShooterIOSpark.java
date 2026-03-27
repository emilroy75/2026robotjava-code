package frc.robot.subsystems.shooter;

import com.revrobotics.spark.ClosedLoopSlot;

import static frc.robot.subsystems.shooter.ShooterConstants.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.ControlType;

public class ShooterIOSpark implements ShooterIO {
  private final SparkMax shootMotor1;
  private final SparkMax shootMotor2;
  private final SparkMax feedMotor;
  private final SparkClosedLoopController shootController;
  public static double TargetVelocity = 0.0;
  // private static double TargetSpeed = .65;

  public ShooterIOSpark(int shootCanId1, int shootCanId2, int feedCanId) {
    shootMotor1 = new SparkMax(shootCanId1, MotorType.kBrushless);
    shootMotor2 = new SparkMax(shootCanId2, MotorType.kBrushless);
    feedMotor = new SparkMax(feedCanId, MotorType.kBrushless);
    shootController = shootMotor1.getClosedLoopController();
    var shootConfig = new SparkMaxConfig();
    // shootConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(shootCurrentLimitAmps);
    shootConfig.closedLoop.p(shootKp).feedForward.kV(shootKv);
    shootConfig.closedLoop.outputRange(0,1);
    shootConfig.encoder.velocityConversionFactor(1.0);

    // var followerConfig = new SparkMaxConfig();
    // followerConfig.idleMode(IdleMode.kCoast).follow(shootMotor1, true);

    // var feedConfig = new SparkMaxConfig();
    // feedConfig.idleMode(IdleMode.kBrake).smartCurrentLimit(feedCurrentLimitAmps).inverted(true);

    shootMotor1.configure(
        shootConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // shootMotor2.configure(
    //     followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // feedMotor.configure(feedConfig, ResetMode.kResetSafeParameters,
    // PersistMode.kPersistParameters);
  }

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    inputs.shootVelocityRPM = shootMotor1.getEncoder().getVelocity();
    inputs.feedVelocityRPM = feedMotor.getEncoder().getVelocity();
    inputs.shootAppliedVolts = shootMotor1.getAppliedOutput() * shootMotor1.getBusVoltage();
    inputs.feedAppliedVolts = feedMotor.getAppliedOutput() * feedMotor.getBusVoltage();
    inputs.shootCurrentAmps = shootMotor1.getOutputCurrent();
    inputs.feedCurrentAmps = feedMotor.getOutputCurrent();
    inputs.shootMotorsConnected = true;
    inputs.feedMotorConnected = true;
    inputs.shootSpeed1 = shootMotor1.get();
    inputs.shootSpeed2 = shootMotor2.get();
  }

  @Override
  public void setShootVelocity() {
    //  shootController.setSetpoint(rpm, SparkBase.ControlType.kVelocity);
    // SmartDashboard.putNumber("speed", 0);
    TargetVelocity = SmartDashboard.getNumber("speed", 0);
    shootController.setSetpoint(TargetVelocity, ControlType.kVelocity, ClosedLoopSlot.kSlot0);
  }

  @Override
  public void runFeeder() {
    if (shootMotor1.getEncoder().getVelocity() > (.8 * TargetVelocity)/2.8) {
        setFeedSpeed(0.75);
    }
    else {
      setFeedSpeed(0.0);
    }
  }

  @Override
  public void setShootSpeed(double speed) {
    shootMotor1.set(speed);
    // TargetSpeed = speed;
    // System.out.println("Shooter Speed Set!");
  }

  @Override
  public void setFeedSpeed(double speed) {
    feedMotor.set(speed);
  }

  @Override
  public void stop() {
    shootMotor1.set(0.0);
    feedMotor.set(0.0);
  }
}
