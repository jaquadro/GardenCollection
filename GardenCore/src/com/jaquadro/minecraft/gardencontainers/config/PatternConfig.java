package com.jaquadro.minecraft.gardencontainers.config;

public class PatternConfig
{
    private int patternId;
    private String overlay;
    private String name;
    private int weight;

    public PatternConfig (int id, String overlay, String name) {
        this.patternId = id;
        this.overlay = overlay;
        this.name = name;
    }

    public int getId () {
        return patternId;
    }

    public String getOverlay () {
        return overlay;
    }

    public String getName () {
        return name;
    }

    public int getWeight () {
        return weight;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setWeight (int weight) {
        this.weight = weight;
    }
}
