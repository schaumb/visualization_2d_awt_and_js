package bxlx.conticup17;

import bxlx.graphics.ChangeableDrawable;
import bxlx.graphics.Color;
import bxlx.graphics.Direction;
import bxlx.graphics.ICanvas;
import bxlx.graphics.IDrawable;
import bxlx.graphics.Point;
import bxlx.graphics.combined.Builder;
import bxlx.graphics.container.Container;
import bxlx.graphics.container.SplitContainer;
import bxlx.graphics.shapes.Rectangle;
import bxlx.system.ColorScheme;

import java.util.List;

/**
 * Created by qqcs on 5/8/17.
 */
public class StationDrawable extends Container<IDrawable> {

    private Rectangle[] lastRectangles = new Rectangle[5];

    private final ChangeableDrawable.ChangeableValue<Double> time;

    public StationDrawable(Direction direction, RobotStates states, RobotStateTimer timer, int playerNum, int stationNum) {
        super();

        this.time = new ChangeableValue<>(this, () -> timer.getTimer().percent());

        RobotStates.StationType station = states.getState().getPlayers()[playerNum].getStations().get(stationNum);
        if(station == null)
            return;

        boolean x = direction.getVector().getX() == 0;
        SplitContainer<IDrawable> outerContainer = new SplitContainer<>(!x);
        for(int i = 0; i < 5; ++i) {
            outerContainer.add((IDrawable) null);
        }

        add(Builder.container()
                .add(station.isOutDrew() ? null : Builder.background().makeColored(Color.WHITE).makeMargin(0.1/5)
                        .makeBackgrounded(Color.DARK_GRAY).makeMargin(0.1).get())
                .add(Builder.make(outerContainer)
                        .makeMargin(
                                x ? 0.1 : 0,
                                x ? 0 : 0.1
                        ).get())
                .add(station.isOutDrew() ? null : Builder.text(station.getAbbr(), "END", 0)
                        .makeColored(ColorScheme.getCurrentColorScheme().textColor)
                        .makeAspect(0, 0, 1)
                        .makeMargin(0.3).get())
                .get());

        SplitContainer<IDrawable> innerContainer = new SplitContainer<>(x);

        int outputSize = station.getOutputs().size();
        for(int i = 0; i < outputSize; ++i) {
            int realIndex = ((stationNum + 1) % 8 > 3) == (playerNum == 0) ? outputSize - 1 - i : i;
            RobotStates.StationOutputType outp = station.getOutputs().get(realIndex);

            if(outp == null) {
                innerContainer.add((IDrawable) null);
                continue;
            }

            PacketDrawable packetDrawable = new PacketDrawable(() -> {
                if(states.getState() == null)
                    return null;
                RobotStates.StationType stationX = states.getState().getPlayers()[playerNum].getStations().get(stationNum);
                RobotStates.Unit unit = states.getState().getUnits().get(stationX.getString(
                        states.getState().getPlayers()[playerNum].getName()
                ) + "-" + outp.getAbbr());

                return unit;
            });

            innerContainer.add(Builder.make(new ChangeableDrawable() {
                        @Override
                        protected void forceRedraw(ICanvas canvas) {
                            lastRectangles[realIndex] = canvas.getBoundingRectangle();
                        }
                    })
                    .makeAspect((int) -direction.getVector().getX(), (int) -direction.getVector().getY(), 1.0)
                    .makeBackgrounded(outp.getColor())
                    .makeMargin(
                            x ? 0.1 : 0,
                            x ? 0 : 0.1)
                    .makeBackgrounded(Color.DARK_GRAY)
                    .makeMargin(0.2).get());

            add(Builder.make(packetDrawable)
                    .makeClipped(true, r -> {
                        RobotStates.Unit unit = packetDrawable.getUnit().get();
                        if(unit == null)
                            return r;

                        double nowTime = time.get();

                        Rectangle toPlace = lastRectangles[realIndex];
                        Point toCenter = toPlace.getCenter();
                        Point center = r.getScaled(toPlace.getSize().getShorterDimension() / r.getSize().getShorterDimension()).getCenter();

                        if(station.isOutDrew()) {
                            center = new Point(playerNum == 0 ? r.getEnd().getX() : r.getStart().getX(), toCenter.getY());
                        }

                        Point from = center;
                        Point to = center;

                        RobotStates.StationOutputType sot = station.getOutputs().get(realIndex);
                        if(sot != RobotStates.StationOutputType.FAIL && sot != RobotStates.StationOutputType.PASS) {
                            List<RobotStates.Unit> changes = states.getState().getChangedFrom();
                            for(int u = 0; u < changes.size(); ++u) {
                                if(changes.get(u) == unit) {
                                    if(states.getState().getChangedFromPlayerIndex().get(u) == playerNum) {
                                        to = toCenter;
                                    } else {
                                        to = new Point(center.getX() + center.getX() - toCenter.getX(), toCenter.getY());
                                    }
                                }
                            }

                            String toUnit = states.getState().hasSwitchUnit(unit);
                            if(toUnit != null) {
                                String state = toUnit.substring(toUnit.lastIndexOf("-") + 1);
                                int stateIndex = 0;
                                for(int st = 0; st < outputSize; ++st) {
                                    RobotStates.StationOutputType statOut = station.getOutputs().get(st);

                                    if(statOut != null && statOut.getAbbr().equals(state)) {
                                        stateIndex = st;
                                        break;
                                    }
                                }
                                to = lastRectangles[stateIndex].getCenter();
                            }

                            if (states.getState().getChangedTo().contains(unit)) {
                                from = toCenter;
                            }
                        } else {
                            from = to = toCenter;
                        }

                        return new Rectangle(from.add(to.add(from.multiple(-1)).multiple(nowTime)).add(toPlace.getSize().asPoint().multiple(-0.5)), toPlace.getSize());
                    })
                    .get());
        }

        outerContainer.get(direction.getVector().asSize().getLongerDimension() == 0 ? 0 : outerContainer.size() - 1)
                .setElem(innerContainer);
    }
}
