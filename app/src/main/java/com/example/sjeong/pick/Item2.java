package com.example.sjeong.pick;

/**
 * Created by mijin on 2017-08-11.
 */

public class Item2 {

    int id;
    String title, profit, content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item2(int id, String title, String profit, String content) {

        this.id = id;
        this.title = title;
        this.profit = profit;
        this.content = content;

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
