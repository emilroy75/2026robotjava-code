# 2026 Robot Development Log
**Team 250 — Emil Roy**

---

## 1. Shooter Subsystem Architecture
- **Structure:** AdvantageKit Tri-layer (Subsystem, IO Interface, Spark/Sim implementations).
- **Mechanical:** Corrected to account for **belt-driven axles**. Added `SHOOT_BELT_RATIO` to trajectory math.
- **Control:** Implemented **`SparkClosedLoopController`** using REVLib 2026 `setSetpoint` for exact RPM targets.
- **Leader-Follower:** Configured `shootMotor2` to follow `shootMotor1` at the hardware level for synchronized axis rotation.

## 2. Intake Subsystem (New)
- **Architecture:** Follows the Tri-layer pattern (`Intake`, `IntakeIO`, `IntakeIOSpark`, `IntakeIOSim`).
- **Dual-Motor Design:**
  - **Pivot Motor:** Handles deployment/retrieval. Set to `kBrake` for stability.
  - **Roller Motor:** Handles ball collection. Set to `kCoast` for efficiency.
- **Shortcuts:** Added `deploy()`, `retract()`, and `runRollers()` methods for cleaner command logic.

## 3. REVLib 2026 Migration
- **Feedforward:** Switched from deprecated `velocityFF` to the new `closedLoop.feedForward.kV()` structure.
- **Shortened Enums:** Standardized on `ResetMode.kResetSafe` and `PersistMode.kPersist`.

## 4. Team-Side Logic (HubPose)
- **Global Access:** `public static Pose2d HubPose` in `Robot.java` is now used by all subsystems for alliance-aware targeting.

## 5. AdvantageKit & Tuning
- **Logging:** Updated `Shooter.java` to log both `TargetRPM` and `ActualRPM` for real-time PID tuning in **AdvantageScope**.
