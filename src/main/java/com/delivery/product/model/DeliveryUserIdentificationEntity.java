package com.delivery.product.model;

import com.delivery.product.enumeration.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DLY_DELIVERY_IDENTIFICATION")
public class DeliveryUserIdentificationEntity {

    @Id
    @Column(name = "DELIVERY_IDENTIFICATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long identificationId;

    @Column(name = "AADHAR_NO")
    private String aadharNo;

    @Column(name = "AADHAR_VERIFIED")
    private String aadharVerified;

    @Lob
    @Column(name = "AADHAR_IMAGE", nullable = false)
    private String aadharImage;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_HOLDER_NAME")
    private String accountHolderName;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "IFSC_CODE")
    private String ifscCode;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE", nullable = false)
    private AccountType accountType;

    @OneToOne(mappedBy = "deliveryUserIdentificationEntity")
    private UserEntity user;

}
