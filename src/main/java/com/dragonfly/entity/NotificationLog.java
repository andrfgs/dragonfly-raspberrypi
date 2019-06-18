package com.dragonfly.entity;

public class NotificationLog extends Log {
    private String content;
    private int level;
    private boolean solved;
    private int sector = -1;

    public NotificationLog() {}

    public NotificationLog(int unitID, String content, int level, boolean solved, int sector) {
        super.timestamp = System.currentTimeMillis();
        super.unitID = unitID;
        this.content = content;
        this.level = level;
        this.solved = solved;
        this.sector = sector;
    }

    public NotificationLog(int unitID, String content, int level, boolean solved) {
        super.timestamp = System.currentTimeMillis();
        super.unitID = unitID;
        this.content = content;
        this.level = level;
        this.solved = solved;
    }

    public String getContent() {
        return content;
    }

    public int getLevel() {
        return level;
    }

    public boolean isSolved() {
        return solved;
    }

    public int getSector() {
        return sector;
    }
}
