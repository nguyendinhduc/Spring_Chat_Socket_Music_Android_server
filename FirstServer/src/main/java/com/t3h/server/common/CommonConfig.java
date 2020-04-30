package com.t3h.server.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CommonConfig {

    @Bean(name = "EncodePassword")
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        ///sdfsdf
        return new BCryptPasswordEncoder();
    }
}
