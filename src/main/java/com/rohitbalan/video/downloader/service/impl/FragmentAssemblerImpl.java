package com.rohitbalan.video.downloader.service.impl;

import com.rohitbalan.video.downloader.model.Fragment;
import com.rohitbalan.video.downloader.model.Status;
import com.rohitbalan.video.downloader.service.FragmentAssembler;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

@Service
public class FragmentAssemblerImpl implements FragmentAssembler {

    @Override
    public void assemble(final List<Fragment> fragments, final Path outFile) throws Exception {
        precheck(fragments);
        try(FileChannel out = FileChannel.open(outFile, CREATE, WRITE)) {
            for(final Fragment fragment: fragments) {
                final Path inFile = fragment.getPath();
                try(FileChannel in=FileChannel.open(inFile, READ)) {
                    for(long p=0, l=in.size(); p<l; ) {
                        p+=in.transferTo(p, l-p, out);
                    }
                }
            }
        }
    }

    protected void precheck(final List<Fragment> fragments) throws Exception {
        for(final Fragment fragment: fragments) {
            if(fragment.getStatus() != Status.COMPLETED) {
                throw new Exception("Fragment " + fragment.getNumber() + " was not downloaded");
            }
        }
    }
}
