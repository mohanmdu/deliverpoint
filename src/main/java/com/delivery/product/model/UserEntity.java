package com.delivery.product.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.enumeration.UserType;
import com.delivery.product.mapper.AddressVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DLY_USER")
public class UserEntity extends AuditorEntity implements Serializable {

	private static final long serialVersionUID = 1234567L;

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "DEVICE_ID")
	private String deviceId;

	@Column(name = "REGION")
	private String region;

	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "DELIVERY_ITEM")
	private String deliveryItem;
	
	@Column(name = "NUM_DELIVERY")
	private String numDelivery;
	
	@Column(name = "NATURE_DELIVERY")
	private String natureDelivery;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private UserStatus status;

	@Column(name = "PASSWORD")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "USER_TYPE")
	private UserType userType;

	@ManyToMany(mappedBy = "senderUserDetails")
	private Set<OrderEntity> senderOrders = new HashSet<>();

	@ManyToMany(mappedBy = "receiverUserDetails")
	private Set<OrderEntity> receiverOrders = new HashSet<>();

	@ManyToMany(mappedBy = "deliveryUserDetails")
	private Set<OrderEntity> deliveryOrders = new HashSet<>();

	@ManyToMany
	@JoinTable(
			name = "USER_ADDRESS",
			joinColumns = @JoinColumn(name = "USER_ID"),
			inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID")
	)
	private Set<AddressEntity> addressList = new HashSet<>();

}
