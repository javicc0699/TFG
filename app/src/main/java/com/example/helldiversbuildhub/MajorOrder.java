package com.example.helldiversbuildhub;

import java.util.List;

public class MajorOrder {
    private long id32;
    private List<Integer> progress;
    private int expiresIn;
    private Setting setting;

    public MajorOrder() { }

    public long getId32() {
        return id32;
    }

    public void setId32(long id32) {
        this.id32 = id32;
    }

    public List<Integer> getProgress() {
        return progress;
    }

    public void setProgress(List<Integer> progress) {
        this.progress = progress;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}
