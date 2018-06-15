package com.ahu.achievement.paper;

public class Code {
    private String codename;//代码文件名
    private String codedesc;//代码描述

    public Code(String codename, String codedesc) {
        this.codename = codename;
        this.codedesc = codedesc;
    }

    public Code() {
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getCodedesc() {
        return codedesc;
    }

    public void setCodedesc(String codedesc) {
        this.codedesc = codedesc;
    }
}
