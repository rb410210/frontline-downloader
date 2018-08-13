package com.rohitbalan.video.downloader;

import com.rohitbalan.video.downloader.service.impl.FrontlineAdaptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DownloaderApplication {

    public static void main(final String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DownloaderApplication.class, args);
        for (final String arg: args) {
            context.getBean(FrontlineAdaptor.class).download(arg);
        }
    }
}
