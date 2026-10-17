package org.firstinspires.ftc.teamcode.pedro;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Map;

@Autonomous(name = "New Auto")
public class NewAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    // ===== 1. REPLACE: your poses (x, y, heading in degrees) =====
    private final Pose startPose = poseFactory.of(24, 24, 0);
    private final Pose pose1     = poseFactory.of(108, 24, 0);
    private final Pose pose2     = poseFactory.of(108, 30, 90);
    private final Pose pose3     = poseFactory.of(108,24,90);

    // ===== 2. REPLACE: your paths =====
    private Path path1() { return line(startPose, pose1).constant(startPose); }
    private Path path2() { return line(pose1, pose2).linear(pose1, pose2); }
    private Path path3() { return line(pose2, pose3).linear(pose2,pose3); }

    // ===== 3. REPLACE: the order things happen =====
    private Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                waitMs(5000)
        );
    }

    // ===== KEEP everything below =====
    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.addData("Path completion", follower.completion());
        telemetry.addData("Distance remaining in path", follower.remainingDistance());

        Map<String, Object> drivetrainLog = follower.debug().drivetrain();
        telemetry.addLine(drivetrainLog.toString());

        telemetry.update();
    }

    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}