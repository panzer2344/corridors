package ru.corridors.dto;

import java.io.Serializable;
import java.util.Objects;

public class ClientInfo implements Serializable {
    private int orderNumber;

    public ClientInfo(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClientInfo that = (ClientInfo) object;
        return orderNumber == that.orderNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber);
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "orderNumber=" + orderNumber +
                '}';
    }
}
