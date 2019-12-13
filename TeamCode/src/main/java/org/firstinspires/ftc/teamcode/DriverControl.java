package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Driver Control", group="Iterative Opmode")
public class DriverControl extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    private final double COUNTS_PER_MOTOR_REV = 1440 ;    // eg: TETRIX Motor Encoder
    private final double DRIVE_GEAR_REDUCTION = 2.0 ;     // This is < 1.0 if geared UP
    private final double WHEEL_DIAMETER_INCHES = 4.0 ;     // For figuring circumference
    private final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);


    private DcMotor scissorMotor;
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private CRServo leftGrab;
    private CRServo rightGrab;
    private DcMotor strafeMotor;

    private double speed = 1.0;
    private double leftMotorSpeed = 0;
    private double rightMotorSpeed = 0;
    private double strafeMotorSpeed = 0;

    private boolean finerMovement = false;


    @Override
    public void runOpMode() {
        // declaration of motor objects
        scissorMotor = hardwareMap.get(DcMotor.class, "scissorMotor");
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        strafeMotor = hardwareMap.get(DcMotor.class, "strafeMotor");

        scissorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        scissorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftGrab = hardwareMap.get(CRServo.class, "leftGrab");
        rightGrab = hardwareMap.get(CRServo.class, "rightGrab");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // setting zero power behavior of motors so that they brake whenever the power is set to 0
        scissorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //backStreetMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // let the driver know when we are initialized
        telemetry.addData("Status", "Initialized");


        waitForStart();
        runtime.reset();
        // mainloop
        while (opModeIsActive()) {
            telemetry.update();

            float strafe = gamepad1.left_stick_x;
            float move = gamepad1.left_stick_y;
            float turn = gamepad1.right_stick_x;

            leftMotorSpeed = Range.clip(move + turn, -1.0, 1.0);
            rightMotorSpeed = Range.clip(move - turn, -1.0, 1.0);
            strafeMotorSpeed = Range.clip(strafe, -1.0, 1.0);


            leftMotor.setPower(leftMotorSpeed);
            rightMotor.setPower(rightMotorSpeed);
            strafeMotor.setPower(strafeMotorSpeed);


            // controls

            telemetry.addData("finer movement", finerMovement);
            telemetry.addData("Scissor Motor", scissorMotor.getCurrentPosition());
            if (gamepad1.start) { //for precise movement
                if (!finerMovement) {
                    finerMovement = true;
                } else {
                    finerMovement = false;
                }
            }
            speed = finerMovement ? 0.3 : 1.0;

            if (gamepad1.dpad_up && scissorMotor.getCurrentPosition() > -11000) {  //extends scissor lift
                scissorMotor.setPower(-speed);
            } else if (gamepad1.dpad_down && scissorMotor.getCurrentPosition() < 1500) {  //contracts scissor lift
                scissorMotor.setPower(speed);
            } else { //stops movement if no input
                scissorMotor.setPower(0);
            }


            if (gamepad1.left_bumper) { //close grabber
                leftGrab.setPower(1);
                rightGrab.setPower(-1);
            } else if (gamepad1.right_bumper) { //open grabber
                leftGrab.setPower(-1);
                rightGrab.setPower(1);
            } else { //stops
                leftGrab.setPower(0);
                rightGrab.setPower(0);
            }
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        strafeMotor.setPower(0);
    }
}
