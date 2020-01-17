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
    private static final double speed = 0.5;

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor strafeMotor;
    private DcMotor scissorMotor;

    private Servo leftGrab;
    private Servo rightGrab;


    @Override
    public void runOpMode() {

        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        strafeMotor = hardwareMap.get(DcMotor.class, "strafeMotor");
        scissorMotor = hardwareMap.get(DcMotor.class, "scissorMotor");

        leftGrab = hardwareMap.get(Servo.class, "leftGrab");
        rightGrab = hardwareMap.get(Servo.class, "rightGrab");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // zero power behavior makes motor brake when power is set to zero

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        strafeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        scissorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        enableEncoders();

        telemetry.addData("Status", "Initialized");
        runtime.reset();
        boolean runOnce = true;
        waitForStart();


        while (opModeIsActive()){// && runOnce) {
            //move(0, 500, 0);
            telemetry.addData("strafeMotor", strafeMotor.getCurrentPosition());
            telemetry.update();
            strafeMotor.setTargetPosition(1440);
            strafeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            strafeMotor.setPower(speed);
            //Make sure this code does not repeat
            runOnce = false;
        }
    }

    //TODO fix this and test it
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
