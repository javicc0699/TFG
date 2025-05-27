package com.example.helldiversbuildhub;

public class PlanetStatus {

    private int index;
    private int owner;
    private int health;
    private double regenPerSecond;
    private int players;
    private Position position;

    public int getIndex() {
        return index;
    }

    public int getOwner() {
        return owner;
    }

    public int getHealth() {
        return health;
    }

    public double getRegenPerSecond() {
        return regenPerSecond;
    }

    public int getPlayers() {
        return players;
    }

    public Position getPosition() {
        return position;
    }
}
