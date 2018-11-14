package com.github.beetrox.hjalpigympabubblan2;

public class Skill {
    public String name;
    public double difficulty;

    public Skill(String name, double difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }
}