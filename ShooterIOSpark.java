package frc.root.subsystems.shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.RelativeEnccoder;

public class ShooterIOSpark implements ShooterIO{

    private final CANSparkMax motor1;
    private final CANSparkMax motor2;
    private final CANSparkMax motor3;
    private final RelativeEncoder encoder1;
    private final RelativeEncoder encoder2;
    private final RelativeEncoder encoder3;

    public ShooterIOSpark(int canId1, int canId2, int canId3){
        motor1 = new CANSparkMax(canId1, MotorType.kBrushless);
        motor2 = new CANSparkMax(canId2, MotorType.Kbrushless);
        motor3 = new CANSparkMax(canId3, Motor type.kBrushless);

        encoder1 = motor1.getEncoder();
        encoder2 = motor2.getEncoder();
        encoder3 = motor3.getEncoder();

        for(CANSparkMax motor : new CANSparkMax[]{motor1, motor2, motor3}){
            motor.restoreFactoryDefualts();
            motor.setSmartCurrentLimit(40);
            motor.setIdleMode(IdleMode.kCoast);
            motor.burnFlash();
        }

    @Override
        public void updateInputs(ShooterIOInputs inputs){
            inputs.velocityRPM = encoder1.getVelocity();
            inputs.appliedVolts = motor1.getAppliedOutput() * motor1.getBusVoltage();
            inputs.currentAmps = getOutputCurrent();
            inputs.motorConnected = true;
        }

    @override
        public void setSpeed(double speed){
            motor1.set(speed)
            motor2.set(speed)
            motor3 set(speed)
        }

    @override
        public void stop(){
            motor1.set(0.0)
            motor2.set(0.0)
            motor3.set(0.0)
        }
    }