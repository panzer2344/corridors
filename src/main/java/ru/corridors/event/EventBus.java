package ru.corridors.event;

public class EventBus {

    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void releaseEvent() {
        event = null;
    }
}
