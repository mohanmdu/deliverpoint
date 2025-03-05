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
    
    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "ADDRESS_LINE_1")
    private String addressLine1;

    @Column(name = "ADDRESS_LINE_2")
    private String addressLine2;

    @ManyToMany(mappedBy = "shippingAddress", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    private Set<OrderEntity> shippingOrder = new HashSet<>();

    @ManyToMany(mappedBy = "deliveryAddress", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    private Set<OrderEntity> deliveryOrder = new HashSet<>();

    @ManyToMany(mappedBy = "addressList", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    private Set<UserEntity> userAddress = new HashSet<>();

    @Column(name = "BUILDING_NAME")
    private String buildingName;

    @Column(name = "FLOOR")
    private String floor;

    @Column(name = "FLAT_NUMBER")
    private String flatNumber;

    @Column(name = "LAND_MARK")
    private String landMark;
    
    @Column(name = "LATLANG")
    private String latlang;
}
