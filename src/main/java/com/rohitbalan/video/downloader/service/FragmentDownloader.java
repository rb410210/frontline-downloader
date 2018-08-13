package com.rohitbalan.video.downloader.service;

import com.rohitbalan.video.downloader.model.Fragment;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public interface FragmentDownloader {
    Callable<Void> download(Fragment fragment, CountDownLatch countDownLatch, int total);
}
