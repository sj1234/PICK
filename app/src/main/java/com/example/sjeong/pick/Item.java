package com.example.sjeong.pick;

/**
 * Created by SJeong on 2017-08-11.
 */

public class Item {

    private String code, name, bank, cont_rate, max_rate;// 약정금리, 최대금리

    public String getCode() {return code;}

    public void setCode(String code) {this.code = code;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getBank() {return bank;}

    public void setBank(String bank) {this.bank = bank;}

    public String getCont_rate() {return cont_rate;}

    public void setCont_rate(String cont_rate) {this.cont_rate = cont_rate;}

    public String getMax_rate() {return max_rate;}

    public void setMax_rate(String max_rate) {this.max_rate = max_rate;}
}
