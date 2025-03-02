package com.delivery.product.services.impl;

import com.delivery.product.constant.AppConstant;
import com.delivery.product.exception.CustomeException;
import com.delivery.product.mapper.UserVO;
import com.delivery.product.model.UserEntity;
import com.delivery.product.repository.UserRepository;
import com.delivery.product.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Transactional
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserVO> findAllUser() {
        List<UserVO> userVOList = new ArrayList<>();
        try{
            userRepository.findAll().forEach(e -> {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(e, userVO);
                userVOList.add(userVO);
            });
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return userVOList;
    }

    @Override
    public Optional<UserVO> findByUserId(Long userId) {
        try{
            Optional<UserEntity> userEntity = userRepository.findById(userId);
            if(userEntity.isPresent()){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userEntity.get(), userVO);
                return Optional.of(userVO);
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserVO> findByUserEmail(String email) {
        try{
            Optional<UserEntity> userEntity = userRepository.findByEmail(email);
            if(userEntity.isPresent()){
               UserVO userVO = new UserVO();
               BeanUtils.copyProperties(userEntity.get(), userVO);
               return Optional.of(userVO);
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return Optional.empty();
    }

    @Override
    public boolean isExistUser(String email) {
        try{
            Optional<UserEntity> userEntity = userRepository.findByEmail(email);
            if(userEntity.isPresent()){
                return true;
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return false;
    }

    @Override
    public String validateUserDetails(UserVO userVO) {
        String error = "";
        try{
            if(StringUtils.isBlank(userVO.getEmail())){
                error = error + " User Email";
            }
            if(StringUtils.isBlank(userVO.getFirstName())){
                error = error + " User First Name";
            }
            if(StringUtils.isBlank(userVO.getLastName())){
                error = error + " User Last Name";
            }
            if(StringUtils.isBlank(userVO.getUserType().name())){
                error = error + " User Type";
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return error;
    }

    @Override
    public Optional<UserVO> saveUser(UserVO userVO) {
        try{
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(userVO, userEntity);
            userEntity = userRepository.save(userEntity);
            userVO.setUserId(userEntity.getUserId());
            return Optional.of(userVO);
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    @Override
    public String deleteUser(Long userId) {
        try{
            if(userRepository.findById(userId).isPresent()){
                userRepository.deleteById(userId);
                return AppConstant.SUCCESS;
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return AppConstant.FAILED;
    }
}
