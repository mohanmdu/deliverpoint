package com.delivery.product.config;

import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.enumeration.UserType;
import com.delivery.product.mapper.UserVO;
import com.delivery.product.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationStartInit {

    private final Logger logger = LoggerFactory.getLogger(ApplicationStartInit.class);

    @Value("${default-password}")
    private String defaultPassword;
    private final IUserService userService;
    @Autowired
    public ApplicationStartInit(IUserService userService) {
        this.userService = userService;
    }

    @EventListener(classes = ApplicationStartedEvent.class)
    public void onApplicationEvent(ApplicationStartedEvent event){
        ObjectMapper mapper = new ObjectMapper();
        try{
            Optional<UserVO> userVO = userService.findByUserContact("1234567890");
            if(userVO.isPresent()){
                logger.info(String.format("Default user already exists %s ", "1234567890"));
            } else {
                UserVO userVO1 = new UserVO();
                userVO1.setName("admin");
                userVO1.setStatus(UserStatus.ACTIVE);
                userVO1.setMobileNumber("1234567890");
                userVO1.setUserType(UserType.ADMIN);
                userVO1.setUserName("1234567890");
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodedPassword = encoder.encode(defaultPassword);
                userVO1.setPassword(encodedPassword);
                userService.saveUser(userVO1);
            }
        }catch (Exception e){
            logger.error("Exception occurred in ", e);
        }
    }
}
