package org.firstinspires.ftc.teamcode.constants;
import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class IntakeConstants {
    public static final String intakeMotorID = "intakeMotor";

    public static double P = 0.00;
    public static double I = 0.00;
    public static double D = 0.00;
    public static double F = 0.00;

    public static double spinIn = 1;
    public static double spinOut = -1;
    public static double spinSlow = 0.30;
    public static double spinIdle = 0;

    public static double timerLen = 0.25;
}
