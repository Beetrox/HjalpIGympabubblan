package com.github.beetrox.hjalpigympabubblan2;

import java.util.List;

public class Drill {
    String name;
    String imageUrl;
    String description;
    List<String> tags;

    public Drill(String name, String imageUrl, String description, List<String> tags) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.tags = tags;
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
}
