package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.shooter.ShooterIO.ShooterIOInputs;

public class Shooter extends SubsystemBase {

    private final ShooterIO io;
    private final ShooterIOInputsAutoLogged inputs = 
        new ShooterIOInputsAutoLogged();

    public Shooter(ShooterIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Shooter", inputs);
    }

    public void setSpeed(double speed) {
        io.setSpeed(speed);
    }

    public void stop() {
        io.stop();
    }

    public double getVelocityRPM() {
        return inputs.velocityRPM;
    }

    public boolean atTargetRPM(double targetRPM) {
        return Math.abs(inputs.velocityRPM - targetRPM) < 100.0;
    }
}