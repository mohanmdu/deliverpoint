package com.delivery.product.mapper;

import com.delivery.product.enumeration.OrderStatus;
import com.delivery.product.model.AddressEntity;
import com.delivery.product.model.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class OrderVO {

    private long orderId;
    private String orderDesc;
    private String orderType;
    private Date orderDate;
    private OrderStatus orderStatus;
    private Set<UserVO> senderUserDetails = new HashSet<>();
    private Set<UserVO> receiverUserDetails = new HashSet<>();
    private Set<UserVO> deliveryUserDetails = new HashSet<>();
    private Date pickStartTime;
    private Date pickEndTime;
    private float orderDistance;
    private float deliveryFees;
    private float weight;
    private float cost;
    private Set<AddressVO> shippingAddress = new HashSet<>();
    private Set<AddressVO> deliveryAddress = new HashSet<>();
    private String createdBy;
    private Date createdDate;
    private String updatedName;
    private Date updatedDate;
}
