package org.example.demospringbasicauthencation.service;

import org.example.demospringbasicauthencation.model.AppUser;
import org.example.demospringbasicauthencation.model.AppUserDto;
import org.example.demospringbasicauthencation.model.AppUserRequest;
import org.example.demospringbasicauthencation.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImp implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    public AppUserServiceImp(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public AppUserDto registerUser(AppUserRequest appUserRequest) {

        String encodedPassword = passwordEncoder.encode(appUserRequest.getPassword());

        appUserRequest.setPassword(encodedPassword);

         AppUser appUser = appUserRepository.insertUser(appUserRequest);

         return modelMapper.map(appUser, AppUserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findUserByEmail(email);
    }
}
