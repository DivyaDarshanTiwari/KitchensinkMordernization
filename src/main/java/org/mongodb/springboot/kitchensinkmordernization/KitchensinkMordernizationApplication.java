package org.mongodb.springboot.kitchensinkmordernization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class KitchensinkMordernizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchensinkMordernizationApplication.class, args);
    }

}
