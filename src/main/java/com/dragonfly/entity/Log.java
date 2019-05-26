package com.dragonfly.entity;

public abstract class Log {
    protected int logID;
    protected long timestamp;
    protected int unitID;

    public int getLogID() {
        return logID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getUnitID() {
        return unitID;
    }
}
