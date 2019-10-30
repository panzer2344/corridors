package ru.corridors.rmi;

import java.rmi.RemoteException;

@FunctionalInterface
public interface Action<C, P> {
    P tryDo(C... args) throws RemoteException;
}
