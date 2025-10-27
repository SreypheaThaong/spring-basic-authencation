package org.example.demospringbasicauthencation.service;

import org.example.demospringbasicauthencation.model.AppUser;
import org.example.demospringbasicauthencation.model.AppUserDto;
import org.example.demospringbasicauthencation.model.AppUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {

    AppUserDto registerUser(AppUserRequest appUserRequest);
}
