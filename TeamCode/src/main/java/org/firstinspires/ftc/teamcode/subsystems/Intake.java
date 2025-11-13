package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.constants.IntakeConstants;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake extends SubsystemBase {
    private MotorEx intakeMotor;
    private Motor.Encoder intakeEncoder;
    private RevColorSensorV3 colorSensor;

    private PIDFController intakeController;

    private double power;
    private double ff;
    private double timer;
    private double intakeGearRatio;

    //Timer
    ElapsedTime Timer;

    private Telemetry telemetry;

    public Intake(HardwareMap hmap, Telemetry telemetry) {
        intakeMotor = new MotorEx(hmap, "intakeMotor", Motor.GoBILDA.RPM_435);
        intakeEncoder = intakeMotor.encoder;

        intakeGearRatio = (100 / 60);

        intakeController = new PIDFController(IntakeConstants.P, IntakeConstants.I, IntakeConstants.D, IntakeConstants.F);

        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setRunMode(Motor.RunMode.RawPower);
        intakeMotor.setInverted(false);

        Timer = new ElapsedTime();

        this.telemetry = telemetry;
    }

    public double getEncoderVelocity() {
        return (intakeEncoder.getCorrectedVelocity() / intakeMotor.getCPR()) * (1 / intakeGearRatio);
    }

    public double getMotorVelocity() {
        return intakeMotor.getCorrectedVelocity();
    }

    public void setPower(double Power) {
        intakeMotor.set(Power);
    }

    public void intake() {
        setPower(IntakeConstants.spinIn);
    }

    public void outtake() {
        setPower(IntakeConstants.spinOut);
    }

    public void slowSpin() {
        setPower(IntakeConstants.spinSlow);
    }

    public void stopIntake() {
        setPower(IntakeConstants.spinIdle);
    }

    public void setIntakeTimer(double timer) {
        Timer.reset();

        timer = IntakeConstants.timerLen;

        while(Timer.seconds() <= timer){
            intake();
        }
    }

    @Override
    public void periodic() {
        telemetry.addData(("Intake Motor Velocity"),getEncoderVelocity());
        telemetry.addData(("Intake Motor Velocity"), getMotorVelocity());
        telemetry.addData(("Intake Motor Power"), intakeMotor.motor.getPower());
        telemetry.update();
    }
}
