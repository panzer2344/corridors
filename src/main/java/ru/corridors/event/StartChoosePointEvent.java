package ru.corridors.event;

import ru.corridors.gui.model.PointBtn;

public class StartChoosePointEvent extends Event {
    private PointBtn startBtn;
    private PointBtn endBtn;

    public StartChoosePointEvent(PointBtn startBtn) {
        this.startBtn = startBtn;
    }

    public PointBtn getStartBtn() {
        return startBtn;
    }

    public PointBtn getEndBtn() {
        return endBtn;
    }

    public void setEndBtn(PointBtn endBtn) {
        this.endBtn = endBtn;
    }
}
