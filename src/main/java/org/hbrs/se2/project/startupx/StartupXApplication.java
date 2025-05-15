package org.hbrs.se2.project.startupx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class StartupXApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StartupXApplication.class, args);
    }

}
