package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Servo Test", group="Iterative Opmode")
public class ServoTest extends LinearOpMode
{
    private Servo leftGrab;
    private Servo rightGrab;

    @Override
    public void runOpMode() {
        //initalize


        leftGrab = hardwareMap.get(Servo.class, "leftGrab");
        rightGrab = hardwareMap.get(Servo.class, "rightGrab");

        // let the driver know when we are initialized
        telemetry.addData("Status", "Initialized");

        leftGrab.setPosition(0.5135);
        rightGrab.setPosition(0.48); //0.485

        /*int x = 0;
        int y = 0;*/

        waitForStart();

        /* mainloop */
        while (opModeIsActive()) {

            if (gamepad1.left_bumper) {             //close grabber
                /*x += 0.01;
                y += 0.01;*/
                leftGrab.setPosition(0.48); //0.48
                rightGrab.setPosition(0.5237); //0.52
            } else if (gamepad1.right_bumper) {     //open grabber
                leftGrab.setPosition(0.5135);
                rightGrab.setPosition(0.484);
            }

            /*leftGrab.setPosition(x);
            rightGrab.setPosition(y);

            telemetry.addData("LeftGrab Position",leftGrab.getPosition());
            telemetry.addData("RightGrab Position",rightGrab.getPosition());
            telemetry.update();*/
        }
    }
}
