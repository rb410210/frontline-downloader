package com.rohitbalan.video.downloader.service;

import com.rohitbalan.video.downloader.model.Fragment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FragmentSearcher {

    List<Fragment> search(String url) throws IOException, URISyntaxException;

}
