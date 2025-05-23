package com.delivery.product.services.impl;

import com.delivery.product.constant.AppConstant;
import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.enumeration.UserType;
import com.delivery.product.exception.CustomeException;
import com.delivery.product.mapper.AddressVO;
import com.delivery.product.mapper.UserVO;
import com.delivery.product.model.AddressEntity;
import com.delivery.product.model.UserEntity;
import com.delivery.product.repository.AddressRepository;
import com.delivery.product.repository.UserRepository;
import com.delivery.product.services.IUserService;
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
    private final AddressRepository addressRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<UserVO> findAllUser() {
        List<UserVO> userVOList = new ArrayList<>();
        try{
            userRepository.findAll().forEach(e -> {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(e, userVO);
                if(e.getDeliveryUserIdentification() != null){
                    BeanUtils.copyProperties(e.getDeliveryUserIdentification(), userVO.getDeliveryUserIdentification());
                }

                if(!e.getAddressList().isEmpty()){
                    Set<AddressVO> addressVOS = new HashSet<>();
                    e.getAddressList().forEach(a -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(a, addressVO);
                        userVO.setPassword(null);
                        addressVOS.add(addressVO);
                    });
                    userVO.setAddressList(addressVOS);
                }


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
                if(userEntity.get().getDeliveryUserIdentification() != null){
                    BeanUtils.copyProperties(userEntity.get().getDeliveryUserIdentification(), userVO.getDeliveryUserIdentification());
                }
                //userVO.setPassword(null);
                if(!userEntity.get().getAddressList().isEmpty()){
                    Set<AddressVO> addressVOS = new HashSet<>();
                    userEntity.get().getAddressList().forEach(a -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(a, addressVO);
                        addressVOS.add(addressVO);
                    });
                    userVO.setAddressList(addressVOS);
                }

                return Optional.of(userVO);
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserVO> findByUserContact(String mobileNumber) {
        try{
            Optional<UserEntity> userEntity = userRepository.findByMobileNumber(mobileNumber, UserStatus.ACTIVE);
            if(userEntity.isPresent()){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userEntity.get(), userVO);
                if(userEntity.get().getDeliveryUserIdentification() != null){
                    BeanUtils.copyProperties(userEntity.get().getDeliveryUserIdentification(), userVO.getDeliveryUserIdentification());
                }
                if(!userEntity.get().getAddressList().isEmpty()){
                    Set<AddressVO> addressVOS = new HashSet<>();
                    userEntity.get().getAddressList().forEach(a -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(a, addressVO);
                        addressVOS.add(addressVO);
                    });
                    userVO.setAddressList(addressVOS);
                }
                return Optional.of(userVO);
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserVO> findByEmailMobileUserId(Long userId, String mobileNumber, String emailId) {
        try{
            Optional<UserEntity> userEntity = userRepository.findByEmailMobileUserId(userId, mobileNumber, emailId);
            if(userEntity.isPresent()){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userEntity.get(), userVO);
                if(userEntity.get().getDeliveryUserIdentification() != null){
                    BeanUtils.copyProperties(userEntity.get().getDeliveryUserIdentification(), userVO.getDeliveryUserIdentification());
                }
                userVO.setPassword(null);
                if(!userEntity.get().getAddressList().isEmpty()){
                    Set<AddressVO> addressVOS = new HashSet<>();
                    userEntity.get().getAddressList().forEach(a -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(a, addressVO);
                        addressVOS.add(addressVO);
                    });
                    userVO.setAddressList(addressVOS);
                }
                return Optional.of(userVO);
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return Optional.empty();
    }

    @Override
    public String validateUserDetails(UserVO userVO) {
        String error = "";
        try{

            if(StringUtils.isBlank(userVO.getName())){
                error = error + " User Full Name";
            }
            if(StringUtils.isBlank(userVO.getUserName())){
                error = error + " User Name";
            }
            if(StringUtils.isBlank(userVO.getPassword())){
                error = error + " Password";
            }
            if(StringUtils.isBlank(userVO.getMobileNumber())){
                error = error + " Mobile Number";
            }
            if(StringUtils.isBlank(userVO.getCountry())){
                error = error + " Country";
            }
            if(StringUtils.isBlank(userVO.getRegion())){
                error = error + " Region";
            }
            if(userVO.getUserType() == null || StringUtils.isBlank(userVO.getUserType().name())){
                error = error + " User Type";
            } else {
                if(userVO.getUserType().name().equalsIgnoreCase(UserType.DELIVERY.name())){
                    if(userVO.getAddressList().isEmpty()){
                        error = error + " Personal Address";
                    }
                    if(StringUtils.isBlank(userVO.getDeliveryUserIdentification().getAadharNo())){
                        error = error + " Aadhar Number";
                    }
                    if(StringUtils.isBlank(userVO.getDeliveryUserIdentification().getAccountNumber())){
                        error = error + " Account Number";
                    }
                    if(StringUtils.isBlank(userVO.getDeliveryUserIdentification().getAccountHolderName())){
                        error = error + " Account Holder Number";
                    }
                    if(StringUtils.isBlank(userVO.getDeliveryUserIdentification().getBankName())){
                        error = error + " Bank Number";
                    }
                    if(StringUtils.isBlank(userVO.getDeliveryUserIdentification().getBranchName())){
                        error = error + " Branch Number";
                    }
                    if(StringUtils.isBlank(userVO.getDeliveryUserIdentification().getIfscCode())){
                        error = error + " IFSC Code";
                    }
                    if(userVO.getDeliveryUserIdentification().getAccountType() == null || StringUtils.isBlank(userVO.getDeliveryUserIdentification().getAccountType().name())){
                        error = error + " Account Type";
                    }
                }
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

            if(!userVO.getAddressList().isEmpty()){
                Set<AddressEntity> addressEntities = new HashSet<>();
                userVO.getAddressList().forEach(e -> {
                    AddressEntity addressEntity = new AddressEntity();
                    BeanUtils.copyProperties(e, addressEntity);
                    addressEntity = addressRepository.save(addressEntity);
                    addressEntities.add(addressEntity);
                });
                userEntity.setAddressList(addressEntities);
            }

            userEntity = userRepository.save(userEntity);
            userVO.setUserId(userEntity.getUserId());
            return Optional.of(userVO);
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    @Override
    public Optional<UserVO> saveUserDeviceId(UserVO userVO) {
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
