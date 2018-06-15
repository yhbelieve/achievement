package com.ahu.achievement.paper;

public class Data {
    private String dataname;//数据文件名
    private String datadesc;//数据描述

    public Data() {
    }

    public Data(String dataname, String datadesc) {
        this.dataname = dataname;
        this.datadesc = datadesc;
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getDatadesc() {
        return datadesc;
    }

    public void setDatadesc(String datadesc) {
        this.datadesc = datadesc;
    }
}
