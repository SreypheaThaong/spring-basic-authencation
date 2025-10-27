package org.example.demospringbasicauthencation;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class DemoSpringBasicAuthencationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringBasicAuthencationApplication.class, args);
    }

}
