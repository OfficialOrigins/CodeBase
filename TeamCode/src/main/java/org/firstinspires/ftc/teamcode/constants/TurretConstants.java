package org.firstinspires.ftc.teamcode.constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public class TurretConstants {
    public static final String turretMotorID = "turretMotor";

    public static int startingPos = 0;
    public static int maxPos = 0;
    public static int minLimit = 0;
    public static int maxLimit = 0;

    //PIDF Constants - Tunable via FTC Dashboard
    public static double kp = 0.0085;
    public static double ki = 0;
    public static double kd = 0;
    public static double kf = 0;
}
