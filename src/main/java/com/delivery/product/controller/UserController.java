package com.delivery.product.controller;

import com.delivery.product.constant.AppConstant;
import com.delivery.product.constant.MessageConstant;
import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.mapper.ResponseVO;
import com.delivery.product.mapper.UserVO;
import com.delivery.product.services.IUserService;
import com.delivery.product.util.AppUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Delivery App User Service", description = "User Service API")
@RestController
@RequestMapping("/dlr/user")
public class UserController {

    private final IUserService userService;
    private final AppUtil appUtil;

    @Autowired
    public UserController(IUserService userService, AppUtil appUtil) {
        this.userService = userService;
        this.appUtil = appUtil;
    }

    @Operation(summary = "Get All User Service", description = "Find All User Data", tags = {"Delivery Get All User"})
    @GetMapping(value = "/get-all-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getAll(){
        return new ResponseEntity<>(appUtil.successResponse(userService.findAllUser(), AppConstant.USER_RESPONSE_VO, MessageConstant.USER_GET_ALL_MSG), HttpStatus.OK);
    }

    @Operation(summary = "Get User By Id Service", description = "Find User By Id Data", tags = {"Delivery Get User By Id"})
    @GetMapping(value = "/get-by-user-id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getByUserId(@PathVariable Long userId){
        Optional<UserVO> userVO = userService.findByUserId(userId);
        return userVO.map(vo -> new ResponseEntity<>(appUtil.successResponse(vo, AppConstant.USER_RESPONSE_VO, MessageConstant.USER_GET_BY_ID_MSG), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(appUtil.failedResponse(MessageConstant.NO_DATA_FOUND, String.format(MessageConstant.USER_DATA_NO_FOUND_MSG, userId)), HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Save User Service", description = "Save User Data", tags = {"Delivery Save User"})
    @PostMapping(value = "/save-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> saveUser(@RequestBody UserVO userVO){
        userVO.setUserName(userVO.getEmail());
        Optional<UserVO> userVODb = userService.findByUserEmail(userVO.getEmail());
        Optional<UserVO> userVOContactDb = userService.findByUserContact(userVO.getMobileNumber());
        if(userVODb.isEmpty() && userVOContactDb.isEmpty()){
            String error = userService.validateUserDetails(userVO);
            if(!StringUtils.isBlank(error)){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.INPUT_ERROR, error)), HttpStatus.BAD_REQUEST);
            } else {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodedPassword = encoder.encode(userVO.getPassword());
                userVO.setStatus(UserStatus.ACTIVE);
                userVO.setPassword(encodedPassword);
                return new ResponseEntity<>(appUtil.successResponse(userService.saveUser(userVO), AppConstant.USER_RESPONSE_VO,MessageConstant.USER_CREATED_MSG), HttpStatus.CREATED);
            }
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.USER_ALREADY_EXISTS_MSG, userVO.getEmail())), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update User Service", description = "Update User Data", tags = {"Delivery Update User"})
    @PutMapping(value = "/update-user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> updateUser(@PathVariable Long userId, @RequestBody UserVO userVO){
        Optional<UserVO> userVODb = userService.findByUserId(userId);
        if(userVODb.isPresent()){
            String error = userService.validateUserDetails(userVO);
            if(!StringUtils.isBlank(error)){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.INPUT_ERROR, error)), HttpStatus.BAD_REQUEST);
            }else{
                userVO.setPassword(userVODb.get().getPassword());
                return new ResponseEntity<>(appUtil.successResponse(userService.saveUser(userVO), AppConstant.USER_RESPONSE_VO,MessageConstant.USER_UPDATED_MSG), HttpStatus.NO_CONTENT);
            }
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.USER_DATA_NO_FOUND_MSG, userId)), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete User Service", description = "Delete User Data", tags = {"Delivery Delete User"})
    @DeleteMapping(value = "/delete-user-by-id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> deleteUser(@PathVariable Long userId){
        Optional<UserVO> userVODb = userService.findByUserId(userId);
        if(userVODb.isPresent()){
            return new ResponseEntity<>(appUtil.successResponse(userService.deleteUser(userId), AppConstant.USER_RESPONSE_VO, MessageConstant.USER_DELETED_MSG), HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.USER_DATA_NO_FOUND_MSG, userId)), HttpStatus.BAD_REQUEST);
        }
    }
}

