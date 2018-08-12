package com.rohitbalan.video.downloader.service.impl;

import com.rohitbalan.video.downloader.model.Fragment;
import com.rohitbalan.video.downloader.model.Status;
import com.rohitbalan.video.downloader.service.FragmentDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

@Service
public class FragmentDownloaderImpl implements FragmentDownloader {
    private Logger logger = LoggerFactory.getLogger(FragmentDownloaderImpl.class);

    @Override
    public Callable<Void> download(final Fragment fragment, final CountDownLatch countDownLatch) {
        return () -> {
            try {
                download(fragment, 0);
                return null;
            } finally {
                countDownLatch.countDown();
            }
        };
    }

    protected void download(final Fragment fragment, final int attempt) throws IOException {
        try {
            logger.info("Downloading fragment number: {}  ", fragment.getNumber());
            fragment.setStatus(Status.PROCESSING);
            final URL website = new URL(fragment.getUrl());
            final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            final String name = fragment.getPath().toString();
            final FileOutputStream fos = new FileOutputStream(name);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fragment.setStatus(Status.COMPLETED);
            logger.info("Downloaded {} to {} ", fragment.getUrl(), name);
        } catch (IOException e) {
            if(attempt > 5) {
                logger.error(e.getMessage(), e);
                throw e;
            } else {
                fragment.setStatus(Status.NOT_STARTED);
                download(fragment, attempt + 1);
            }

        }
    }
}
