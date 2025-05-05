package com.delivery.product.mapper;

import com.delivery.product.enumeration.OrderStatus;
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
    private String orderNumber;
    private String contactNumber;
    private String contactPerson;
    private String orderType;
    private Date orderDate;
    private OrderStatus orderStatus;
    private Set<UserVO> senderUserDetails = new HashSet<>();
    private Set<UserVO> receiverUserDetails = new HashSet<>();
    private Set<UserVO> deliveryUserDetails = new HashSet<>();
	private Date pickupStartTime;
    private Date pickupEndTime;
    private Date deliverBookDate;
    private Date orderCompletedDate;
    private double orderDistance;
    private double deliveryFees;
    private double weight;
    private double cost;
    private Set<AddressVO> shippingAddress = new HashSet<>();
    private Set<AddressVO> deliveryAddress = new HashSet<>();
    private String createdBy;
    private Date createdDate;
    private String updatedName;
    private Date updatedDate;
}
