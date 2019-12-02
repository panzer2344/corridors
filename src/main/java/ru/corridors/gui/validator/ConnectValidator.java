package ru.corridors.gui.validator;

import ru.corridors.gui.model.UILine;
import ru.corridors.gui.model.UIPoint;

public class ConnectValidator implements Validator<UIPoint> {
    private UIPoint initiator;

    public ConnectValidator(UIPoint initiator) {
        this.initiator = initiator;
    }

    @Override
    public boolean isValid(UIPoint point) {
        boolean isConnected = false;

        for(UILine connection : initiator.getConnections()) {
            isConnected = point.equals(connection.getVertex().a) ||
                          point.equals(connection.getVertex().b);
        }

        return isConnected;
    }
}
