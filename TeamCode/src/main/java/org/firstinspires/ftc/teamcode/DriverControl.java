package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Driver Control", group="Iterative Opmode")
public class DriverControl extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor strafeMotor;
    private DcMotor scissorMotor;

    private Servo leftGrab;
    private Servo rightGrab;
    private Servo sideGrab;

    private double speed = 1.0;
    private double leftMotorSpeed = 0;
    private double rightMotorSpeed = 0;
    private double strafeMotorSpeed = 0;

    private boolean finerMovement = false;

    @Override
    public void runOpMode() {
        /* initialization */
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        strafeMotor = hardwareMap.get(DcMotor.class, "strafeMotor");
        scissorMotor = hardwareMap.get(DcMotor.class, "scissorMotor");

        scissorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        scissorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftGrab = hardwareMap.get(Servo.class, "leftGrab");
        rightGrab = hardwareMap.get(Servo.class, "rightGrab");
        sideGrab = hardwareMap.get(Servo.class, "sideGrab");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // zero power behavior makes motor brake when power is set to zero

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        strafeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        scissorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status:", "Initialized");

        leftGrab.setPosition(0.518);
        rightGrab.setPosition(0.48); //0.48
        sideGrab.setPosition(0);

        waitForStart();
        runtime.reset();
        /* mainloop */
        while (opModeIsActive()) {
            telemetry.update();
            /* controls */
            float strafe = gamepad1.left_stick_x;
            float move = gamepad1.left_stick_y;
            float turn = gamepad1.right_stick_x;

            if (!finerMovement) {
                leftMotorSpeed = Range.clip(move + turn, -1.0, 1.0);
                rightMotorSpeed = Range.clip(move - turn, -1.0, 1.0);
                strafeMotorSpeed = Range.clip(strafe, -1.0, 1.0);
            } else {
                leftMotorSpeed = speed;
                rightMotorSpeed = speed;
                strafeMotorSpeed = speed;
            }

            leftMotor.setPower(leftMotorSpeed);
            rightMotor.setPower(rightMotorSpeed);
            strafeMotor.setPower(strafeMotorSpeed);


            telemetry.addData("Finer Movement:", finerMovement);
            telemetry.addData("Scissor Motor Pos:", scissorMotor.getCurrentPosition());
            // the following toggles the speed of the robot's components for small movements
            if (gamepad1.start) {
                if (!finerMovement) {
                    finerMovement = true;
                    speed = 0.3;
                } else {
                    finerMovement = false;
                    speed = 1.0;
                }
            }

            // the motor powering the scissor lift is only allowed to move when in a certain range

            if (gamepad1.dpad_up && scissorMotor.getCurrentPosition() > -11000) {           //extends scissor lift
                scissorMotor.setPower(-speed);
            } else if (gamepad1.dpad_down && scissorMotor.getCurrentPosition() < 1500) {    //contracts scissor lift
                scissorMotor.setPower(speed);
            } else {
                scissorMotor.setPower(0);
            }

            if (gamepad1.left_bumper) {             //close grabber
                /*x += 0.01;
                y += 0.01;*/
                leftGrab.setPosition(0.46); //0.48
                rightGrab.setPosition(0.54); //0.5237
            } else if (gamepad1.right_bumper) {     //open grabber
                leftGrab.setPosition(0.5135);
                rightGrab.setPosition(0.484);
            }

            if (gamepad1.x && sideGrab.getPosition() < 0.8) {
                sideGrab.setPosition(sideGrab.getPosition()+0.01);
            } else if (gamepad1.y && sideGrab.getPosition() > 0) {
                sideGrab.setPosition(sideGrab.getPosition()-0.01);
            }
            telemetry.addData("pos side", sideGrab.getPosition());
            telemetry.update();
            /*
            if (gamepad1.left_bumper) {             //close grabber
                leftGrab.setPower(1);
                rightGrab.setPower(-1);
            } else if (gamepad1.right_bumper) {     //open grabber
                leftGrab.setPower(-1);
                rightGrab.setPower(1);
            } else {
                leftGrab.setPower(0);
                rightGrab.setPower(0);
            }*/
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        strafeMotor.setPower(0);
    }
}
