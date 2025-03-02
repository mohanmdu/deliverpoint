package com.delivery.product.mapper;

import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.enumeration.UserType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private Long userId;
    private String firstName;
    private String userName;
    private String lastName;
    private String email;
    private UserStatus status;
    private String password;
    private String mobileNumber;
    private String gender;
    private UserType userType;
    private String createdBy;
    private Date createdDate;
    private String updatedName;
    private Date updatedDate;

}
