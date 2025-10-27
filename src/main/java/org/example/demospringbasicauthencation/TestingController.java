package org.example.demospringbasicauthencation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {

    @GetMapping("/test")
    public String test() {
        return "Testing";
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/user")
    public String user() {
        return "User";
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/admin")
    public String admin() {
        return "Admin";
    }

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/after-login")
    public String afterLogin() {
        return "Hello Authenticated";
    }
}
