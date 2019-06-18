package com.dragonfly.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plantation that = (Plantation) o;
        return sector == that.sector &&
                unitID == that.unitID &&
                sowDate == that.sowDate &&
                Objects.equals(sowedPlant, that.sowedPlant);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sector, unitID, sowedPlant, sowDate);
    }
}
