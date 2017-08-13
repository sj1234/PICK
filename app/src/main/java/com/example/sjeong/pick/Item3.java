package com.example.sjeong.pick;

/**
 * Created by mijin on 2017-08-13.
 */

public class Item3 {

    String targetTitle;
    int amount, targetAmaount, duration;

    public Item3(String targetTitle, int amount, int targetAmaount, int duration) {
        this.targetTitle = targetTitle;
        this.amount = amount;
        this.targetAmaount = targetAmaount;
        this.duration = duration;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTargetAmaount() {
        return targetAmaount;
    }

    public void setTargetAmaount(int targetAmaount) {
        this.targetAmaount = targetAmaount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "" + targetAmaount;
    }
}
