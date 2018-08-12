package com.rohitbalan.video.downloader.service;

import com.rohitbalan.video.downloader.model.Fragment;

import java.nio.file.Path;
import java.util.List;

public interface FragmentAssembler {
    void assemble(List<Fragment> fragments, Path path) throws Exception;
}
