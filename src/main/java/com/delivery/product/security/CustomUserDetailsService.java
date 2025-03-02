package com.delivery.product.security;

import com.delivery.product.mapper.UserVO;
import com.delivery.product.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserVO> user = userService.findByUserEmail(username);
        if(user.isPresent()){
            logger.info("User Authenticated Successfully..!!!");
            user.get().setUserName(username);
            return new CustomUserDetails(user.get());
        }else{
            logger.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }

    }
}
