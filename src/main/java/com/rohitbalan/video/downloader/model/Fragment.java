package com.rohitbalan.video.downloader.model;

import java.nio.file.Path;

import static com.rohitbalan.video.downloader.model.Status.NOT_STARTED;

public class Fragment {

    private Path path;
    private String url;
    private long number;
    private Status status = NOT_STARTED;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Fragment{" +
                "path=" + path +
                ", url='" + url + '\'' +
                ", number=" + number +
                ", status=" + status +
                '}';
    }
}
