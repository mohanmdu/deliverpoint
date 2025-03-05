package com.delivery.product.services;

import com.delivery.product.mapper.UserVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {

    List<UserVO> findAllUser();

    Optional<UserVO> findByUserId(Long userId);

    String validateUserDetails(UserVO userVO);

    Optional<UserVO> saveUser(UserVO userVO);

    String deleteUser(Long userId);

    Optional<UserVO> findByUserContact(String mobileNumber);
}
