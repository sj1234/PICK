package com.example.sjeong.pick;

/**
 * Created by SJeong on 2017-11-27.
 */

public class ItemDetail {
    private String title, detail;

    public ItemDetail(String title, String detail){
        this.title = title;
        this.detail = detail;
    };

    public String getTitle() {return title;}

    public String getDetail() {return detail;}

}
