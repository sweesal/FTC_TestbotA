package org.firstinspires.ftc.teamcode.robotC.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotB.RobotMapBotB;
import org.firstinspires.ftc.teamcode.robotC.RobotMapBotC;

public class ShooterC {

    private final DcMotor shooter = RobotMapBotC.shooter;
    private final DcMotor elevator = RobotMapBotC.elevator;
    private final Servo trigger = RobotMapBotC.trigger;
    private final DigitalChannel lowerSwitch = RobotMapBotC.lowerSwitch;
    private final ElapsedTime triggerTimer;
    private final ElapsedTime slopeTimer;


    private boolean isShoot = false;
    private static boolean isTriggered = false;
    private static double slopeAngle = 0.55; //default value

    public ShooterC() {
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        elevator.resetDeviceConfigurationForOpMode();
        elevator.setDirection(DcMotorSimple.Direction.FORWARD);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        trigger.setDirection(Servo.Direction.FORWARD);
        lowerSwitch.setMode(DigitalChannel.Mode.INPUT);
        triggerTimer = new ElapsedTime();
        slopeTimer = new ElapsedTime();
    }

    public boolean getSwitchLower () {
        return !lowerSwitch.getState();
    }

//    public void setShooter (boolean isBtnPressed) {
//        if (isBtnPressed)
//            isShooting = !isShooting;
//        if (isShooting)
//            shooter.setPower(0.99);//shooting power
//        else
//            shooter.setPower(0);//stop shooting
//    }

    public void setShooter (boolean isBtnPressed) {
        if(isBtnPressed && slopeTimer.seconds() > 0.2) {
            isShoot = !isShoot;
            slopeTimer.reset();
        }
        if(isShoot) shooter.setPower(0.99);
        else shooter.setPower(0);
    }

    // Set trigger.
    public void setTrigger (boolean btnSet) {
        if(btnSet && triggerTimer.seconds() > 0.3) {
            isTriggered = true;
            triggerTimer.reset();
            slopeTimer.reset();
        }
        if(isTriggered) {
            trigger.setPosition(0.5);
            if(triggerTimer.seconds() > 1.0)
            {
                trigger.setPosition(0);
                isTriggered = false;
            }
        }
    }


    // Elevator mechanism can only move to one direction if top/button limit was triggered.
    public void setElevator (double input, boolean isAtTop, boolean isAtButton) {
        if (isAtTop)
            elevator.setPower(Range.clip(input, -0.2, 0));
        else if (isAtButton)
            elevator.setPower(Range.clip(input, 0, 0.2));
        else
            elevator.setPower(Range.clip(input, -0.5, 0.5));
    }

    // When Y btn of gamepad was pressed elevator goes up, and goes down if A btn pressed.
    // The elevator will single-directional-maneuverable when top/button limit was triggered.
    public void setElevator (boolean cmdUp, boolean cmdDown, boolean isAtTop, boolean isAtButton) {
        if (isAtTop)
            elevator.setPower(cmdDown ? -0.25 : 0);
        else if (isAtButton)
            elevator.setPower(cmdUp ? 0.25 : 0);
        else if (!isAtTop | !isAtButton){
            if (cmdUp)
                elevator.setPower(0.3);
            if (cmdDown)
                elevator.setPower(-0.25);
        } else if (!cmdDown && !cmdUp){
            elevator.setPower(0);
        }
    }

    public double getElevator () {
        return elevator.getCurrentPosition();
    }

}
