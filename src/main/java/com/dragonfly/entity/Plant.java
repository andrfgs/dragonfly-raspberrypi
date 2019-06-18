package com.dragonfly.entity;

import com.dragonfly.entityenum.LightingRequirements;
import com.dragonfly.entityenum.SunlightRequirements;
import com.dragonfly.entityenum.WaterRequirements;

public class Plant {

    private String name;
    private float seedDepth;
    private float spacing;
    private int growthTime;
    private int sunlightRequirements;
    private int lightingRequirements;
    private int waterRequirements;
    private float minTemperature;
    private float maxTemperature;

    public Plant() {}

    public String getName() {
        return name;
    }

    public float getSeedDepth() {
        return seedDepth;
    }

    public float getSpacing() {
        return spacing;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public SunlightRequirements getSunlightRequirementsEnum() {
        return SunlightRequirements.values()[sunlightRequirements];
    }

    public LightingRequirements getLightingRequirementsEnum() {
        return LightingRequirements.values()[lightingRequirements];
    }

    public WaterRequirements getWaterRequirementsEnum() {
        return WaterRequirements.values()[waterRequirements];
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", seedDepth=" + seedDepth +
                ", spacing=" + spacing +
                ", growthTime=" + growthTime +
                ", sunlightRequirements=" + sunlightRequirements +
                ", lightingRequirements=" + lightingRequirements +
                ", waterRequirements=" + waterRequirements +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                '}';
    }
}
