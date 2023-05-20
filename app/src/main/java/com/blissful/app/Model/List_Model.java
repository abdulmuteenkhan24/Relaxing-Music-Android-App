package com.blissful.app.Model;

public class List_Model {
    private String course_name = "";
    private String course_rating = "";
    private String VideoUrl = "";

    // Constructor
    public List_Model(String course_name, String VideoUrl) {
        this.course_name = course_name;
        this.VideoUrl = VideoUrl;
        //  this.course_rating = course_rating;
//      this.course_image = course_image;
    }

    // Getter and Setter
    public String getCourse_name() {
        return course_name;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setVideoUrl(String VideoUrl) {
        this.VideoUrl = VideoUrl;
    }

    public String getCourse_rating() {
        return course_rating;
    }

//    public void setCourse_rating(int course_rating) {
//        this.course_rating = course_rating;
//    }

//    public int getCourse_image() {
//        return course_image;
//    }
//
//    public void setCourse_image(int course_image) {
//        this.course_image = course_image;
//    }
}
