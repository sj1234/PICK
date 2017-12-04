package com.example.sjeong.pick;

/**
 * Created by mijin on 2017-12-01.
 */

public class Hot {
    String Name;
    Double minRate,maxRate;
    int bankIcon, rank;
    int prod_code;

    public int getProd_code() {
        return prod_code;
    }

    public void setProd_code(int prod_code) {
        this.prod_code = prod_code;
    }

    public Double getMinRate() {
        return minRate;
    }

    public void setMinRate(Double minRate) {
        this.minRate = minRate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

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

    public Hot(String name, Double minRate, Double maxRate, int bankIcon, int rank, int prod_code) {
        this.Name = name;
        this.minRate = minRate;
        this.maxRate = maxRate;
        this.bankIcon = bankIcon;
        this.rank = rank;
        this.prod_code = prod_code;
    }
}
