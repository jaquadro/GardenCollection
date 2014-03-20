package com.jaquadro.minecraft.modularpots.config;

import java.util.ArrayList;
import java.util.List;

public class PatternConfig
{
    private int patternId;
    private String overlay;
    private String name;
    private List<String> genLocations = new ArrayList<String>();
    private List<Integer> genRarity = new ArrayList<Integer>();

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

    public int getLocationCount () {
        return genLocations.size();
    }

    public String getGenLocation (int index) {
        return genLocations.get(index);
    }

    public int getGenRarity (int index) {
        return genRarity.get(index);
    }

    public void setName (String name) {
        this.name = name;
    }

    public void addGenLocation (String location, int rarity) {
        genLocations.add(location);
        genRarity.add(rarity);
    }
}
