package org.firstinspires.ftc.teamcode.robotC.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotA.RobotMapBotA;
import org.firstinspires.ftc.teamcode.robotC.RobotMapBotC;

public class IntakeC {

    private final DcMotor intake = RobotMapBotC.intake;
    private static boolean isIntakeTriggered = false;

    private static final double intakePower = 0.9;

    public IntakeC() {
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setIntakeFree (boolean isBtnPressed, double setPower) {
        if (isBtnPressed) {
            intake.setPower(Range.clip(setPower, -0.99, 0.99));
        } else {
            intake.setPower(0);
        }
    }

    public void setIntake (boolean isBtnPressed, boolean isRevBtnPressed) {
//        if (isBtnPressed)
//            isIntakeTriggered = !isIntakeTriggered;
//        if (isIntakeTriggered)
        if (isBtnPressed)
            intake.setPower(intakePower);//intake power
        else if (isRevBtnPressed)
            intake.setPower(-intakePower);
        else
            intake.setPower(0);//stop intake
    }

}
