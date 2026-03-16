package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {
    
    @AutoLog
    class ShooterIOInputs {
        public double velocityRPM = 0.0;
        public double appliedVolts = 0.0;
        public double currentAmps = 0.0;
        public boolean motorConnected = true;
    }
    
    default void updateInputs(ShooterIOInputs inputs) {}
    default void setSpeed(double speed) {}
    default void stop() {}
}