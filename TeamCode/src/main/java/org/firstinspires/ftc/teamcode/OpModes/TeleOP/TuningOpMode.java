package org.firstinspires.ftc.teamcode.OpModes.TeleOP;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

import com.pedropathing.follower.Follower;


@TeleOp (group = "Tuning", name = "PositionTuningOp")
public class TuningOpMode extends OpMode {

    private Drivetrain dt;
    private Intake intake;
    private servoTransfer transfer;


    private DcMotor turretMotor;
    private DcMotor shooterMotor;
    private Servo hoodServo;

    public Follower follower;

    GamepadEx Driver1;
    GamepadEx Driver2;

    public void aPeriodic() {
//        dt.periodic();
        transfer.periodic();
        intake.periodic();
    }

    @Override
    public void init() {
//        dt = new Drivetrain(hardwareMap, telemetry);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0,0));
        intake = new Intake(hardwareMap, telemetry);
        transfer = new servoTransfer(hardwareMap, telemetry);

        turretMotor = hardwareMap.get(DcMotor.class, "turretMotor");
        shooterMotor = hardwareMap.get(DcMotor.class, "shooterMotor");
        hoodServo = hardwareMap.get(Servo.class, "hoodServo");

        Driver1 = new GamepadEx(gamepad1);
        Driver2 = new GamepadEx(gamepad2);

        //Servos
        transfer.init();
        hoodServo.setPosition(0);

        //Motors
        turretMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void start() {
        follower.startTeleopDrive(true);
        follower.setMaxPower(0.55);
//        dt.enableTeleop();
//        dt.follower.setMaxPower(0.70);
    }

    @Override
    public void loop() {
        follower.update();
        follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        aPeriodic();
        telemetry.update();

        if(Driver1.getButton(GamepadKeys.Button.A)) {
            intake.intake();
        }

        else if (Driver1.getButton(GamepadKeys.Button.B)) {
            intake.outtake();
        }

        else {
            intake.stopIntake();
        }

        if(Driver1.getButton(GamepadKeys.Button.DPAD_RIGHT)) {
            shooterMotor.setPower(1);
        }

        else if (Driver1.getButton(GamepadKeys.Button.DPAD_LEFT)) {
            shooterMotor.setPower(-0.20);
        }

        else {
            shooterMotor.setPower(0);
        }

        if(Driver1.getButton(GamepadKeys.Button.Y)) {
            turretMotor.setPower(0.50);
        }

        else if (Driver1.getButton(GamepadKeys.Button.X)) {
            turretMotor.setPower(-0.50);
        }

        else {
            turretMotor.setPower(0);
        }

        if(Driver1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            hoodServo.setPosition(0);
        }

        if(Driver1.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            hoodServo.setPosition(0.60);
        }

        if(Driver2.gamepad.a) {
            transfer.closeGate();
        }

        if(Driver2.gamepad.b) {
            transfer.openGate();
        }

        if (Driver2.gamepad.dpad_up) {
            transfer.extendPitch();
        }

        if (Driver2.gamepad.dpad_down) {
            transfer.retractPitch();
        }

//        if(Driver2.getButton(GamepadKeys.Button.RIGHT_STICK_BUTTON))
//        {
//            dt.resetHeading();
//        }
    }
}
