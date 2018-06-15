package com.ahu.achievement.paper;

import java.util.List;

public class References {
    private String referencename;//引文文件名称
    private String referencedesc;//引文描述
    private String url;//引文url
    private List<Tags> tags;//引文标签

    public References() {
    }

    public References(String referencename, String referencedesc, String url, List<Tags> tags) {
        this.referencename = referencename;
        this.referencedesc = referencedesc;
        this.url = url;
        this.tags = tags;
    }

    public String getReferencename() {
        return referencename;
    }

    public void setReferencename(String referencename) {
        this.referencename = referencename;
    }

    public String getReferencedesc() {
        return referencedesc;
    }

    public void setReferencedesc(String referencedesc) {
        this.referencedesc = referencedesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
}
