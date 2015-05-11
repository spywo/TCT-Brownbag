package com.autodesk.icp.community.mobile.ui;


public class Item {
    private String imageUrl;
    private String title;
    private String description;
    private String timestamp;

    public Item() {
        
    }
    public Item(String imageUrl, String title, String description, String timestamp) {
        super();
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }
    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    
}
