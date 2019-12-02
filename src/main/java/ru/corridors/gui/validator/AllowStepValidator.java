package ru.corridors.gui.validator;

public class AllowStepValidator implements Validator<Void> {

    public static final AllowStepValidator instance = new AllowStepValidator();

    private boolean isAllow = false;

    private AllowStepValidator() {}

    @Override
    public boolean isValid(Void object) {
        return isAllow;
    }

    public void setAllow(boolean allow) {
        isAllow = allow;
    }
}
