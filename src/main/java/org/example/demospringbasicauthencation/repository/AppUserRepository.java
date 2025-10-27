package org.example.demospringbasicauthencation.repository;

import org.apache.ibatis.annotations.*;
import org.example.demospringbasicauthencation.model.AppUser;
import org.example.demospringbasicauthencation.model.AppUserRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Mapper
public interface AppUserRepository {

    @Select("""
        insert into app_user(email, password, role, phone_number)
        values (#{user.email}, #{user.password}, #{user.role}, #{user.phoneNumber} )
        returning *;
    """)
    @Result(property = "phoneNumber", column = "phone_number")
    AppUser insertUser(@Param("user") AppUserRequest appUserRequest);


//    -----------
    @Select("""
        select * from app_user
        where email = #{email}
    """)
    AppUser findUserByEmail(String email);
}
