package com.cola.service.user.configuration;

import com.cola.service.user.service.UserService;
import com.cola.service.user.service.impl.UserServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiachen.shi on 6/20/2017.
 */
@Configuration
public class UserConfiguration {

    @Bean(name="userService")
    @ConditionalOnClass(UserService.class)
    public UserService userService(){
        UserService userService = new UserServiceImpl();
        return userService;
    }

}
