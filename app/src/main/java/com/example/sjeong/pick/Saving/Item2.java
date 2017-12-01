package com.example.sjeong.pick.Saving;

/**
 * Created by mijin on 2017-08-11.
 */

public class Item2 {


    int id, prime_cond;
    String title, profit, content;

    public int getPrime_cond() {
        return prime_cond;
    }

    public void setPrime_cond(int prime_cond) {
        this.prime_cond = prime_cond;
    }

    public Item2(int id, int prime_cond, String title, String profit, String content) {

        this.id=id;
        this.prime_cond=prime_cond;
        this.title = title;
        this.profit = profit;
        this.content = content;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getProfit() {
        return profit;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
