package org.mongodb.springboot.kitchensinkmordernization;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@OpenAPIDefinition(info = @Info(title = "KitchenSink APIs",version = "1.0.0",description = "Apis for KitchenSink"))
public class KitchensinkMordernizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchensinkMordernizationApplication.class, args);
    }

}
