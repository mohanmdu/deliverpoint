package com.delivery.product.model;

import com.delivery.product.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "DLY_ORDER")
public class OrderEntity extends AuditorEntity implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(name = "ORDER_DESC")
    private String orderDesc;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name = "DELIVER_BOOK_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliverBookDate;

    @Column(name = "ORDER_COMPLETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderCompletedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_STATUS")
    private OrderStatus orderStatus;

    @Column(name = "CANCEL_MSG")
    private String cancelMessage;

    @Column(name = "ORDER_NUMBER")
    private String orderNumber;

    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;

    @Column(name = "CONTACT_PERSON")
    private String contactPerson;

    @ManyToMany
    @JoinTable(
            name = "USER_ORDER_SENDER",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ORDER_ID")
    )
    private Set<UserEntity> senderUserDetails = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "USER_ORDER_RECEIVER",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ORDER_ID")
    )
    private Set<UserEntity> receiverUserDetails = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "USER_ORDER_DELIVERY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ORDER_ID")
    )
    private Set<UserEntity> deliveryUserDetails = new HashSet<>();

    @Column(name = "PICK_UP_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickupStartTime;

    @Column(name = "DELIVERED_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickupEndTime;

    @Column(name = "ORDER_DISTANCE")
    private double orderDistance;

    @Column(name = "DELIVERY_FEES")
    private double deliveryFees;

    @Column(name = "WEIGHT")
    private double weight;

    @Column(name = "COST")
    private double cost;

    @ManyToMany
    @JoinTable(
            name = "ORDER_SHIPPING_ADDRESS",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID")
    )
    private Set<AddressEntity> shippingAddress = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "ORDER_DELIVERY_ADDRESS",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID")
    )
    private Set<AddressEntity> deliveryAddress = new HashSet<>();
}
