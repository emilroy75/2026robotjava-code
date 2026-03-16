  public double getHeadingDegrees() {
    return getPose().getRotation().getDegrees();
  }

  public Rotation2d getHeading() {
    return getPose().getRotation();
  }
