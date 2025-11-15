package org.firstinspires.ftc.teamcode.OpModes.TeleOP;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.TurretConstants;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

import java.util.List;

/**
 * OpMode specifically for PID tuning the turret's AprilTag tracking
 * 
 * Features:
 * - Real-time PID tuning via FTC Dashboard (http://192.168.43.1:8080/dash)
 * - Displays Limelight data and turret status
 * - Automatically tracks AprilTags when detected
 * - Shows PID error and output values
 * 
 * Usage:
 * 1. Connect to Control Hub Wi-Fi
 * 2. Open FTC Dashboard: http://192.168.43.1:8080/dash
 * 3. Run this OpMode
 * 4. Adjust kp, ki, kd, kf values in TurretConstants section
 * 5. Watch telemetry to see turret response
 */
@TeleOp(group = "Tuning", name = "Turret PID Tuning")
public class TurretPIDTuningOpMode extends OpMode {

    private Turret turret;
    private FtcDashboard dashboard;
    private MultipleTelemetry multipleTelemetry;

    @Override
    public void init() {
        // Initialize FTC Dashboard for PID tuning
        dashboard = FtcDashboard.getInstance();
        multipleTelemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        // Initialize turret subsystem
        turret = Turret.getInstance(hardwareMap);

        multipleTelemetry.addData("Status", "Initialized");
        multipleTelemetry.addData("Instructions", "Connect to FTC Dashboard to tune PID values");
        multipleTelemetry.addData("Dashboard URL", "http://192.168.43.1:8080/dash");
        multipleTelemetry.update();
    }

    @Override
    public void start() {
        // Start Limelight polling
        turret.startLimelight();
        
        // Start auto-tracking
        turret.startTracking();

        multipleTelemetry.addData("Status", "Started - Turret tracking enabled");
        multipleTelemetry.update();
    }

    @Override
    public void loop() {
        // Update turret subsystem (reads Limelight and applies PID)
        turret.periodic();


        // Display PID Constants (from FTC Dashboard)
        multipleTelemetry.addLine("=== PID Constants (Tune via Dashboard) ===");
        multipleTelemetry.addData("Kp", TurretConstants.kp);
        multipleTelemetry.addData("Ki", TurretConstants.ki);
        multipleTelemetry.addData("Kd", TurretConstants.kd);
        multipleTelemetry.addData("Kf", TurretConstants.kf);
        multipleTelemetry.addLine("");

        // Display AprilTag Detection
        multipleTelemetry.addLine("=== AprilTag Detection ===");
        multipleTelemetry.addData("AprilTag Detected", turret.hasTarget() ? "YES" : "NO");
        multipleTelemetry.addData("TX (degrees)", String.format("%.2f", turret.getTx()));
        multipleTelemetry.addLine("");

        // Display Turret Status
        multipleTelemetry.addLine("=== Turret Status ===");
        multipleTelemetry.addData("Current Angle", String.format("%.2f°", turret.getCurrentAngle()));
        multipleTelemetry.addData("Encoder Position", turret.getEncoderPosition());
        multipleTelemetry.addData("Has Target", turret.hasTarget() ? "YES" : "NO");
        multipleTelemetry.addData("Tracking State", turret.getSystemState().toString());
        multipleTelemetry.addLine("");

        // Display PID Control Info
        multipleTelemetry.addLine("=== PID Control ===");
        multipleTelemetry.addData("Error (TX)", String.format("%.2f°", turret.getTx()));
        multipleTelemetry.addData("Target Angle", "0° (centered)");
        multipleTelemetry.addLine("");

        // Display Gamepad Controls
        multipleTelemetry.addLine("=== Controls ===");
        multipleTelemetry.addData("A Button", "Start Tracking");
        multipleTelemetry.addData("B Button", "Stop Tracking");
        multipleTelemetry.addData("X Button", "Return to Zero");
        multipleTelemetry.addData("Y Button", "Manual Control (Left Stick X)");

        // Gamepad controls
        if (gamepad1.a) {
            turret.startTracking();
        }
        if (gamepad1.b) {
            turret.stopTracking();
        }
        if (gamepad1.x) {
            turret.forceReturnToZero();
        }
        if (gamepad1.y) {
            turret.setManualPowerControl(gamepad1.left_stick_x * 0.5);
        }

        multipleTelemetry.update();
    }

    @Override
    public void stop() {
        // Stop Limelight polling
        turret.stopLimelight();
        
        multipleTelemetry.addData("Status", "Stopped");
        multipleTelemetry.update();
    }
}

