package com.delivery.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "DLY_ADDRESS")
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @Id
    @Column(name = "ADDRESS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @Column(name = "STREET")
    private String street;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "ADDRESS_LINE_1")
    private String addressLine1;

    @Column(name = "ADDRESS_LINE_2")
    private String addressLine2;

    @Column(name = "COUNTRY")
    private String country;

    @ManyToMany(mappedBy = "addressList")
    private Set<UserEntity> deliveryUserId = new HashSet<>();

    @ManyToMany(mappedBy = "shippingAddress")
    private Set<OrderEntity> shippingOrder = new HashSet<>();

    @ManyToMany(mappedBy = "deliveryAddress")
    private Set<OrderEntity> deliveryOrder = new HashSet<>();
}
