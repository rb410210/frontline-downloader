package com.rohitbalan.video.downloader.service.impl;

import com.rohitbalan.video.downloader.model.Fragment;
import com.rohitbalan.video.downloader.service.FragmentAssembler;
import com.rohitbalan.video.downloader.service.FragmentDownloader;
import com.rohitbalan.video.downloader.service.FragmentSearcher;
import com.rohitbalan.video.downloader.service.SiteAdaptor;
import org.apache.commons.io.FilenameUtils;
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
public class M3UDownloader implements SiteAdaptor {

    private Logger logger = LoggerFactory.getLogger(com.rohitbalan.video.downloader.service.impl.FrontlineAdaptor.class);

    @Autowired
    private FragmentSearcher fragmentSearcher;

    @Autowired
    private FragmentDownloader fragmentDownloader;

    @Autowired
    private FragmentAssembler fragmentAssembler;


    @Override
    public void download(final String playListUrl) {
        try {
            final String fileName = FilenameUtils.getBaseName(playListUrl) + ".mp4";
            download(playListUrl, fileName);
        } catch(final Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void download(final String playListUrl, final String fileName) {
        try {
            if(playListUrl.indexOf(".m3u") > -1) {
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
    public boolean adaptable(final String playListUrl) {
        return playListUrl.indexOf(".m3u") > -1;
    }
}
