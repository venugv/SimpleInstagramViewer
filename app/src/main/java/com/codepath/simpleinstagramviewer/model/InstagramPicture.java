package com.codepath.simpleinstagramviewer.model;

import java.util.ArrayList;

/**
 * Created by vvenkatraman on 11/17/15.
 */
public class InstagramPicture {
    String username;
    private String type;
    String caption;
    String imageURL;
    private String videoURL;
    int imageHeight;
    int likes;
    String profilePicURL;
    String submittedTime;
    int commentsCount;
    private String id;
    ArrayList<InstagramPictureComment> comments = new ArrayList<InstagramPictureComment>();

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public ArrayList<InstagramPictureComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<InstagramPictureComment> comments) {
        this.comments = comments;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getSubmittedTime() {
        return submittedTime;
    }

    public void setSubmittedTime(String submittedTime) {
        this.submittedTime = submittedTime;
    }

    @Override
    public String toString() {
        return "InstagramPicture{" +
                "username='" + username + '\'' +
                ", caption='" + caption + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", imageHeight='" + imageHeight + '\'' +
                ", likes=" + likes +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}