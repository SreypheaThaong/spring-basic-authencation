package org.example.demospringbasicauthencation.controller;

import org.example.demospringbasicauthencation.model.AppUser;
import org.example.demospringbasicauthencation.model.AppUserDto;
import org.example.demospringbasicauthencation.model.AppUserRequest;
import org.example.demospringbasicauthencation.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDto> registerUser(@RequestBody AppUserRequest appUserRequest) {

        AppUserDto appUser = appUserService.registerUser(appUserRequest);

        return ResponseEntity.ok(appUser);
    }
}
