package com.blissful.app.Model;


public class Albums_Model {
    private String course_name;
    private String imgid;

    private int id;

    public Albums_Model(int id, String course_name, String imgid) {
        this.course_name = course_name;
        this.imgid = imgid;
        this.id = id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public int getId() {
        return id;
    };

    public void setId(int id) {
        this.id = id;
    }

    ;
}