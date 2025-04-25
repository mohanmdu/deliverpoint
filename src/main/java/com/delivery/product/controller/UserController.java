package com.delivery.product.controller;

import com.delivery.product.constant.AppConstant;
import com.delivery.product.constant.MessageConstant;
import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.mapper.ChangePassword;
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
        userVO.setUserName(userVO.getMobileNumber());
        Optional<UserVO> userVOContactDb = userService.findByUserContact(userVO.getMobileNumber());
        if(userVOContactDb.isEmpty()){
            userVO.setUserName(userVO.getMobileNumber());
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
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.USER_ALREADY_EXISTS_MSG, userVO.getMobileNumber())), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update User Service", description = "Update User Data", tags = {"Delivery Update User"})
    @PutMapping(value = "/update-user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> updateUser(@PathVariable Long userId, @RequestBody UserVO userVO){
        Optional<UserVO> userVODb = userService.findByUserId(userId);
        if(userVODb.isPresent()){
            Optional<UserVO> isExistsUser = userService.findByEmailMobileUserId(userId, userVO.getMobileNumber(), userVO.getEmailId());
            if(isExistsUser.isPresent()){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.INPUT_ERROR, MessageConstant.USER_ALREADY_EXISTS_MSG_1)), HttpStatus.BAD_REQUEST);
            }
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

    @Operation(summary = "Update User Service", description = "Update User Data", tags = {"Delivery Update User"})
    @PutMapping(value = "/update-user-primary-info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> updateUserPrimaryData(@PathVariable Long userId, @RequestBody UserVO userVO){
        Optional<UserVO> userVODb = userService.findByUserId(userId);
        if(userVODb.isPresent()){
            Optional<UserVO> isExistsUser = userService.findByEmailMobileUserId(userId, userVO.getMobileNumber(), userVO.getEmailId());
            if(isExistsUser.isPresent()){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.INPUT_ERROR, MessageConstant.USER_ALREADY_EXISTS_MSG_1)), HttpStatus.BAD_REQUEST);
            }
            if(!StringUtils.isBlank(userVO.getEmailId())){
                userVODb.get().setEmailId(userVO.getEmailId());
            }
            if(!StringUtils.isBlank(userVO.getMobileNumber())){
                userVODb.get().setMobileNumber(userVO.getMobileNumber());
            }
            if(!StringUtils.isBlank(userVO.getName())){
                userVODb.get().setName(userVO.getName());
            }
            return new ResponseEntity<>(appUtil.successResponse(userService.saveUser(userVO), AppConstant.USER_RESPONSE_VO,MessageConstant.USER_UPDATED_MSG), HttpStatus.NO_CONTENT);
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

    @Operation(summary = "Login User Service", description = "Login User Data", tags = {"Delivery Login User"})
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> login(@RequestBody UserVO userVO){
        Optional<UserVO> userVOContactDb = userService.findByUserContact(userVO.getMobileNumber());
        if(userVOContactDb.isPresent()){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!encoder.matches(userVO.getPassword(), userVOContactDb.get().getPassword())){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.PASSWORD_NOT_MATCH, userVO.getMobileNumber())), HttpStatus.BAD_REQUEST);
            }
            if(!userVO.getDeviceId().equalsIgnoreCase("WEB")){
                userVOContactDb.get().setDeviceId(userVO.getDeviceId());
                return new ResponseEntity<>(appUtil.successResponse(userService.saveUserDeviceId(userVOContactDb.get()), AppConstant.USER_RESPONSE_VO,MessageConstant.LOGIN_SUCCESS), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(appUtil.successResponse(userVOContactDb, AppConstant.USER_RESPONSE_VO,MessageConstant.LOGIN_SUCCESS), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.LOGIN_FAILED, userVO.getMobileNumber())), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Logout User Service", description = "Logout User Data", tags = {"Delivery Logout User"})
    @GetMapping(value = "/logout/{userName}/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> getByUserId(@PathVariable String userName, @PathVariable String deviceId){
        Optional<UserVO> userVOContactDb = userService.findByUserContact(userName);
        if(!deviceId.equalsIgnoreCase("WEB")){
            userVOContactDb.get().setDeviceId(null);
            return new ResponseEntity<>(appUtil.successResponse(userService.saveUserDeviceId(userVOContactDb.get()), AppConstant.USER_RESPONSE_VO,MessageConstant.LOGOUT_SUCCESS), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(appUtil.successResponse(userVOContactDb, AppConstant.USER_RESPONSE_VO,MessageConstant.LOGOUT_SUCCESS), HttpStatus.CREATED);
    }

    @Operation(summary = "Change Password User Service", description = "Change Password User Data", tags = {"Change User Password"})
    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO> changePassword(@RequestBody ChangePassword changePassword){
        Optional<UserVO> userVOContactDb = userService.findByUserId(changePassword.getUserId());
        if(userVOContactDb.isPresent()){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!encoder.matches(changePassword.getOldPassword(), userVOContactDb.get().getPassword())){
                return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.PASSWORD_NOT_MATCH, changePassword.getUserId())), HttpStatus.BAD_REQUEST);
            }
            String encodedPassword = encoder.encode(changePassword.getNewPassword());
            userVOContactDb.get().setPassword(encodedPassword);
            return new ResponseEntity<>(appUtil.successResponse(userService.saveUserDeviceId(userVOContactDb.get()), AppConstant.USER_RESPONSE_VO,MessageConstant.LOGIN_SUCCESS), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(appUtil.failedResponse(MessageConstant.INPUT_ERROR,String.format(MessageConstant.LOGIN_FAILED, changePassword.getUserId())), HttpStatus.BAD_REQUEST);
        }
    }

}

