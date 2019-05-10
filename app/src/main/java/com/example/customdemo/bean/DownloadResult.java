package com.example.customdemo.bean;

import java.io.Serializable;

public class DownloadResult implements Serializable {
    private String fileTotalLen;
    private String currentCountLen;
    private int progress;
    private String fileUrl;

    public String getFileTotalLen() {
        return fileTotalLen;
    }

    public void setFileTotalLen(String fileTotalLen) {
        this.fileTotalLen = fileTotalLen;
    }

    public String getCurrentCountLen() {
        return currentCountLen;
    }

    public void setCurrentCountLen(String currentCountLen) {
        this.currentCountLen = currentCountLen;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
