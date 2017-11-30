package com.example.sjeong.pick;

/**
 * Created by SJeong on 2017-11-27.
 */

public class Goal {
    private String name, month, start_sum, monthly, rate, com_sim, start, fail;

    public Goal(String name, String month, String start_sum, String monthly, String rate, String com_sim, String start, String fail){
        this.name = name;
        this.month = month;
        this.start_sum = start_sum;
        this.monthly = monthly;
        this.rate = rate;
        this.com_sim = com_sim;
        this.start = start;
        this.fail = fail;
    }

    public String getName() {return name;}

    public String getMonth() {return month;}

    public String getStart_sum(){return start_sum;}

    public String getMonthly() {return monthly;}

    public String getRate() {return rate;}

    public String getCom_sim() {return com_sim;}

    public String getStart() {return start;}

    public String getFail() {return fail;}
}
