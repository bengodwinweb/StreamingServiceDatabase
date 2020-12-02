package com.streamingservicebackend.model.mediaconnection;

public class MediaConnection {

    private final String mediaId;
    private final String mediaName;

    public MediaConnection(String mediaId, String mediaName) {
        this.mediaId = mediaId;
        this.mediaName = mediaName;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getMediaName() {
        return mediaName;
    }
}
