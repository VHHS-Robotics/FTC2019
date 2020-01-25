package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

// Roman is my extraterrestrial cat -Luis

@Autonomous(name="AutoBuildZone", group="Iterative Opmode")
public class AutoBuildZone extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();


    private static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: Andymark Motor Encoder was 280
    private static final double COUNTS_PER_MOTOR_REV_60 = 420;    // eg: Andymark Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    private static final double speed = 1.0;

    private double ticks_per_inch = 114.5915;

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor strafeMotor;
    private DcMotor scissorMotor;

    private Servo leftGrab;
    private Servo rightGrab;
    private Servo sideGrab;

    private Boolean isRed = null;
    private Boolean isBuild = null;
    private Boolean movePlate = null;
    private Boolean parkTapeTop = null;

    private int direction = 1;

    @Override
    public void runOpMode() {

        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        strafeMotor = hardwareMap.get(DcMotor.class, "strafeMotor");
        scissorMotor = hardwareMap.get(DcMotor.class, "scissorMotor");

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

        enableEncoders();
        sideGrab.setPosition(0);

        //Ask questions
        while (isRed == null) {

            if (gamepad1.a) {
                isRed = gamepad1.a;
            }
            if (gamepad1.b) {
                isRed = !gamepad1.b;
            }
            telemetry.addData("isRed?", isRed);
            telemetry.update();
        }
        sleep(250);
        while (isBuild == null) {
            if (gamepad1.a) {
                isBuild = gamepad1.a;
            }
            if (gamepad1.b) {
                isBuild = !gamepad1.b;
            }
            telemetry.addData("isBuild?", isBuild);
            telemetry.update();
        }
        sleep(250);
        while (movePlate == null) {
            if (gamepad1.a) {
                movePlate = gamepad1.a;
            }
            if (gamepad1.b) {
                movePlate = !gamepad1.b;
            }
            telemetry.addData("movePlate?", movePlate);
            telemetry.update();
        }
        sleep(250);
        while (parkTapeTop == null) {
            if (gamepad1.a) {
                parkTapeTop = gamepad1.a;
            }
            if (gamepad1.b) {
                parkTapeTop = !gamepad1.b;
            }
            telemetry.addData("parkTapeTop?", parkTapeTop);
            telemetry.update();
        }

        boolean runOnce = true;
        waitForStart();


        while (opModeIsActive() && runOnce) {
            telemetry.addData("leftMotor.isBusy()", leftMotor.isBusy());
            telemetry.addData("rightMotor.isBusy()", rightMotor.isBusy());
//            telemetry.addData("strafeMotor", strafeMotor.getCurrentPosition());
//            telemetry.update();

            // @TODO make scissor lift lower while moving to fit under bridge
            // @TODO ALL MEASUREMENTS ARE ARBITRARY
            if (!isRed) {
                direction = -1; // multiply times distance
            }
            if (isBuild) {
                if (movePlate) {
                    leftRight(-24);
                    forwardBackward(-35);
                    leftRight(-10);
                    // GRAB
                    leftRight(29);
                    // DROP
                    forwardBackward(35);
                }
                leftRight(-15);
                forwardBackward(47);
                leftRight(-6);
                leftRight(15);
                // GRAB
                forwardBackward(-47);
                // DROP
                if(!parkTapeTop) {
                    leftRight(19);
                }
                forwardBackward(12);
            } else {
                forwardBackward(12);
                leftRight(-25);
                // GRAB
                leftRight(5);
                forwardBackward(-47);
                // DROP
                if (movePlate) {
                    forwardBackward(12);
                    leftRight(-5);
                    // GRAB
                    leftRight(29);
                    // DROP
                }
                forwardBackward(59);
                leftRight(-30);
                // GRAB
                leftRight(5);
                forwardBackward(-47);
                // DROP
                if (!parkTapeTop) {
                    leftRight(26);
                }
                forwardBackward(12);
            }

            //Make sure this code does not repeat
            runOnce = false;
        }
    }

    //TODO fix this and test it

    private void leftRight(int distance) {
        strafeMotor.setTargetPosition(strafeMotor.getCurrentPosition() + (int)(ticks_per_inch*distance));
        strafeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        strafeMotor.setPower(speed);
        /*if (distance > 0) {
            strafeMotor.setPower(speed);
        } else {
            strafeMotor.setPower(-speed);
        }*/
        while (strafeMotor.isBusy()) {}
        //strafeMotor.setPower(0);
        sleep(1000);

    }

    private void forwardBackward(int distance) {
        leftMotor.setTargetPosition(leftMotor.getCurrentPosition() + (int)(ticks_per_inch*distance*direction));
        rightMotor.setTargetPosition(leftMotor.getCurrentPosition() + (int)(ticks_per_inch*distance*direction));

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(speed);
        rightMotor.setPower(speed);//0.905);
        /*
        if (distance > 0) {
            leftMotor.setPower(speed*direction);
            rightMotor.setPower(speed*direction);
        } else {
            leftMotor.setPower(-speed*direction);
            rightMotor.setPower(-speed*direction);
        }*/
        while (rightMotor.isBusy()) {}
        sleep(1000);
        /*leftMotor.setPower(0);
        rightMotor.setPower(0);*/

    }

    private void move(float strafeY,float strafeX, float turn){
        int leftMotorNew;
        int rightMotorNew;
        int strafeMotorNew;


        if(strafeY!=0){
            //adding the distance to move in inches to current position
            leftMotorNew = leftMotor.getCurrentPosition() + (int) (strafeY * COUNTS_PER_INCH);
            rightMotorNew = rightMotor.getCurrentPosition() + (int) (strafeY * COUNTS_PER_INCH);
            leftMotor.setTargetPosition(leftMotorNew);
            rightMotor.setTargetPosition(rightMotorNew);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setPower(speed);
            rightMotor.setPower(speed);
        }
        if(strafeX!=0){

            strafeMotorNew = strafeMotor.getCurrentPosition() + (int) (strafeX * COUNTS_PER_INCH);
            strafeMotor.setTargetPosition(strafeMotorNew);
            strafeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeMotor.setPower(speed);
        }
        if(turn!=0){
            leftMotorNew = leftMotor.getCurrentPosition() - (int) Math.round((Math.PI*12.5*turn)/180*COUNTS_PER_INCH) ;
            rightMotorNew = rightMotor.getCurrentPosition() + (int) Math.round((Math.PI*12.5*turn)/180*COUNTS_PER_INCH);
            leftMotor.setTargetPosition(leftMotorNew);
            rightMotor.setTargetPosition(rightMotorNew);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setPower(speed);
            rightMotor.setPower(speed);
        }

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        strafeMotor.setPower(0);
    }

    private void enableEncoders(){
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        strafeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        scissorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        strafeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        scissorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
