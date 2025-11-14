package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.controller.wpilibcontroller.SimpleMotorFeedforward;

import org.firstinspires.ftc.teamcode.constants.TurretConstants;

public class Turret extends SubsystemBase {
    private DcMotorEx turretMotor;
    private DigitalChannel leftHomingSwitch;

    public enum SystemState {
        IDLE,
        HOME,
        FINDING_POSITION,
        RELOCALIZING,
        TARGET_POSITION,
        MANUAL
    }

    public enum WantedState {
        IDLE,
        HOME,
        FINDING_POSITION,
        RELOCALIZING,
        TARGET_POSITION,
        MANUAL
    }

    private PIDFController positionController;
    private SimpleMotorFeedforward frictionController;

    private WantedState wantedState = WantedState.IDLE;
    private SystemState systemState = SystemState.IDLE;

    public static Turret instance;
    public static synchronized Turret getInstance(HardwareMap hMap) {
        if(instance == null) {
            instance = new Turret(hMap);
        }
        return instance;
    }

    private double tx;
    private boolean hasTarget;

    // Encoder constants for GoBilda 312 RPM motor with 36:174 gear ratio
    // Motor encoder: 537.7 CPR at motor output
    // Gear ratio: 36:174 = 174/36 = 4.833:1 reduction
    // Effective CPR at turret: 537.7 * 4.833 = 2598.6 counts per turret revolution
    private static final double MOTOR_CPR = 537.7; // Counts per revolution at motor
    private static final double GEAR_RATIO = 174.0 / 36.0; // 4.833:1 reduction
    private static final double TURRET_CPR = MOTOR_CPR * GEAR_RATIO; // ~2598.6 counts per turret revolution
    private static final double COUNTS_PER_DEGREE = TURRET_CPR / 360.0; // ~7.22 counts per degree

    public Turret(HardwareMap hMap) {
        turretMotor = hMap.get(DcMotorEx.class, TurretConstants.turretMotorID);
        turretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize PID controller with values from TurretConstants (tunable via FTC Dashboard)
        positionController = new PIDFController(
            TurretConstants.kp,
            TurretConstants.ki,
            TurretConstants.kd,
            TurretConstants.kf
        );
        frictionController = new SimpleMotorFeedforward(0, 0, 0);
    }

    @Override
    public void periodic() {
        // Update PID coefficients from FTC Dashboard in real-time
        positionController.setPIDF(
            TurretConstants.kp,
            TurretConstants.ki,
            TurretConstants.kd,
            TurretConstants.kf
        );

        systemState = handleTransition();
        applyStates();
    }

    private SystemState handleTransition() {
        double angle = getAngle();

        if (angle > 360 && systemState != SystemState.RELOCALIZING) {
            return SystemState.RELOCALIZING;
        }

        switch(wantedState) {
            case IDLE:
                return SystemState.IDLE;
            case HOME:
                return SystemState.HOME;
            case FINDING_POSITION:
                return SystemState.FINDING_POSITION;
            case RELOCALIZING:
                return SystemState.RELOCALIZING;
            case TARGET_POSITION:
                return SystemState.TARGET_POSITION;
            case MANUAL:
                return SystemState.MANUAL;
        }

        return systemState;
    }

    public void applyStates() {
        switch(systemState) {
            case IDLE:
                turretMotor.setPower(0);
                break;
            case HOME:
            case FINDING_POSITION:
            case RELOCALIZING:
                relocalize();
                break;
            case TARGET_POSITION:
                if (hasTarget) {
                    aimWithVision();
                } else {
                    turretMotor.setPower(0);
                }
                break;
            case MANUAL:
                // Manual control is set externally via setManualPowerControl()
                break;
        }
    }

    /**
     * Uses PID controller to track AprilTag based on Limelight tx value
     * tx is the horizontal offset in degrees from the center of the camera view
     */
    private void aimWithVision() {
        // Use tx directly as the error (how many degrees we need to rotate)
        // Positive tx means target is to the right, so we need to rotate right (positive power)
        // Negative tx means target is to the left, so we need to rotate left (negative power)
        double error = tx; // Error in degrees
        
        // Calculate PID output
        // We want to minimize the error, so we use error as input
        double power = positionController.calculate(0, error); // Calculate power to reduce error to 0
        
        // Clamp power to safe range
        power = Math.max(-1.0, Math.min(1.0, power));
        
        turretMotor.setPower(power);
    }

    private void relocalize() {
        double currentAngle = getAngle();
        double target = 0;

        double power = positionController.calculate(currentAngle, target);
        turretMotor.setPower(power);

        // When close enough, stop and go to idle or tracking
        if (Math.abs(currentAngle - 0) < 2.0) {  // 2 degree tolerance
            turretMotor.setPower(0);
            wantedState = WantedState.TARGET_POSITION; // automatically resume
        }
    }

    public void setManualPowerControl(double power) {
        wantedState = WantedState.MANUAL;
        turretMotor.setPower(power);
    }

    /**
     * Updates turret with Limelight data for AprilTag tracking
     * @param tx Horizontal offset in degrees (positive = right, negative = left)
     * @param hasTarget Whether an AprilTag is currently detected
     */
    public void setLimelightData(double tx, boolean hasTarget) {
        this.tx = tx;
        this.hasTarget = hasTarget;
    }

    public void startTracking() {
        wantedState = WantedState.TARGET_POSITION;
    }

    public void forceReturnToZero() {
        wantedState = WantedState.RELOCALIZING;
    }

    /**
     * Gets the current turret angle in degrees
     * Uses encoder counts and accounts for gear ratio
     */
    private double getAngle() {
        int encoderCounts = turretMotor.getCurrentPosition();
        // Convert encoder counts to degrees
        // encoderCounts / COUNTS_PER_DEGREE gives us degrees
        return encoderCounts / COUNTS_PER_DEGREE;
    }

    /**
     * Gets the current encoder position
     */
    public int getEncoderPosition() {
        return turretMotor.getCurrentPosition();
    }

    /**
     * Gets the current angle in degrees
     */
    public double getCurrentAngle() {
        return getAngle();
    }
}
