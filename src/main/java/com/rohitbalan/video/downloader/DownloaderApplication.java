package com.rohitbalan.video.downloader;

import com.rohitbalan.video.downloader.service.SiteAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@SpringBootApplication
public class DownloaderApplication implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(DownloaderApplication.class);

    @Autowired
    private Map<String, SiteAdaptor> siteAdaptors;

    public static void main(final String[] args) {
        SpringApplication.run(DownloaderApplication.class, args);

    }

    @Override
    public void run(final ApplicationArguments applicationArguments) throws Exception {
        logger.debug("applicationArguments: {}", applicationArguments);
        final Set<String> options = new TreeSet<>(applicationArguments.getOptionNames());
        for(final String option: options) {
            if(option.startsWith("url")) {
                final String fileNameOption = option.replace("url", "file");
                if(options.contains(fileNameOption)) {
                    callAdaptor(applicationArguments.getOptionValues(option).get(0), applicationArguments.getOptionValues(fileNameOption).get(0));
                } else {
                    callAdaptor(applicationArguments.getOptionValues(option).get(0), null);
                }
            }
        }
        for(final String nonOptionArg: applicationArguments.getNonOptionArgs()) {
            callAdaptor(nonOptionArg, null);
        }
    }

    private void callAdaptor(final String url, final String filename) {
        for(final Map.Entry<String, SiteAdaptor> siteAdaptorEntry :siteAdaptors.entrySet()) {
            final SiteAdaptor siteAdaptor = siteAdaptorEntry.getValue();
            if(siteAdaptor.adaptable(url)) {
                if(filename!=null) {
                    siteAdaptor.download(url, filename);
                } else {
                    siteAdaptor.download(url);
                }
            }
        }
    }


}
