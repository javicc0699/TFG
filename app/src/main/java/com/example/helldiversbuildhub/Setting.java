package com.example.helldiversbuildhub;

import java.util.List;

public class Setting {
    private int type;
    private String overrideTitle;
    private String overrideBrief;
    private String taskDescription;
    private List<Task> tasks;
    private Reward reward;
    private int flags;

    public Setting() { }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOverrideTitle() {
        return overrideTitle;
    }

    public void setOverrideTitle(String overrideTitle) {
        this.overrideTitle = overrideTitle;
    }

    public String getOverrideBrief() {
        return overrideBrief;
    }

    public void setOverrideBrief(String overrideBrief) {
        this.overrideBrief = overrideBrief;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}