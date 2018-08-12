package com.rohitbalan.video.downloader.service.impl;

import com.rohitbalan.video.downloader.model.Fragment;
import com.rohitbalan.video.downloader.service.FragmentSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

@Service
public class FragmentSearcherImpl implements FragmentSearcher {

    private Logger logger = LoggerFactory.getLogger(FragmentSearcherImpl.class);

    @Override
    public List<Fragment> search(final String url) throws IOException, URISyntaxException {
        logger.info("Downloading URL: {}", url);
        final String playlistContent = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        logger.debug("playlistContent: {}", playlistContent);
        return parse(playlistContent, new URI(url));
    }

    protected List<Fragment> parse(final String playlistContent, final URI playlistUrl) {
        final ArrayList<Fragment> fragments = new ArrayList<>();
        final URI parent = playlistUrl.getPath().endsWith("/") ? playlistUrl.resolve("..") : playlistUrl.resolve(".");
        final StringTokenizer tokenizer = new StringTokenizer(playlistContent, "\n");
        new File("./" + playlistUrl.toString().hashCode()).mkdirs();
        long index = 0l;
        while (tokenizer.hasMoreTokens()) {
            final String line = tokenizer.nextToken();
            if(!line.startsWith("#")) {
                final Fragment fragment = new Fragment();
                final String url = parent.toString() + line;
                logger.debug("fragment url: {}", url);
                fragment.setUrl(url);
                fragment.setNumber(index++);
                fragment.setPath(Paths.get("./" + playlistUrl.toString().hashCode() + "/" + fragment.getNumber() + ".ts"));
                fragments.add(fragment);
            }
        }
        return fragments;
    }
}
