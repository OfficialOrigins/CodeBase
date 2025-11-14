//package org.firstinspires.ftc.teamcode.subsystems;
//
//import com.qualcomm.hardware.limelightvision.LLResultTypes;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.seattlesolvers.solverslib.command.SubsystemBase;
//import com.seattlesolvers.solverslib.hardware.ServoEx;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.constants.ShooterConstants;
//
//import java.util.List;
//
//public class Shooter extends SubsystemBase {
//    private ServoEx hoodServo;
//
//    // Replaced MotorEx → DcMotorEx
//    private DcMotorEx shooterMotor;
//
//    private Limelight3A Cam;
//    private Telemetry telemetry;
//
//    private double currentVelocity;
//    private double targetVelocity;
//    public boolean flyWheelOn;
//
//    private List<LLResultTypes.FiducialResult> tagList;
//
//    public Shooter(HardwareMap hmap, Telemetry telemetry) {
//        this.telemetry = telemetry;
//
//        hoodServo = new ServoEx(hmap, ShooterConstants.hoodServoID);
//
//        shooterMotor = hmap.get(DcMotorEx.class, ShooterConstants.shooterMotorID);
//
//        Cam = hmap.get(Limelight3A.class, "limelight");
//
//        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//
//        // Apply PIDF coefficients
//        shooterMotor.setVelocityPIDFCoefficients(
//                ShooterConstants.kp,
//                ShooterConstants.ki,
//                ShooterConstants.kd,
//                ShooterConstants.kf
//        );
//
//        tagList = Cam.getLatestResult().getFiducialResults();
//    }
//
//    public double getDistance()
//
//    @Override
//    public void periodic() {
//
//        if (flyWheelOn) {
//            shooterMotor.setVelocity(targetVelocity);
//        } else {
//            shooterMotor.setVelocity(0);
//        }
//
//        currentVelocity = getVelocity();
//
//        telemetry.addData("Motor Velocity Raw:", getMotorVelocity());
//        telemetry.addData("Encoder Velocity:", getVelocity());
//        telemetry.addData("Target Velocity:", targetVelocity);
//        telemetry.addData("Current Velocity:", currentVelocity);
//    }
//
//    public double getVelocity() {
//        return shooterMotor.getVelocity(); // Encoder-based velocity
//    }
//
//    public double getMotorVelocity() {
//        return shooterMotor.getVelocity(); // Same but direct
//    }
//
//    public void setMax() {
//        shooterMotor.setPower(1);
//    }
//
//    public void stop() {
//        shooterMotor.setPower(0);
//    }
//
//    public void shootClose() {
//        targetVelocity = ShooterConstants.shootClose;
//    }
//
//    public void shootMid() {
//        targetVelocity = ShooterConstants.shootMid;
//    }
//
//    public void shootFar() {
//        targetVelocity = ShooterConstants.ShootFar;
//    }
//
//    public void spinWheelBack() {
//        shooterMotor.setPower(0.2);
//    }
//
//    //Servo Positions
//    public void setHoodInit() {
//
//    }
//}
