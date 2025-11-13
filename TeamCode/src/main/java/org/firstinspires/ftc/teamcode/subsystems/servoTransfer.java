package org.firstinspires.ftc.teamcode.subsystems;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.ServoEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.constants.transferConstants;


public class servoTransfer extends SubsystemBase {
    private ServoEx pitchServo;
    private ServoEx yawServo;

    private boolean gateOpen;
    private boolean kickerExtended;

    private Telemetry telemetry;

    public servoTransfer(HardwareMap hmap, Telemetry telemetry) {
        pitchServo = new ServoEx(hmap, transferConstants.pitchServoID);
        yawServo = new ServoEx(hmap, transferConstants.yawServoID);

//        kicker.setInverted(true);
//        yawServo.setInverted(true);

        kickerExtended = false;
        gateOpen = false;

        this.telemetry = telemetry;
    }

    public void init() {
        pitchServo.set(0.00);
        yawServo.set(0.00);
    }

    public void extendPitch() {
        pitchServo.set(0.60);
        kickerExtended = true;
    }

    public void retractPitch() {
        pitchServo.set(0.00);
        kickerExtended = false;
    }

    public void openGate() {
        yawServo.set(0.50);
        gateOpen = true;
    }

    public void closeGate() {
        yawServo.set(0.50);
        gateOpen = false;
    }

    @Override
    public void periodic() {
        telemetry.addData(("Gate open: "), gateOpen);
        telemetry.addData(("Kicker Extended: "), kickerExtended);
        telemetry.update();
    }
}
