  public static Command rotateByDegrees(Drive drive, double degrees) {
    return Commands.defer(
        () -> {
          double targetDegrees = Math.IEEEremainder(drive.getHeadingDegrees() + degrees, 360.0);
          return Commands.runEnd(
                  () -> drive.runVelocity(new ChassisSpeeds(0.0, 0.0, Math.signum(degrees) * 2.0)),
                  () -> drive.runVelocity(new ChassisSpeeds(0.0, 0.0, 0.0)),
                  drive)
              .until(() -> Math.abs(drive.getHeadingDegrees() - targetDegrees) < 2.0);
        },
        Set.of(drive));
  }
