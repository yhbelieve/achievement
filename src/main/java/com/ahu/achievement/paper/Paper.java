package com.ahu.achievement.paper;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Paper {
    @Id
    private String id;
    private String authorsname;//作者姓名
    private String authorsid;//作者id
    private String papername;//论文名称
    private String readme;//readme文档内容
    private String paperdesc;//论文描述
    private String publish;//发布时间
    private String degreelevel;//论文等级
    private String tutorname;//导师姓名
    private List<Tags> tags;//标签：论文关键词
    private List<Code> code;//代码
    private List<Data> data;//实验数据
    private List<References> references;//引文
    private List<Others> others;//其他
    private int isshow;//默认1代表可以在界面上显示，0为管理员审核不通过，不能在界面上显示
    public Paper() {
    }

    public Paper(String id, String authorsname, String authorsid, String papername, String readme, String paperdesc, String publish, String degreelevel, String tutorname, List<Tags> tags, List<Code> code, List<Data> data, List<References> references, List<Others> others, int isshow) {
        this.id = id;
        this.authorsname = authorsname;
        this.authorsid = authorsid;
        this.papername = papername;
        this.readme = readme;
        this.paperdesc = paperdesc;
        this.publish = publish;
        this.degreelevel = degreelevel;
        this.tutorname = tutorname;
        this.tags = tags;
        this.code = code;
        this.data = data;
        this.references = references;
        this.others = others;
        this.isshow = isshow;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorsname() {
        return authorsname;
    }

    public void setAuthorsname(String authorsname) {
        this.authorsname = authorsname;
    }

    public String getAuthorsid() {
        return authorsid;
    }

    public void setAuthorsid(String authorsid) {
        this.authorsid = authorsid;
    }

    public String getPapername() {
        return papername;
    }

    public void setPapername(String papername) {
        this.papername = papername;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }

    public String getPaperdesc() {
        return paperdesc;
    }

    public void setPaperdesc(String paperdesc) {
        this.paperdesc = paperdesc;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getDegreelevel() {
        return degreelevel;
    }

    public void setDegreelevel(String degreelevel) {
        this.degreelevel = degreelevel;
    }

    public String getTutorname() {
        return tutorname;
    }

    public void setTutorname(String tutorname) {
        this.tutorname = tutorname;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public List<Code> getCode() {
        return code;
    }

    public void setCode(List<Code> code) {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<References> getReferences() {
        return references;
    }

    public void setReferences(List<References> references) {
        this.references = references;
    }

    public List<Others> getOthers() {
        return others;
    }

    public void setOthers(List<Others> others) {
        this.others = others;
    }
}
