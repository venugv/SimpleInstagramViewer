package com.codepath.simpleinstagramviewer.data;

/**
 * Created by vvenkatraman on 11/17/15.
 */
public class PictureComment {
    private String comment;
    private String userName;
    private String profilePicURL;
    private String submittedTime;

    public PictureComment(String comment, String userName, String profilePicURL,
                          String submittedTime) {
        this.comment = comment;
        this.userName = userName;
        this.profilePicURL = profilePicURL;
        this.submittedTime = submittedTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
