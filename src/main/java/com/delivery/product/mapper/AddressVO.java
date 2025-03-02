package com.delivery.product.mapper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class AddressVO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    private long addressId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String country;
    private Set<UserVO> deliveryUserId = new HashSet<>();
    private Set<OrderVO> shippingOrder = new HashSet<>();
    private Set<OrderVO> deliveryOrder = new HashSet<>();
}
