package ru.corridors.gui.handler;

import ru.corridors.client.ClientInfoContainer;
import ru.corridors.dto.StepInfo;
import ru.corridors.gui.ClientGUI;

public class StepResultHandler {

    private ClientGUI clientGUI;

    public StepResultHandler(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    public void handle(StepInfo stepInfo) {
        int countOfSquares = stepInfo.getCountOfSquares();
        if(countOfSquares != 0) {
            if(ClientInfoContainer.instance.getClientInfo().equals(stepInfo.getFilledSqureBy())) {
                clientGUI.getUiScore().addYourPoints(countOfSquares);
            } else {
                clientGUI.getUiScore().addOpponentPoints(countOfSquares);
            }
        }
    }

}
