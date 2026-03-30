# FINAL_ROBOT_POSE_VISION_REQUIREMENTS.md
**Vision Requirement: Final Robot Pose (Path B)**
**Goal: Simplified plug-and-play custom vision for the 2026 robot**

In this approach, the Raspberry Pi or coprocessor performs the AprilTag solve and publishes a finished **field-relative robot pose** to NetworkTables. The robot reads that pose and converts it into the existing `VisionIO` input format used by this codebase.

This is the recommended integration path if the goal is a practical PhotonVision replacement with minimal robot-side refactoring, while still supporting professional-grade trust weighting.

---

## 1. Purpose

The coprocessor must:

- Detect AprilTags from each camera.
- Estimate a **field-relative robot pose** for each usable frame.
- Publish pose observations and metadata over NT4.
- Publish enough timing and quality information for the robot to decide whether to trust the observation.
- Optionally publish dynamic `stdDev` values so the robot can use Pi-provided trust instead of estimating trust locally.

This system is **robot-pose-centric**, not target-pose-centric.

The primary output is:

- **`robotPose`**: the robot pose on the field at the time the frame was captured.

---

## 2. Data Standard

All published values must follow these units and conventions:

- **Distance:** meters
- **Angles:** radians
- **Time:** seconds
- **Coordinate system:** WPILib field coordinate system used by the robot pose estimator
- **Pose format:** `[x, y, z, roll, pitch, yaw]`

Notes:

- `x`, `y`, and `z` must be field-relative.
- `roll`, `pitch`, and `yaw` must describe the final robot rotation.
- The published pose must represent the **robot pose**, not camera-to-tag pose.
- Do not publish degrees in the NT API.
- Do not publish inches in the NT API.

---

## 3. Pi-Side Processing Requirements

For each frame:

1. Capture the frame and preserve the **capture timestamp**.
2. Detect AprilTags in the image.
3. Solve tag pose using camera calibration.
4. Use the field layout and the camera's fixed transform on the robot to compute a **field-relative robot pose**.
5. Compute frame quality values.
6. Compute optional dynamic trust values (`stdDev`).
7. Publish the latest valid observation to NetworkTables.

The Pi must know:

- Camera intrinsics
- Distortion coefficients
- `robotToCamera` transform for each camera
- 2026 AprilTag field layout

Recommended solve path:

- Use `cv2.solvePnPGeneric(...)` when ambiguity metrics are needed.
- Prefer multi-tag solutions over single-tag solutions.

---

## 4. Required NetworkTables Layout

Publish one table per camera.

**Base path:** `/CustomVision/[CameraName]/`

Recommended camera names:
- `Front`
- `Left`
- `Right`

### Required Keys Per Camera

| Key | Type | Unit | Description |
| :--- | :--- | :--- | :--- |
| `connected` | `boolean` | N/A | `true` when the camera and pipeline are alive. |
| `tx` | `double` | radians | Horizontal angle to the best target. |
| `ty` | `double` | radians | Vertical angle to the best target. |
| `robotPose` | `double[6]` | meters/radians | Final robot pose as `[x, y, z, roll, pitch, yaw]`. |
| `timestamp` | `double` | seconds | Frame capture time in the timebase used by robot-side fusion. |
| `ambiguity` | `double` | N/A | Pose ambiguity metric for the chosen solution. |
| `tagCount` | `int` | count | Number of tags used in the final pose estimate. |
| `averageTagDistance` | `double` | meters | Average camera-to-tag distance for tags used in the solve. |
| `tagIds` | `int[]` | N/A | Tag IDs used in the final pose estimate. |
| `heartbeat` | `int` | N/A | Incrementing counter proving the process is still updating. |

### Optional But Recommended Keys

| Key | Type | Unit | Description |
| :--- | :--- | :--- | :--- |
| `stdDev` | `double[3]` | meters/radians | Trust score as `[xStdDev, yStdDev, yawStdDev]`. Lower values mean higher trust. |
| `processingLatencyMs` | `double` | milliseconds | Time spent processing the frame. |
| `totalLatencyMs` | `double` | milliseconds | End-to-end latency estimate. |
| `targetArea` | `double` | percent | Best target image area, for debugging only. |

### Atomic Update Requirement

If the NT library and implementation support atomic or timestamped reads for published values, they should be used so the robot reads a consistent sample of all pose data.

---

## 5. Multi-Camera Requirements

Each camera must have its own NT table and its own latest pose observation. The coprocessor must apply the correct unique transform (X, Y, Z, Yaw, Pitch, Roll) for each camera independently before calculating the final `robotPose`.

---

## 6. Quality Requirements

Minimum required quality fields: `ambiguity`, `tagCount`, `averageTagDistance`, `tagIds`.

Recommended behavior:
- Reject poses that place the robot outside field bounds.
- Reject poses with unrealistic Z height or physically impossible orientation.
- If using `solvePnPGeneric(...)`, reject or heavily down-weight frames with ambiguity above `0.15` to `0.30`.

---

## 7. Trust / Standard Deviation Requirements

`stdDev = [xStdDev, yStdDev, yawStdDev]`

Suggested starting model:
`stdDev = baseline * (averageTagDistance^2 / tagCount)`

If `stdDev` is published, the robot-side Java code should use it when adding vision measurements to the pose estimator.

---

## 8. Timing Requirements

The `timestamp` field is critical.
- It must represent **when the frame was captured**.
- It must be in the same timebase expected by the robot-side fusion code (FPGA time).
- Do not use publish time or processing completion time.

---

## 9. Robot-Side Integration Requirements

The Java adapter (`VisionIOCustomNT.java`) should read the `/CustomVision/[CameraName]/` path and populate the `VisionIOInputs` structure.

---

## 10. Example Java Mapping

```java
public void updateInputs(VisionIOInputs inputs) {
  inputs.connected = connectedSub.get();

  inputs.latestTargetObservation =
      new TargetObservation(
          Rotation2d.fromRadians(txSub.get()),
          Rotation2d.fromRadians(tySub.get()));

  double[] poseData = robotPoseSub.get();
  long[] rawIds = tagIdsSub.get();
  int[] ids = new int[rawIds.length];
  for (int i = 0; i < rawIds.length; i++) {
    ids[i] = (int) rawIds[i];
  }

  inputs.tagIds = ids;

  if (poseData.length == 6 && tagCountSub.get() > 0) {
    Pose3d pose =
        new Pose3d(
            poseData[0],
            poseData[1],
            poseData[2],
            new Rotation3d(poseData[3], poseData[4], poseData[5]));

    inputs.poseObservations =
        new PoseObservation[] {
          new PoseObservation(
              timestampSub.get(),
              pose,
              ambiguitySub.get(),
              (int) tagCountSub.get(),
              averageTagDistanceSub.get(),
              PoseObservationType.PHOTONVISION)
        };
  } else {
    inputs.poseObservations = new PoseObservation[0];
  }
}
```
