package com.example.sjeong.pick;

/**
 * Created by mijin on 2017-12-01.
 */

public class Hot {
    String Name;
    Double maxRate;
    int bankIcon;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(Double maxRate) {
        this.maxRate = maxRate;
    }

    public int getBankIcon() {
        return bankIcon;
    }

    public void setBankIcon(int bankIcon) {
        this.bankIcon = bankIcon;
    }

    public Hot(String name, Double maxRate, int bankIcon) {
        Name = name;
        this.maxRate = maxRate;
        this.bankIcon = bankIcon;
    }
}
