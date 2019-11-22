package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Driver Control", group="Iterative Opmode")
public class DriverControl extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    private CRServo stoneClaw;

    private DcMotor scissorMotor;
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    //private DcMotor backStreetMotor;
    private DcMotor extendOUTMotor;

    private double liftSpeed = 0.85;
    private double leftMotorSpeed = 0;
    private double rightMotorSpeed = 0;
    //private double backStreetMotorSpeed = 0;
    private boolean finerMovement = false;


    @Override
    public void runOpMode() {
        // declaration of servo objects
        stoneClaw = hardwareMap.get(CRServo.class, "stoneClaw");

        // declaration of motor objects
        extendOUTMotor = hardwareMap.get(DcMotor.class, "extendOut");
        scissorMotor = hardwareMap.get(DcMotor.class, "scissorMotor");
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        //backStreetMotor = hardwareMap.get(DcMotor.class, "backMotor");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD );

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

            float strafeX = gamepad1.right_stick_x;
            float move = gamepad1.left_stick_y;
            float turn = gamepad1.right_stick_x;

            leftMotorSpeed = Range.clip(move + turn, -1.0, 1.0);
            rightMotorSpeed = Range.clip(move - turn, -1.0, 1.0);
            //backStreetMotorSpeed = Range.clip(strafeX, -1.0, 1.0);

            leftMotor.setPower(leftMotorSpeed);
            rightMotor.setPower(rightMotorSpeed);
            //backStreetMotor.setPower(backStreetMotorSpeed);

            // controls
            liftSpeed = finerMovement ? 0.3 : 0.6;
            telemetry.addData("finer movement", finerMovement);
            if (gamepad1.start) {
                if (!finerMovement) {
                    finerMovement = true;
                } else {
                    finerMovement = false;
                }
            } else if (gamepad1.y) {  //closes stone claw
                stoneClaw.setPower(-1);
            } else if (gamepad1.x) {
                stoneClaw.setPower(1);
            }  else if (gamepad1.a) {
                extendOUTMotor.setPower(-
                        0.5);
            }else if (gamepad1.b) {
                extendOUTMotor.setPower(0.5);
            } else if (gamepad1.dpad_up) {  //extends scissor lift
                scissorMotor.setPower(-liftSpeed);
            } else if (gamepad1.dpad_down){  //contracts scissor lift
                scissorMotor.setPower(liftSpeed);
            } else { //stops all motors and servos if we are not pushing any buttons
                stoneClaw.setPower(0);
                scissorMotor.setPower(0);
                extendOUTMotor.setPower(0);
            }
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        //backStreetMotor.setPower(0);
    }
}
