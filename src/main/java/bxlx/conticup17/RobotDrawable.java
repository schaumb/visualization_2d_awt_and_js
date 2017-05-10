package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.Size;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.shapes.Arc;
import bxlx.graphics.shapes.Polygon;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.Timer;

import java.util.Arrays;

/**
 * Created by qqcs on 5/8/17.
 */
public class RobotDrawable extends ChangeableDrawable {
    private final RobotStates.RobotPlayer player;

    private final ChangeableTimer timer = new ChangeableTimer(this, new Timer(5000));

    public RobotDrawable(RobotStates.RobotPlayer player) {
        this.player = player;
    }

    @Override
    public IDrawable.Redraw needRedraw() {
        return super.needRedraw().setIf(super.needRedraw().iNeedRedraw(), Redraw.PARENT_NEED_REDRAW);
    }

    @Override
    protected void forceRedraw(ICanvas canvas) {
        double time = timer.get();

        /*
            double len = 2 - 1 / (normAngle < Math.PI / 4 ? Math.cos(normAngle) : Math.sin(normAngle));
        */

        // input parameters:
        double robotAngle = Math.atan(1 - 9 * Math.floor(time * 4.99999) / 43); // 0 -> Math.PI * 2
        double headAngleFromRobotAngle = - robotAngle; // (time * 2 % 1 - 0.5) * Math.PI; // -Math.PI / 2 -> Math.PI / 2
        double robotLength = 1; // time; // 0 -> 1
        double armClosed = 0; // 0.0 closed -> 1.0 open
        PacketDrawable element = null ; // new PacketDrawable();


        // pre - constants
        double robotNormAngle = Math.asin(Math.abs(Math.sin(robotAngle * 2))) / 2;
        double robotMaxLength = 1 / (robotNormAngle < Math.PI / 4 ? Math.cos(robotNormAngle) : Math.sin(robotNormAngle));

        Point dir = Direction.fromRadian(robotAngle).getVector();
        Point othDir = new Point(-dir.getY(), dir.getX());
        Point center = canvas.getBoundingRectangle().getCenter();
        double maxSize = canvas.getBoundingRectangle().getSize().getShorterDimension();

        double baseMultiplier = 0.5;
        double baseArea = maxSize * baseMultiplier;
        double elementSize = maxSize * (1 - baseMultiplier) * 0.5 * 0.9 * 0.2 * 0.8 * 0.9;
        Color marginColor = Color.BLACK;
        Color robotColor = Color.YELLOW;

        double headAngle = robotAngle + headAngleFromRobotAngle;
        Point headDir = Direction.fromRadian(headAngle).getVector();
        Point headOthDir = new Point(-headDir.getY(), headDir.getX());

        // start circle
        double startCircleRadius = baseArea * 0.1;
        double startCircleMargin = 0.92;
        Color startCircleColor = robotColor.getScale(Color.BLACK, 0.4);

        // base area
        double baseAreaRadius = startCircleRadius * 0.8;
        double baseAreaSide = baseAreaRadius * Math.sqrt(2);
        Color baseAreaColor = Color.BLACK;

        // first stalk
        Color firstStalkColor = robotColor.getScale(Color.BLACK, 0.3);

        double firstStalkStartWidth = baseAreaSide / 5; // 0 .. baseAreaSide / 2
        double firstStalkStartOthDirection = baseAreaSide / 2 - firstStalkStartWidth; // 0 .. baseAreaSide / 2 - firstStalkStartWidth
        double firstStalkStartDirection = 0; // -baseAreaSide / 2 .. baseAreaSide / 2
        double firstStalkStartMargin = 0.1;

        double firstStalkEndWidth = firstStalkStartWidth * 1.1;
        double firstStalkEndOthDirection = firstStalkStartOthDirection * 1.2;
        double firstStalkEndDirection = 0.5 * baseArea * - ((0.5 / 2 / robotMaxLength - 0.5) * robotLength + 0.5);
        double firstStalkEndMargin = firstStalkStartMargin * 1.0;

        double firstStalkHeight = firstStalkEndDirection - firstStalkStartDirection;

        double firstStalkConnectorMultiplier = 0.5;
        double firstStalkConnectorDirectionCenter = firstStalkHeight * firstStalkConnectorMultiplier;
        double firstStalkConnectorHeight = firstStalkStartWidth + (firstStalkEndWidth - firstStalkStartWidth) * firstStalkConnectorMultiplier;
        double firstStalkConnectorMargin = firstStalkStartMargin + (firstStalkEndMargin - firstStalkStartMargin) * (firstStalkConnectorMultiplier - firstStalkConnectorHeight / 2 / firstStalkHeight);
        double firstStalkConnectorOthDirectionFrom = firstStalkStartOthDirection + (firstStalkEndOthDirection - firstStalkStartOthDirection) * (firstStalkConnectorMultiplier - firstStalkConnectorHeight / 2 / firstStalkHeight);
        double firstStalkConnectorOthDirectionTo = firstStalkStartOthDirection + (firstStalkEndOthDirection - firstStalkStartOthDirection) * (firstStalkConnectorMultiplier + firstStalkConnectorHeight / 2 / firstStalkHeight);

        // stalkConnector

        double stalkConnectorMultiplierBaseFirstStalkEnd = 0.97;
        double stalkConnectorOthDirection = (firstStalkEndOthDirection + firstStalkEndWidth) * stalkConnectorMultiplierBaseFirstStalkEnd;
        double stalkConnectorHeight = stalkConnectorOthDirection * 0.4;
        double stalkConnectorDirection = firstStalkEndDirection * stalkConnectorMultiplierBaseFirstStalkEnd;

        // second stalk
        Color secondStalkColor = robotColor.getScale(Color.BLACK, 0.2);

        double secondStalkStartWidth = firstStalkEndWidth * 1.0;
        double secondStalkStartOthDirection = firstStalkEndOthDirection - secondStalkStartWidth;
        double secondStalkStartDirection = firstStalkEndDirection * 1.0;
        double secondStalkStartMargin = 0.12;

        double secondStalkEndWidth = secondStalkStartWidth * 1.3;
        double secondStalkEndOthDirection = secondStalkStartOthDirection * 1.4;
        double secondStalkEndMargin = secondStalkStartMargin * 1.0;

            // head connector
            Color headCircleColor = robotColor.getScale(Color.BLACK, 0.1);
            double headCircleMultiplierBaseSecondStalkEnd = 0.97;
            double headCircleOthDirection = (secondStalkEndOthDirection + secondStalkEndWidth) * headCircleMultiplierBaseSecondStalkEnd;
            double headCircleHeight = headCircleOthDirection * 1.0;
            double headCircleMargin = secondStalkEndMargin * 1.0;
            double headOthDirection = headCircleHeight / Math.sqrt(2) * 1.2;
            double headDirection = baseArea * 0.07;
            double headMargin = headCircleMargin * Math.sqrt(2);

        double secondStalkEndDirection = (0.5 * baseArea * ((robotMaxLength - 0.5) * robotLength + 0.5) - headCircleHeight - robotMaxLength * headDirection) / headCircleMultiplierBaseSecondStalkEnd; // - headDirection * robotMaxLength * robotLength * Math.sqrt(2);

        double headConnectorDirection = secondStalkEndDirection * headCircleMultiplierBaseSecondStalkEnd;


        // second stalk connectors

        double secondStalkHeight = secondStalkEndDirection - secondStalkStartDirection;

        double secondStalkFirstConnectorMultiplier = 1 / 3.0;
        double secondStalkFirstConnectorDirectionCenter = secondStalkHeight * secondStalkFirstConnectorMultiplier + secondStalkStartDirection;
        double secondStalkFirstConnectorHeight = secondStalkStartWidth + (secondStalkEndWidth - secondStalkStartWidth) * secondStalkFirstConnectorMultiplier;
        double secondStalkFirstConnectorMargin = secondStalkStartMargin + (secondStalkEndMargin - secondStalkStartMargin) * (secondStalkFirstConnectorMultiplier - secondStalkFirstConnectorHeight / 2 / secondStalkHeight);
        double secondStalkFirstConnectorOthDirectionFrom = secondStalkStartOthDirection + (secondStalkEndOthDirection - secondStalkStartOthDirection) * (secondStalkFirstConnectorMultiplier - secondStalkFirstConnectorHeight / 2 / secondStalkHeight);
        double secondStalkFirstConnectorOthDirectionTo = secondStalkStartOthDirection + (secondStalkEndOthDirection - secondStalkStartOthDirection) * (secondStalkFirstConnectorMultiplier + secondStalkFirstConnectorHeight / 2 / secondStalkHeight);

        double secondStalkSecondConnectorMultiplier = 2 / 3.0;
        double secondStalkSecondConnectorDirectionCenter = secondStalkHeight * secondStalkSecondConnectorMultiplier + secondStalkStartDirection;
        double secondStalkSecondConnectorHeight = secondStalkStartWidth + (secondStalkEndWidth - secondStalkStartWidth) * secondStalkSecondConnectorMultiplier;
        double secondStalkSecondConnectorMargin = secondStalkStartMargin + (secondStalkEndMargin - secondStalkStartMargin) * (secondStalkSecondConnectorMultiplier - secondStalkSecondConnectorHeight / 2 / secondStalkHeight);
        double secondStalkSecondConnectorOthDirectionFrom = secondStalkStartOthDirection + (secondStalkEndOthDirection - secondStalkStartOthDirection) * (secondStalkSecondConnectorMultiplier - secondStalkSecondConnectorHeight / 2 / secondStalkHeight);
        double secondStalkSecondConnectorOthDirectionTo = secondStalkStartOthDirection + (secondStalkEndOthDirection - secondStalkStartOthDirection) * (secondStalkSecondConnectorMultiplier + secondStalkSecondConnectorHeight / 2 / secondStalkHeight);

        // head
        Point headCenter = center.add(dir.multiple(headConnectorDirection + headCircleHeight));

        // arm
        Color armColor = Color.BLACK;
        Color armWristColor = Color.YELLOW;

        double armStartRadius = (headOthDirection - headOthDirection * headMargin) * 0.6 / 2;
        double armStartDirectionMultiplier = 3.0 / 4.0;
        double armStartOthDirection = (headOthDirection) * armStartDirectionMultiplier;
        double armStartDirection = headDirection - (headOthDirection) * (1 - armStartDirectionMultiplier);
        double armFirstSize = elementSize * 0.65;
        double armSecondSize = elementSize * 1;
        double armWristMargin = 0.5;

        Point armStart1 = headCenter.add(headDir.multiple(armStartDirection)).add(headOthDir.multiple(armStartOthDirection));
        Point armStart2 = headCenter.add(headDir.multiple(armStartDirection)).add(headOthDir.multiple(-armStartOthDirection));


        double armDefaultDistance = (armStartOthDirection - armStartRadius) * 2;

        // armDefaultDistance + Math.sin(armFromAngle) * armFirstSize * 2 == elementSize
        // Math.asin((elementSize - armDefaultDistance) / armFirstSize / 2)
        double armFromAngle = Math.asin((elementSize - armDefaultDistance) / armFirstSize / 2);
        double armToAngle = Math.PI / 2;

        double armRealAngle = armFromAngle + (armToAngle - armFromAngle) * armClosed;

        Point arm1Dir = Direction.fromRadian(headAngle - armRealAngle).getVector();
        Point arm1OthDir = new Point(-arm1Dir.getY(), arm1Dir.getX());
        Point arm2Dir = Direction.fromRadian(headAngle + armRealAngle).getVector();
        Point arm2OthDir = new Point(-arm2Dir.getY(), arm2Dir.getX());

        Point armMiddle1 = armStart1.add(arm1Dir.multiple(armFirstSize));
        Point armMiddle2 = armStart2.add(arm2Dir.multiple(armFirstSize));

        Builder.container()
                // background
                .add(Builder.shape(new Rectangle(center.add(-baseArea/2), Size.square(baseArea)))
                        .makeColored(Color.WHITE).get())

                // circles
                .add(Builder.shape(Arc.circle(center, startCircleRadius))
                        .makeColored(marginColor).get())
                .add(Builder.shape(Arc.circle(center, startCircleRadius * startCircleMargin))
                        .makeColored(startCircleColor)
                        .get())

                // base
                .add(Builder.shape(Polygon.nGon(4, center, baseAreaRadius, robotAngle + Math.PI / 4))
                        .makeColored(baseAreaColor).get())

                // first stalk
                // 1
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(firstStalkStartDirection)).add(othDir.multiple(firstStalkStartOthDirection)),
                        center.add(dir.multiple(firstStalkStartDirection)).add(othDir.multiple(firstStalkStartOthDirection + firstStalkStartWidth)),
                        center.add(dir.multiple(firstStalkEndDirection)).add(othDir.multiple(firstStalkEndOthDirection + firstStalkEndWidth)),
                        center.add(dir.multiple(firstStalkEndDirection)).add(othDir.multiple(firstStalkEndOthDirection)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(firstStalkStartDirection + firstStalkStartWidth * firstStalkStartMargin)).add(othDir.multiple(firstStalkStartOthDirection + firstStalkStartWidth * firstStalkStartMargin)),
                        center.add(dir.multiple(firstStalkStartDirection + firstStalkStartWidth * firstStalkStartMargin)).add(othDir.multiple(firstStalkStartOthDirection + firstStalkStartWidth - firstStalkStartWidth * firstStalkStartMargin)),
                        center.add(dir.multiple(firstStalkEndDirection + firstStalkEndWidth * firstStalkEndMargin)).add(othDir.multiple(firstStalkEndOthDirection + firstStalkEndWidth - firstStalkEndWidth * firstStalkEndMargin)),
                        center.add(dir.multiple(firstStalkEndDirection + firstStalkEndWidth * firstStalkEndMargin)).add(othDir.multiple(firstStalkEndOthDirection + firstStalkEndWidth * firstStalkEndMargin)))))
                        .makeColored(firstStalkColor)
                        .get())
                // 2
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(firstStalkStartDirection)).add(othDir.multiple(-1).multiple(firstStalkStartOthDirection)),
                        center.add(dir.multiple(firstStalkStartDirection)).add(othDir.multiple(-1).multiple(firstStalkStartOthDirection + firstStalkStartWidth)),
                        center.add(dir.multiple(firstStalkEndDirection)).add(othDir.multiple(-1).multiple(firstStalkEndOthDirection + firstStalkEndWidth)),
                        center.add(dir.multiple(firstStalkEndDirection)).add(othDir.multiple(-1).multiple(firstStalkEndOthDirection)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(firstStalkStartDirection + firstStalkStartWidth * firstStalkStartMargin)).add(othDir.multiple(-1).multiple(firstStalkStartOthDirection + firstStalkStartWidth * firstStalkStartMargin)),
                        center.add(dir.multiple(firstStalkStartDirection + firstStalkStartWidth * firstStalkStartMargin)).add(othDir.multiple(-1).multiple(firstStalkStartOthDirection + firstStalkStartWidth - firstStalkStartWidth * firstStalkStartMargin)),
                        center.add(dir.multiple(firstStalkEndDirection + firstStalkEndWidth * firstStalkEndMargin)).add(othDir.multiple(-1).multiple(firstStalkEndOthDirection + firstStalkEndWidth - firstStalkEndWidth * firstStalkEndMargin)),
                        center.add(dir.multiple(firstStalkEndDirection + firstStalkEndWidth * firstStalkEndMargin)).add(othDir.multiple(-1).multiple(firstStalkEndOthDirection + firstStalkEndWidth * firstStalkEndMargin)))))
                        .makeColored(firstStalkColor)
                        .get())

                // first stalk connector
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter - firstStalkConnectorHeight / 2)).add(othDir.multiple(firstStalkConnectorOthDirectionFrom)),
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter - firstStalkConnectorHeight / 2)).add(othDir.multiple(-firstStalkConnectorOthDirectionFrom)),
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter + firstStalkConnectorHeight / 2)).add(othDir.multiple(-firstStalkConnectorOthDirectionTo)),
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter + firstStalkConnectorHeight / 2)).add(othDir.multiple(firstStalkConnectorOthDirectionTo)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter - firstStalkConnectorHeight / 2 + firstStalkConnectorHeight * firstStalkConnectorMargin)).add(othDir.multiple(firstStalkConnectorOthDirectionFrom + 2 * firstStalkConnectorMargin * firstStalkConnectorHeight)),
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter - firstStalkConnectorHeight / 2 + firstStalkConnectorHeight * firstStalkConnectorMargin)).add(othDir.multiple(-firstStalkConnectorOthDirectionFrom - 2 * firstStalkConnectorMargin * firstStalkConnectorHeight)),
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter + firstStalkConnectorHeight / 2 - firstStalkConnectorHeight * firstStalkConnectorMargin)).add(othDir.multiple(-firstStalkConnectorOthDirectionTo - 2 * firstStalkConnectorMargin * firstStalkConnectorHeight)),
                        center.add(dir.multiple(firstStalkConnectorDirectionCenter + firstStalkConnectorHeight / 2 - firstStalkConnectorHeight * firstStalkConnectorMargin)).add(othDir.multiple(firstStalkConnectorOthDirectionTo + 2 * firstStalkConnectorMargin * firstStalkConnectorHeight)))))
                        .makeColored(firstStalkColor)
                        .get())

                // stalk connector
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(stalkConnectorDirection)).add(othDir.multiple(stalkConnectorOthDirection)),
                        center.add(dir.multiple(stalkConnectorDirection)).add(othDir.multiple(-stalkConnectorOthDirection)),
                        center.add(dir.multiple(stalkConnectorDirection - stalkConnectorHeight)).add(othDir.multiple(-stalkConnectorOthDirection)),
                        center.add(dir.multiple(stalkConnectorDirection - stalkConnectorHeight)).add(othDir.multiple(stalkConnectorOthDirection)))))
                        .makeColored(marginColor)
                        .get())

                // second stalk
                // 1
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkStartDirection)).add(othDir.multiple(secondStalkStartOthDirection)),
                        center.add(dir.multiple(secondStalkStartDirection)).add(othDir.multiple(secondStalkStartOthDirection + secondStalkStartWidth)),
                        center.add(dir.multiple(secondStalkEndDirection)).add(othDir.multiple(secondStalkEndOthDirection + secondStalkEndWidth)),
                        center.add(dir.multiple(secondStalkEndDirection)).add(othDir.multiple(secondStalkEndOthDirection)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkStartDirection + secondStalkStartWidth * secondStalkStartMargin)).add(othDir.multiple(secondStalkStartOthDirection + secondStalkStartWidth * secondStalkStartMargin)),
                        center.add(dir.multiple(secondStalkStartDirection + secondStalkStartWidth * secondStalkStartMargin)).add(othDir.multiple(secondStalkStartOthDirection + secondStalkStartWidth - secondStalkStartWidth * secondStalkStartMargin)),
                        center.add(dir.multiple(secondStalkEndDirection - secondStalkEndWidth * secondStalkEndMargin)).add(othDir.multiple(secondStalkEndOthDirection + secondStalkEndWidth - secondStalkEndWidth * secondStalkEndMargin)),
                        center.add(dir.multiple(secondStalkEndDirection - secondStalkEndWidth * secondStalkEndMargin)).add(othDir.multiple(secondStalkEndOthDirection + secondStalkEndWidth * secondStalkEndMargin)))))
                        .makeColored(secondStalkColor)
                        .get())
                // 2
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkStartDirection)).add(othDir.multiple(-1).multiple(secondStalkStartOthDirection)),
                        center.add(dir.multiple(secondStalkStartDirection)).add(othDir.multiple(-1).multiple(secondStalkStartOthDirection + secondStalkStartWidth)),
                        center.add(dir.multiple(secondStalkEndDirection)).add(othDir.multiple(-1).multiple(secondStalkEndOthDirection + secondStalkEndWidth)),
                        center.add(dir.multiple(secondStalkEndDirection)).add(othDir.multiple(-1).multiple(secondStalkEndOthDirection)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkStartDirection + secondStalkStartWidth * secondStalkStartMargin)).add(othDir.multiple(-1).multiple(secondStalkStartOthDirection + secondStalkStartWidth * secondStalkStartMargin)),
                        center.add(dir.multiple(secondStalkStartDirection + secondStalkStartWidth * secondStalkStartMargin)).add(othDir.multiple(-1).multiple(secondStalkStartOthDirection + secondStalkStartWidth - secondStalkStartWidth * secondStalkStartMargin)),
                        center.add(dir.multiple(secondStalkEndDirection - secondStalkEndWidth * secondStalkEndMargin)).add(othDir.multiple(-1).multiple(secondStalkEndOthDirection + secondStalkEndWidth - secondStalkEndWidth * secondStalkEndMargin)),
                        center.add(dir.multiple(secondStalkEndDirection - secondStalkEndWidth * secondStalkEndMargin)).add(othDir.multiple(-1).multiple(secondStalkEndOthDirection + secondStalkEndWidth * secondStalkEndMargin)))))
                        .makeColored(secondStalkColor)
                        .get())


                // second stalk first connector
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter - secondStalkFirstConnectorHeight / 2)).add(othDir.multiple(secondStalkFirstConnectorOthDirectionFrom)),
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter - secondStalkFirstConnectorHeight / 2)).add(othDir.multiple(-secondStalkFirstConnectorOthDirectionFrom)),
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter + secondStalkFirstConnectorHeight / 2)).add(othDir.multiple(-secondStalkFirstConnectorOthDirectionTo)),
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter + secondStalkFirstConnectorHeight / 2)).add(othDir.multiple(secondStalkFirstConnectorOthDirectionTo)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter - secondStalkFirstConnectorHeight / 2 + secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)).add(othDir.multiple(secondStalkFirstConnectorOthDirectionFrom + 2 * secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)),
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter - secondStalkFirstConnectorHeight / 2 + secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)).add(othDir.multiple(-secondStalkFirstConnectorOthDirectionFrom - 2 * secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)),
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter + secondStalkFirstConnectorHeight / 2 - secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)).add(othDir.multiple(-secondStalkFirstConnectorOthDirectionTo - 2 * secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)),
                        center.add(dir.multiple(secondStalkFirstConnectorDirectionCenter + secondStalkFirstConnectorHeight / 2 - secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)).add(othDir.multiple(secondStalkFirstConnectorOthDirectionTo + 2 * secondStalkFirstConnectorHeight * secondStalkFirstConnectorMargin)))))
                        .makeColored(secondStalkColor)
                        .get())

                // second stalk second connector
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter - secondStalkSecondConnectorHeight / 2)).add(othDir.multiple(secondStalkSecondConnectorOthDirectionFrom)),
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter - secondStalkSecondConnectorHeight / 2)).add(othDir.multiple(-secondStalkSecondConnectorOthDirectionFrom)),
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter + secondStalkSecondConnectorHeight / 2)).add(othDir.multiple(-secondStalkSecondConnectorOthDirectionTo)),
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter + secondStalkSecondConnectorHeight / 2)).add(othDir.multiple(secondStalkSecondConnectorOthDirectionTo)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter - secondStalkSecondConnectorHeight / 2 + secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)).add(othDir.multiple(secondStalkSecondConnectorOthDirectionFrom + 2 * secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)),
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter - secondStalkSecondConnectorHeight / 2 + secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)).add(othDir.multiple(-secondStalkSecondConnectorOthDirectionFrom - 2 * secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)),
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter + secondStalkSecondConnectorHeight / 2 - secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)).add(othDir.multiple(-secondStalkSecondConnectorOthDirectionTo - 2 * secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)),
                        center.add(dir.multiple(secondStalkSecondConnectorDirectionCenter + secondStalkSecondConnectorHeight / 2 - secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)).add(othDir.multiple(secondStalkSecondConnectorOthDirectionTo + 2 * secondStalkSecondConnectorHeight * secondStalkSecondConnectorMargin)))))
                        .makeColored(secondStalkColor)
                        .get())

                // head connector
                .add(Builder.shape(new Polygon(Arrays.asList(
                        center.add(dir.multiple(headConnectorDirection)).add(othDir.multiple(headCircleOthDirection)),
                        center.add(dir.multiple(headConnectorDirection)).add(othDir.multiple(-headCircleOthDirection)),
                        headCenter.add(othDir.multiple(-headCircleOthDirection)),
                        headCenter.add(othDir.multiple(headCircleOthDirection)))))
                        .makeColored(marginColor)
                        .get())

                // head
                .add(Builder.shape(new Polygon(Arrays.asList(
                        headCenter.add(headDir.multiple(0)).add(headOthDir.multiple(headOthDirection)),
                        headCenter.add(headDir.multiple(0)).add(headOthDir.multiple(-headOthDirection)),
                        headCenter.add(headDir.multiple(headDirection)).add(headOthDir.multiple(-headOthDirection)),
                        headCenter.add(headDir.multiple(headDirection)).add(headOthDir.multiple(headOthDirection)))))
                        .makeColored(marginColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        headCenter.add(headDir.multiple(0 + headOthDirection * headMargin)).add(headOthDir.multiple(headOthDirection - headOthDirection * headMargin)),
                        headCenter.add(headDir.multiple(0 + headOthDirection * headMargin)).add(headOthDir.multiple(-headOthDirection + headOthDirection * headMargin)),
                        headCenter.add(headDir.multiple(headDirection - headOthDirection * headMargin)).add(headOthDir.multiple(-headOthDirection + headOthDirection * headMargin)),
                        headCenter.add(headDir.multiple(headDirection - headOthDirection * headMargin)).add(headOthDir.multiple(headOthDirection - headOthDirection * headMargin)))))
                        .makeColored(headCircleColor)
                        .get())

                // head connector - circle
                .add(Builder.shape(Arc.circle(headCenter, headCircleHeight))
                        .makeColored(marginColor)
                        .get())

                .add(Builder.shape(Arc.circle(headCenter, headCircleHeight - headCircleHeight * headCircleMargin))
                        .makeColored(headCircleColor)
                        .get())

                // arm first part
                .add(Builder.shape(new Polygon(Arrays.asList(
                        armStart1.add(arm1Dir.multiple(0)).add(arm1OthDir.multiple(armStartRadius)),
                        armStart1.add(arm1Dir.multiple(0)).add(arm1OthDir.multiple(-armStartRadius)),
                        armStart1.add(arm1Dir.multiple(armFirstSize)).add(arm1OthDir.multiple(-armStartRadius)),
                        armStart1.add(arm1Dir.multiple(armFirstSize)).add(arm1OthDir.multiple(armStartRadius)))))
                        .makeColored(armColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        armStart2.add(arm2Dir.multiple(0)).add(arm2OthDir.multiple(armStartRadius)),
                        armStart2.add(arm2Dir.multiple(0)).add(arm2OthDir.multiple(-armStartRadius)),
                        armStart2.add(arm2Dir.multiple(armFirstSize)).add(arm2OthDir.multiple(-armStartRadius)),
                        armStart2.add(arm2Dir.multiple(armFirstSize)).add(arm2OthDir.multiple(armStartRadius)))))
                        .makeColored(armColor)
                        .get())

                // arm second part
                .add(Builder.shape(new Polygon(Arrays.asList(
                        armMiddle1.add(headDir.multiple(0)).add(headOthDir.multiple(armStartRadius)),
                        armMiddle1.add(headDir.multiple(0)).add(headOthDir.multiple(-armStartRadius)),
                        armMiddle1.add(headDir.multiple(armSecondSize)).add(headOthDir.multiple(-armStartRadius)))))
                        .makeColored(armColor)
                        .get())
                .add(Builder.shape(new Polygon(Arrays.asList(
                        armMiddle2.add(headDir.multiple(0)).add(headOthDir.multiple(armStartRadius)),
                        armMiddle2.add(headDir.multiple(0)).add(headOthDir.multiple(-armStartRadius)),
                        armMiddle2.add(headDir.multiple(armSecondSize)).add(headOthDir.multiple(armStartRadius)))))
                        .makeColored(armColor)
                        .get())

                // arm start circles
                .add(Builder.shape(Arc.circle(armStart1, armStartRadius))
                        .makeColored(armColor)
                        .get())

                .add(Builder.shape(Arc.circle(armStart1, armStartRadius - armStartRadius * armWristMargin))
                        .makeColored(armWristColor)
                        .get())

                .add(Builder.shape(Arc.circle(armStart2, armStartRadius))
                        .makeColored(armColor)
                        .get())

                .add(Builder.shape(Arc.circle(armStart2, armStartRadius - armStartRadius * armWristMargin))
                        .makeColored(armWristColor)
                        .get())

                // arm middle circles
                .add(Builder.shape(Arc.circle(armMiddle1, armStartRadius))
                        .makeColored(armColor)
                        .get())

                .add(Builder.shape(Arc.circle(armMiddle1, armStartRadius - armStartRadius * armWristMargin))
                        .makeColored(armWristColor)
                        .get())

                .add(Builder.shape(Arc.circle(armMiddle2, armStartRadius))
                        .makeColored(armColor)
                        .get())

                .add(Builder.shape(Arc.circle(armMiddle2, armStartRadius - armStartRadius * armWristMargin))
                        .makeColored(armWristColor)
                        .get())

                .add(element == null ? null : Builder.make(element)
                        .makeShapeClipped(new Polygon(Arrays.asList(
                                armMiddle2.add(headDir.multiple(armSecondSize)).add(headOthDir.multiple(armStartRadius)),
                                armMiddle2.add(headDir.multiple(0)).add(headOthDir.multiple(armStartRadius)),
                                armMiddle1.add(headDir.multiple(0)).add(headOthDir.multiple(-armStartRadius)),
                                armMiddle1.add(headDir.multiple(armSecondSize)).add(headOthDir.multiple(-armStartRadius)))))
                        .get())

                .get().forceDraw(canvas);

        if(time >= 1.0) {
            timer.getTimer().setStart();
        }
    }
}
