package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Adhan on 1/3/2016.
 * Left Motor TeleOp
 * unused
 *
 */
public class forwardTest extends OpMode {

    //DriveTrain
    DcMotor motorLeft;
    DcMotor motorRight;

    public forwardTest() {

    }
    //Programs run on initialization
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the bot we assume the following,
		 *   There are two motors "motorRight" and "motorLeft"
		 *   "motorRight" is on the right side of the bot.
		 */
        //DriveTrain
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");

        /*This was included in case the motor needed to be reversed
         *FORWARD is to make the motor run forward
         *REVERSE is to run the motor in the opposite direction i.e. reverse
         */
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }


    //Programs looped
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // driverLeftStick: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // driverRightStick: right_stick_y ranges from -1 to 1, where -1 is full up
        // and 1 is full down
        float driverLeftStick = gamepad1.left_stick_y;
        float driverRightStick = gamepad1.left_stick_y;

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        driverLeftStick = (float)scaleInput(driverLeftStick);
        driverRightStick = (float)scaleInput(driverRightStick);

        // clip the right/left values so that the values never exceed +/- 1
        driverLeftStick = Range.clip(driverLeftStick, -1, 1);
        driverRightStick = Range.clip(driverRightStick,-1,1);

        //sets the power of the motor
        motorRight.setPower(1);
        motorLeft.setPower(1);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", driverLeftStick));
        telemetry.addData("right tgt pwr",  "right  pwr: " + String.format("%.2f", driverRightStick));

    }
    @Override
    public void stop() {

    }
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}