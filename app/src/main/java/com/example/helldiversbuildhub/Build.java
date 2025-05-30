package com.example.helldiversbuildhub;

import java.util.List;

public class Build {
    public String id;
    public String userId;
    public String username;
    public String buildName;
    public String primaryWeapon;
    public String secondaryWeapon;
    public String armorPassive;
    public String booster;
    public String faction;
    public List<String> stratagems;
    public int likes;
    public int dislikes;
    public String date;

    public Build() {
    }

    // Constructor
    public Build(String id, String userId, String username, String buildName, String primaryWeapon, String secondaryWeapon, String armorPassive, String booster, String faction, List<String> stratagems, int likes, int dislikes, String date) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.buildName = buildName;
        this.primaryWeapon = primaryWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.armorPassive = armorPassive;
        this.booster = booster;
        this.faction = faction;
        this.stratagems = stratagems;
        this.likes = likes;
        this.dislikes = dislikes;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}


