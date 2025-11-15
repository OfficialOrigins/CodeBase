package org.firstinspires.ftc.teamcode.OpModes.TeleOP;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

/**
 * OpMode to test Turret PID controller with Limelight AprilTag tracking
 * 
 * Usage:
 * 1. Run this OpMode on your robot
 * 2. Connect to FTC Dashboard at http://192.168.43.1:8080/dash
 * 3. Tune PID values (kp, ki, kd, kf) in real-time via the dashboard
 * 4. The turret will automatically track AprilTags detected by Limelight
 */
@TeleOp(group = "Tuning", name = "Turret PID Test")
public class TurretTestOpMode extends OpMode {
    
    private Turret turret;
    private Telemetry dashboardTelemetry;

    @Override
    public void init() {
        // Initialize FTC Dashboard
        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        
        // Initialize Turret subsystem (includes Limelight initialization)
        turret = Turret.getInstance(hardwareMap);
        
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Instructions", "Connect to FTC Dashboard to tune PID values");
        telemetry.update();
    }

    @Override
    public void start() {
        // Start Limelight polling
        turret.startLimelight();
        
        // Start auto-tracking AprilTags
        turret.startTracking();
        
        telemetry.addData("Status", "Started");
        telemetry.addData("Turret", "Tracking AprilTags");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Update turret (reads Limelight and applies PID control)
        turret.periodic();
        
        // Display telemetry
        telemetry.addData("Turret Has Target", turret.hasTarget());
        telemetry.addData("Turret TX", String.format("%.2f", turret.getTx()));
        telemetry.addData("Turret Angle", String.format("%.2f", turret.getCurrentAngle()));
        telemetry.addData("Turret Encoder", turret.getEncoderPosition());
        telemetry.addData("---", "---");
        telemetry.addData("FTC Dashboard", "http://192.168.43.1:8080/dash");
        telemetry.addData("Tune PID", "Adjust kp, ki, kd, kf in TurretConstants");
        telemetry.update();
        
        // Also send to dashboard
        dashboardTelemetry.addData("Turret Has Target", turret.hasTarget());
        dashboardTelemetry.addData("Turret TX", String.format("%.2f", turret.getTx()));
        dashboardTelemetry.addData("Turret Angle", String.format("%.2f", turret.getCurrentAngle()));
        dashboardTelemetry.update();
    }

    @Override
    public void stop() {
        // Stop Limelight polling
        turret.stopLimelight();
        
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}

