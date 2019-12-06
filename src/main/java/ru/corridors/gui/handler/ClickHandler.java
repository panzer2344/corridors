package ru.corridors.gui.handler;

import ru.corridors.dto.Line;
import ru.corridors.dto.Point;
import ru.corridors.dto.StepInfo;
import ru.corridors.gui.model.State;
import ru.corridors.gui.model.UILine;
import ru.corridors.gui.model.UIPoint;
import ru.corridors.gui.validator.AllowStepValidator;
import ru.corridors.gui.validator.ConnectValidator;
import ru.corridors.gui.validator.FillLineValidator;
import ru.corridors.gui.validator.Validator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClickHandler implements MouseListener {

    public static final ClickHandler instance = new ClickHandler();

    private Validator<Void> allowStepValidator = AllowStepValidator.instance;
    private Validator<StepInfo> fillLineValidator = FillLineValidator.instance;
    private RegisterStepAction registerStepAction = RegisterStepAction.instance;

    private UIPoint first;
    private UIPoint second;

    private UIPoint lastMissedPoint;
    private State missedPointPrevState;
    private State firstPointPrevState;

    private ClickHandler() {}

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        if( ! allowStepValidator.isValid(null) ) return;

        if(lastMissedPoint != null) {
            lastMissedPoint.setState(missedPointPrevState);
            lastMissedPoint = null;
        }

        if(mouseEvent.getSource() instanceof UIPoint) {
            UIPoint point = (UIPoint) mouseEvent.getSource();

            if(first == null) {
                first = point;
                firstPointPrevState = first.getState();
                first.setState(State.CHOOSING_FIRST_PLAYER);
            } else {
                second = point;
                StepInfo stepInfo = buildStepInfo(first, second);

                if(new ConnectValidator(first).isValid(point) && fillLineValidator.isValid(stepInfo)) {
                    if(first.getState().isChangableState()) first.setState(State.ACTIVE_FIRST_PLAYER);

                    if(second.getState().isChangableState()) second.setState(State.ACTIVE_FIRST_PLAYER);

                    UILine connectionLine = first.getConnection(second);
                    if(connectionLine != null) {
                        connectionLine.setState(State.ACTIVE_FIRST_PLAYER);
                    }

                    registerStepAction.doAction(stepInfo);

                    clearLinks();
                } else {
                    lastMissedPoint = point;
                    missedPointPrevState = lastMissedPoint.getState();
                    lastMissedPoint.setState(State.MISSED_POINT);
                    //first.setState(State.NOT_ACTIVE_POINT);
                    first.setState(firstPointPrevState);

                    clearLinks();
                }
//                if(new ConnectValidator(first).isValid(point)) {
//                    second = point;
//
//                    StepInfo stepInfo = buildStepInfo(first, second);
//
//                    if(fillLineValidator.isValid(stepInfo)) {
//                        first.setState(State.ACTIVE_FIRST_PLAYER);
//                        second.setState(State.ACTIVE_FIRST_PLAYER);
//
//                        UILine connectionLine = first.getConnection(second);
//                        if(connectionLine != null) {
//                            connectionLine.setState(State.ACTIVE_FIRST_PLAYER);
//                        }
//
//                        registerStepAction.doAction(stepInfo);
//
//                        clearLinks();
//                    } else {
//                        second = null;
//                    }
//
//                } else {
//                    lastMissedPoint = point;
//                    missedPointPrevState = lastMissedPoint.getState();
//                    lastMissedPoint.setState(State.MISSED_POINT);
//                    first.setState(State.NOT_ACTIVE_POINT);
//
//                    clearLinks();
//                }
            }
        } else {
            clearLinks();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    private void clearLinks() {
        first = null;
        second = null;
    }

    private StepInfo buildStepInfo(UIPoint from, UIPoint to){
        return new StepInfo(new Line(
                new Point(from.getIndexHor(), from.getIndexVert()),
                new Point(to.getIndexHor(), to.getIndexVert())));
    }
}
