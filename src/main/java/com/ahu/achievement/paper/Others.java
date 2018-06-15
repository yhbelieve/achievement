package com.ahu.achievement.paper;

public class Others {
    private String othersname;//others文件名
    private  String  othersdesc;//others描述

    public Others() {
    }

    public Others(String othersname, String othersdesc) {
        this.othersname = othersname;
        this.othersdesc = othersdesc;
    }

    public String getOthersname() {
        return othersname;
    }

    public void setOthersname(String othersname) {
        this.othersname = othersname;
    }

    public String getOthersdesc() {
        return othersdesc;
    }

    public void setOthersdesc(String othersdesc) {
        this.othersdesc = othersdesc;
    }
}
