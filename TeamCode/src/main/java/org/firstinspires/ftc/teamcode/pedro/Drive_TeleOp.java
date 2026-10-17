package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



@TeleOp(name = "TeleOp")
public class Drive_TeleOp extends OpMode {
    private Follower follower;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(OpModeStorage.autonomousEndPose); // pick up where auto ended
        follower.update();
    }

    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,   // forward
                -gamepad1.left_stick_x,   // strafe (left is positive in Pedro)
                -gamepad1.right_stick_x,  // turn (left is positive in Pedro)
                follower.pose().heading()
        );
        follower.manual(powers);

        // relocalise button
        if (gamepad1.startWasPressed()) {
            follower.setPose(new Pose(10.5, 10.5, Math.toRadians(90)));
        }

        follower.update();
        Pose robotPose = follower.pose();
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}
