package ru.corridors.gui.validator;

public interface Validator<T> {
    boolean isValid(T object);
}
