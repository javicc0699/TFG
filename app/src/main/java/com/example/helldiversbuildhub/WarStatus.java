package com.example.helldiversbuildhub;

import java.util.List;

public class WarStatus {

    private int warId;
    private long time;
    private double impactMultiplier;
    private int storyBeatId32;
    private List<PlanetStatus> planetStatus;

    public int getWarId() {
        return warId;
    }

    public long getTime() {
        return time;
    }

    public double getImpactMultiplier() {
        return impactMultiplier;
    }

    public int getStoryBeatId32() {
        return storyBeatId32;
    }

    public List<PlanetStatus> getPlanetStatus() {
        return planetStatus;
    }
}
