package com.delivery.product.mapper;

import com.delivery.product.enumeration.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryUserIdentificationVO {

    private long identificationId;
    private String aadharNo;
    private String aadharVerified;
    private String aadharImage;
    private String accountNumber;
    private String accountHolderName;
    private String bankName;
    private String ifscCode;
    private String branchName;
    private AccountType accountType;
    private UserVO user;
}
