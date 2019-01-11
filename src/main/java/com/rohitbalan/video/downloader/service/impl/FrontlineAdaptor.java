package com.rohitbalan.video.downloader.service.impl;

import com.rohitbalan.video.downloader.model.Fragment;
import com.rohitbalan.video.downloader.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FrontlineAdaptor implements SiteAdaptor {

    private Logger logger = LoggerFactory.getLogger(FrontlineAdaptor.class);

    @Autowired
    private FragmentSearcher fragmentSearcher;

    @Autowired
    private FragmentDownloader fragmentDownloader;

    @Autowired
    private FragmentAssembler fragmentAssembler;

    @Autowired
    private YoutubeDLBridge youtubeDLBridge;

    @Override
    public void download(final String url) {
        try {
            final String fileName = youtubeDLBridge.getFileName(url);
            download(url, fileName);
        } catch(final Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void download(final String url, final String fileName) {
        try {
            final String playListUrl = youtubeDLBridge.getUrl(url);
            if(playListUrl.indexOf("m3u") > -1) {
                final List<Fragment> fragments = fragmentSearcher.search(playListUrl);
                logger.debug("fragments count: {}, fragments: {}", fragments.size(), fragments);

                final CountDownLatch countDownLatch = new CountDownLatch(fragments.size());
                final ExecutorService executor = Executors.newFixedThreadPool(2);
                for(final Fragment fragment: fragments) {
                    executor.submit(fragmentDownloader.download(fragment, countDownLatch, fragments.size()));
                }
                countDownLatch.await();
                executor.shutdown();

                fragmentAssembler.assemble(fragments, Paths.get(fileName));
            } else {
                final URL fileUrl = new URL(playListUrl);
                final ReadableByteChannel rbc = Channels.newChannel(fileUrl.openStream());
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                rbc.close();
            }
        } catch(final Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean adaptable(final String url) {
        return url.indexOf(".pbs.org/") > -1;
    }
}
