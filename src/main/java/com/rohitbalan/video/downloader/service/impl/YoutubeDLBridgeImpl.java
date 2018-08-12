package com.rohitbalan.video.downloader.service.impl;

import com.rohitbalan.video.downloader.service.YoutubeDLBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class YoutubeDLBridgeImpl implements YoutubeDLBridge {
    private Logger logger = LoggerFactory.getLogger(YoutubeDLBridgeImpl.class);

    @Override
    public String getUrl(String url) throws IOException, InterruptedException {
        final String playListUrl = runOption(url, "--get-url");
        logger.info("Downloading: {}", playListUrl);
        return playListUrl;
    }

    @Override
    public String getFileName(String url) throws IOException, InterruptedException {
        final String playListUrl = runOption(url, "--get-filename");
        logger.info("File Name: {}", playListUrl);
        return playListUrl;
    }

    private String runOption(final String url, final String option)  throws IOException, InterruptedException {
        final ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "youtube-dl " + option + " '" + url + "'");
        //pb.inheritIO();
        final Process p = pb.start();
        p.waitFor();
        final BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String output = "";
        String line = "";

        while ((line = b.readLine()) != null) {
            output = line;
            //logger.debug("Line: {}", line);
        }

        b.close();
        return output;
    }
}
