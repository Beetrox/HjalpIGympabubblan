package com.github.beetrox.hjalpigympabubblan2;

import java.util.List;

public class Drill {

    String userId;
    String name;
    String imageUrl;
    String description;
    String category;
    List<String> tags;

    public Drill() {

    }

    public Drill(String userId, String name, String imageUrl, String description, List<String> tags, String category) {
        this.userId = userId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.tags = tags;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
