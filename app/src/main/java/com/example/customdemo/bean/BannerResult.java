package com.example.customdemo.bean;

import java.io.Serializable;

/**
 * Created by ${LZQ} on 2019/4/26.
 */
public class BannerResult implements Serializable {
    /**
     * url : string
     * link : string
     */

    private String url;
    private String link;
    private int linkType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }
}
