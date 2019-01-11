package com.rohitbalan.video.downloader.service;

public interface SiteAdaptor {
    void download(String url);
    void download(String url, String fileName);
    boolean adaptable(String url);
}
