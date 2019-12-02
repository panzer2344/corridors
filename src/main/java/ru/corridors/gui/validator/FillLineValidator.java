package ru.corridors.gui.validator;

import ru.corridors.dto.StepInfo;
import ru.corridors.rmi.server.Server;

import java.rmi.RemoteException;

public class FillLineValidator implements Validator<StepInfo> {

    public static final FillLineValidator instance = new FillLineValidator();

    private Server serverStub;

    private FillLineValidator() {}

    @Override
    public boolean isValid(StepInfo stepInfo) {
        try {
            return serverStub.isStepCorrect(stepInfo);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setServerStub(Server serverStub) {
        this.serverStub = serverStub;
    }
}
