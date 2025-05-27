package com.example.helldiversbuildhub;

import java.util.List;

public class Build {

    private String id;
    private String userId;
    private String primaryWeapon;
    private String secondaryWeapon;
    private String armorPassive;
    private List<String> stratagems;
    private int likes;
    private int dislikes;
    private String date;

    public Build(){}

    public Build(String userId, String id, String primaryWeapon, String secondaryWeapon, String armorPassive, List<String> stratagems, int likes, int dislikes, String date) {
        this.userId = userId;
        this.id = id;
        this.primaryWeapon = primaryWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.armorPassive = armorPassive;
        this.stratagems = stratagems;
        this.likes = 0;
        this.dislikes = 0;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrimaryWeapon() {
        return primaryWeapon;
    }

    public void setPrimaryWeapon(String primaryWeapon) {
        this.primaryWeapon = primaryWeapon;
    }

    public String getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public void setSecondaryWeapon(String secondaryWeapon) {
        this.secondaryWeapon = secondaryWeapon;
    }

    public String getArmorPassive() {
        return armorPassive;
    }

    public void setArmorPassive(String armorPassive) {
        this.armorPassive = armorPassive;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getStratagems() {
        return stratagems;
    }

    public void setStratagems(List<String> stratagems) {
        this.stratagems = stratagems;
    }
}
