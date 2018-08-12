package com.rohitbalan.video.downloader.service;

import java.io.IOException;

public interface YoutubeDLBridge {
    String getUrl(String url) throws IOException, InterruptedException;
    String getFileName(String url) throws IOException, InterruptedException;
}
