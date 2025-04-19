package com.delivery.product.mapper;

import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.enumeration.UserType;
import com.delivery.product.model.DeliveryUserIdentificationEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private Long userId;
    private String name;
    private String userName;
    private String emailId;
    private String deviceId;
    private String deliverType;
    private UserStatus status;
    private String password;
    private String mobileNumber;
    private String gender;
    private String country;
    private String region;
    private UserType userType;
    private String createdBy;
    private Date createdDate;
    private String updatedName;
    private Date updatedDate;
    private Set<AddressVO> addressList = new HashSet<>();
    private String userProfile;
    private DeliveryUserIdentificationVO deliveryUserIdentification;

}
