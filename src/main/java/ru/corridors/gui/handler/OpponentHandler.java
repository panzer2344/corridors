package ru.corridors.gui.handler;

import ru.corridors.client.ClientInfoContainer;
import ru.corridors.dto.Point;
import ru.corridors.dto.StepInfo;
import ru.corridors.gui.GameFieldUI;
import ru.corridors.gui.model.State;
import ru.corridors.gui.model.UILine;
import ru.corridors.gui.model.UIPoint;

public class OpponentHandler {
    private GameFieldUI gameFieldUI;

    public OpponentHandler(GameFieldUI gameFieldUI) {
        this.gameFieldUI = gameFieldUI;
    }

    public void handle(StepInfo stepInfo) {
        Point from = stepInfo.getLine().getFrom();
        Point to = stepInfo.getLine().getTo();

        handle(from.getY(), from.getX(), to.getY(), to.getX());
    }

    public void handle(int fromIndexVert, int fromIndexHoriz, int toIndexVert, int toIndexHoriz) {
        UIPoint from = gameFieldUI.getPoints().get(fromIndexVert).get(fromIndexHoriz);
        UIPoint to = gameFieldUI.getPoints().get(toIndexVert).get(toIndexHoriz);

        System.out.println("handle for " + ClientInfoContainer.instance.getOpponentInfo());

        if(from.getState().isChangableState()) from.setState(State.ACTIVE_SECOND_PLAYER);
        if(to.getState().isChangableState()) to.setState(State.ACTIVE_SECOND_PLAYER);

        UILine connectionLine = from.getConnection(to);
        if(connectionLine != null) {
            connectionLine.setState(State.ACTIVE_SECOND_PLAYER);
        }

    }
}
