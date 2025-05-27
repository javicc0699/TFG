package com.example.helldiversbuildhub;

public class Planet {
    private int planetIndex; // Necesito
    private String name; // Necesito
    private String faction; // Necesito
    private int players;
    private int health;
    private int maxHealth;
    private double percentage;
    private boolean defense;
    private Biome biome;
    private String expireDateTime;

    public int getPlanetIndex() {
        return planetIndex;
    }

    public String getName() {
        return name;
    }

    public String getFaction() {
        return faction;
    }

    public int getPlayers() {
        return players;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getPercentage() {
        return percentage;
    }

    public boolean isDefense() {
        return defense;
    }

    public Biome getBiome() {
        return biome;
    }

    public String getExpireDateTime() {
        return expireDateTime;
    }

}
