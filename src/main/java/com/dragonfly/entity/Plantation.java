package com.dragonfly.entity;

public class Plantation {
    private int sector;
    private int unitID;
    private String sowedPlant;
    private long sowDate;

    public Plantation() {}

    public Plantation(int sector, int unitID, String sowedPlant, long sowDate) {
        this.sector = sector;
        this.unitID = unitID;
        this.sowedPlant = sowedPlant;
        this.sowDate = sowDate;
    }

    public int getSector() {
        return sector;
    }

    public int getUnitID() {
        return unitID;
    }

    public String getSowedPlant() {
        return sowedPlant;
    }

    public long getSowDate() {
        return sowDate;
    }
}
