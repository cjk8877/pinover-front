package com.example.pictureplace;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import java.util.List;

public class MyPinDTO {
        @SerializedName("postingid")
        private int postingId;
        @SerializedName("disclosure")
        private String disclosure;
        @SerializedName("content")
        private String content;
        @SerializedName("locationid")
        private String locationId;
        @SerializedName("userid")
        private String userId;
        @SerializedName("postdate")
        private String postDate;
        @SerializedName("declaration")
        private int declaration;
        @SerializedName("pictures")
        private List<String> pictures;
        @SerializedName("tags")
        private List<String> tags;
        @SerializedName("recommendCount")
        private int recommendCount;
        @SerializedName("locationname")
        private String locationname;

        public String getLocationname(){return locationname;}

        public void  setLocationname(String locationname){this.locationname = locationname;}

        public int getPostingId() {
            return postingId;
        }

        public void setPostingId(int postingId) {
            this.postingId = postingId;
        }

        public String getDisclosure() {
            return disclosure;
        }

        public void setDisclosure(String disclosure) {
            this.disclosure = disclosure;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPostDate() {
            return postDate;
        }

        public void setPostDate(String postDate) {
            this.postDate = postDate;
        }

        public int getDeclaration() {
            return declaration;
        }

        public void setDeclaration(int declaration) {
            this.declaration = declaration;
        }

        public List<String> getPictures() {
            return pictures;
        }

        public void setPictures(List<String> pictures) {
            this.pictures = pictures;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public int getRecommendCount() {
            return recommendCount;
        }

        public void setRecommendCount(int recommendCount) {
            this.recommendCount = recommendCount;
        }

}


