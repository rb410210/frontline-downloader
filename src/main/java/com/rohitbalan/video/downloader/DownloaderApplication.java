package com.rohitbalan.video.downloader;

import com.rohitbalan.video.downloader.service.SiteAdaptor;
import com.rohitbalan.video.downloader.service.impl.FrontlineAdaptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class DownloaderApplication {

    public static void main(final String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DownloaderApplication.class, args);
        for (final String arg: args) {
            for(final Map.Entry<String, SiteAdaptor> siteAdaptorEntry :context.getBeansOfType(SiteAdaptor.class).entrySet()) {
                final SiteAdaptor siteAdaptor = siteAdaptorEntry.getValue();
                if(siteAdaptor.adaptable(arg)) {
                    siteAdaptor.download(arg);
                }
            }
        }
    }
}
