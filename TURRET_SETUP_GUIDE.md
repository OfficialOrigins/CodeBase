# Turret PID Controller Setup Guide

## Accessing FTC Dashboard

### Step 1: Connect to Robot Wi-Fi
1. Power on your Control Hub
2. On your computer/laptop, connect to the Control Hub's Wi-Fi network:
   - Network name: Usually something like "TEAM-XXXX-RC" or similar
   - Password: Check your Control Hub configuration

### Step 2: Open FTC Dashboard
1. Open a web browser (Chrome, Firefox, Safari, etc.)
2. Navigate to: **`http://192.168.43.1:8080/dash`**
   - If that doesn't work, try: `http://192.168.49.1:8080/dash` (for Android phone controllers)
3. The FTC Dashboard should load showing telemetry and configuration options

### Step 3: Tune PID Values
1. In the FTC Dashboard, you should see a section for **`TurretConstants`**
2. You'll see sliders/input fields for:
   - **kp** (Proportional gain) - Start with 0.0085
   - **ki** (Integral gain) - Start with 0
   - **kd** (Derivative gain) - Start with 0
   - **kf** (Feedforward) - Start with 0
3. Adjust values in real-time while your robot is running
4. Changes take effect immediately - no need to restart

## Verifying Limelight AprilTag Detection

### Step 1: Check Limelight Pipeline Configuration
1. Connect to your Limelight's web interface:
   - Find your Limelight's IP address (usually shown in Robot Controller app)
   - Open browser and go to: `http://[limelight-ip]` (e.g., `http://10.11.66.2`)
2. Verify Pipeline 20 is configured for **Fiducial (AprilTag) Detection**:
   - Go to Pipeline Settings
   - Select Pipeline 20
   - Ensure it's set to **"Fiducial"** or **"AprilTag"** mode
   - The pipeline should detect the green square box (AprilTag)

### Step 2: Test Detection in Code
The code now explicitly checks for AprilTag (fiducial) results:
- If AprilTags are detected, `hasTarget` will be `true` and `tx` will contain the horizontal offset
- If no AprilTags are found, `hasTarget` will be `false`

### Step 3: Monitor in Telemetry
Add this to your OpMode's loop() to verify detection:
```java
telemetry.addData("Turret Has Target", turret.hasTarget);
telemetry.addData("Turret TX", turret.tx);
telemetry.update();
```

## Code Usage Example

```java
// In init():
turret = Turret.getInstance(hardwareMap);

// In start():
turret.startLimelight();  // Start Limelight polling
turret.startTracking();    // Begin auto-tracking AprilTags

// In loop():
turret.periodic();  // Automatically reads Limelight and tracks AprilTags
```

## Troubleshooting

### FTC Dashboard Not Loading
- Check Wi-Fi connection to Control Hub
- Try different IP addresses: `192.168.43.1` or `192.168.49.1`
- Ensure robot code is running (Dashboard only works when code is active)
- Check firewall settings on your computer

### Limelight Not Detecting AprilTags
- Verify pipeline 20 is set to "Fiducial" mode in Limelight web interface
- Check that AprilTags are in view and properly lit
- Ensure `limelight.start()` is called before reading data
- Check Limelight's LED brightness settings
- Verify AprilTag size matches Limelight's detection range

### PID Controller Not Working
- Ensure FTC Dashboard is connected and values are being updated
- Check that `turret.periodic()` is being called in your loop
- Verify `turret.startTracking()` was called
- Check telemetry to see if `hasTarget` is true when AprilTags are visible

