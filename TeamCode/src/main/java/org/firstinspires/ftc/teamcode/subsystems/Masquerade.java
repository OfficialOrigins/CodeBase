//package org.firstinspires.ftc.teamcode.subsystems;
//
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
//import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
//
//import com.pedropathing.util.Timer;
//import com.qualcomm.robotcore.hardware.Gamepad;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.seattlesolvers.solverslib.gamepad.GamepadEx;
//import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
//
//import org.firstinspires.ftc.teamcode.pedropathing.Constants;
//import org.firstinspires.ftc.teamcode.subsystems.
//import org.firstinspires.ftc.teamcode.subsystems.Shooter;
//import org.firstinspires.ftc.teamcode.subsystems.Intake;
//import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.follower.FollowerConstants;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//public class Masquerade {
//
//    public enum Alliance {
//        RED,
//        BLUE;
//    }
//
//    public Drivetrain dt;
//    public  Follower follower;
//    public Intake intake;
//    public Shooter shooter;
//    public Turret turret;
//    public GamepadEx driver1;
//    public GamepadEx driver2;
//    private Telemetry telemetry;
//    private int transferState=1;
//    private Timer tTimer;
//
//    public Masquerade(HardwareMap hMap, Telemetry telemetry, Alliance color, Gamepad d1, Gamepad d2)
//    {
////        dt= new Drivetrain(hMap,telemetry);
//        follower = Constants.createFollower(hMap);
//        this.telemetry=telemetry;
//        intake = new Intake(hardwareMap,telemetry);
//        //shooter= new Shooter(hMap,telemetry,color);
//        turret = new Turret(hMap);
//        driver1= new GamepadEx(d1);
//        driver2= new GamepadEx(d2);
//        tTimer=new Timer();
//    }
//
//
//
//}
