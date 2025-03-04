package com.delivery.product.services.impl;

import com.delivery.product.exception.CustomeException;
import com.delivery.product.mapper.AddressVO;
import com.delivery.product.mapper.OrderVO;
import com.delivery.product.mapper.UserVO;
import com.delivery.product.model.AddressEntity;
import com.delivery.product.model.OrderEntity;
import com.delivery.product.model.UserEntity;
import com.delivery.product.repository.AddressRepository;
import com.delivery.product.repository.OrderRepository;
import com.delivery.product.services.IOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Transactional
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<OrderVO> findAllOrder() {
        List<OrderVO> orderVOList = new ArrayList<>();
        try{
            orderRepository.findAll().forEach(e -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(e, orderVO);

                if(!e.getSenderUserDetails().isEmpty()){
                    Set<UserVO> senderUserDetails = new HashSet<>();
                    e.getSenderUserDetails().forEach(a -> {
                        UserVO userVO = new UserVO();
                        BeanUtils.copyProperties(a, userVO);
                        senderUserDetails.add(userVO);
                    });
                    orderVO.setSenderUserDetails(senderUserDetails);
                }

                if(!e.getReceiverUserDetails().isEmpty()){
                    Set<UserVO> receiverUserDetails = new HashSet<>();
                    e.getReceiverUserDetails().forEach(b -> {
                        UserVO userVO = new UserVO();
                        BeanUtils.copyProperties(b, userVO);
                        receiverUserDetails.add(userVO);
                    });
                    orderVO.setReceiverUserDetails(receiverUserDetails);
                }

                if(!e.getDeliveryUserDetails().isEmpty()){
                    Set<UserVO> deliveryUserDetails = new HashSet<>();
                    e.getDeliveryUserDetails().forEach(c -> {
                        UserVO userVO = new UserVO();
                        BeanUtils.copyProperties(c, userVO);
                        deliveryUserDetails.add(userVO);
                    });
                    orderVO.setDeliveryUserDetails(deliveryUserDetails);
                }

                if(!e.getShippingAddress().isEmpty()){
                    Set<AddressVO> shippingAddress = new HashSet<>();
                    e.getShippingAddress().forEach(d -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(d, addressVO);
                        shippingAddress.add(addressVO);
                    });
                    orderVO.setShippingAddress(shippingAddress);
                }

                if(!e.getDeliveryAddress().isEmpty()){
                    Set<AddressVO> deliveryAddress = new HashSet<>();
                    e.getDeliveryAddress().forEach(f -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(f, addressVO);
                        deliveryAddress.add(addressVO);
                    });
                    orderVO.setDeliveryAddress(deliveryAddress);
                }

                orderVOList.add(orderVO);
            });
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return orderVOList;
    }

    @Override
    public Optional<OrderVO> findByOrderId(Long orderId) {
        try{
            Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
            if(orderEntity.isPresent()){
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orderEntity.get(), orderVO);

                if(!orderEntity.get().getSenderUserDetails().isEmpty()){
                    Set<UserVO> senderUserDetails = new HashSet<>();
                    orderEntity.get().getSenderUserDetails().forEach(a -> {
                        UserVO userVO = new UserVO();
                        BeanUtils.copyProperties(a, userVO);
                        senderUserDetails.add(userVO);
                    });
                    orderVO.setSenderUserDetails(senderUserDetails);
                }

                if(!orderEntity.get().getReceiverUserDetails().isEmpty()){
                    Set<UserVO> receiverUserDetails = new HashSet<>();
                    orderEntity.get().getReceiverUserDetails().forEach(b -> {
                        UserVO userVO = new UserVO();
                        BeanUtils.copyProperties(b, userVO);
                        receiverUserDetails.add(userVO);
                    });
                    orderVO.setReceiverUserDetails(receiverUserDetails);
                }

                if(!orderEntity.get().getDeliveryUserDetails().isEmpty()){
                    Set<UserVO> deliveryUserDetails = new HashSet<>();
                    orderEntity.get().getDeliveryUserDetails().forEach(c -> {
                        UserVO userVO = new UserVO();
                        BeanUtils.copyProperties(c, userVO);
                        deliveryUserDetails.add(userVO);
                    });
                    orderVO.setDeliveryUserDetails(deliveryUserDetails);
                }

                if(!orderEntity.get().getShippingAddress().isEmpty()){
                    Set<AddressVO> shippingAddress = new HashSet<>();
                    orderEntity.get().getShippingAddress().forEach(d -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(d, addressVO);
                        shippingAddress.add(addressVO);
                    });
                    orderVO.setShippingAddress(shippingAddress);
                }

                if(!orderEntity.get().getDeliveryAddress().isEmpty()){
                    Set<AddressVO> deliveryAddress = new HashSet<>();
                    orderEntity.get().getDeliveryAddress().forEach(f -> {
                        AddressVO addressVO = new AddressVO();
                        BeanUtils.copyProperties(f, addressVO);
                        deliveryAddress.add(addressVO);
                    });
                    orderVO.setDeliveryAddress(deliveryAddress);
                }

                return Optional.of(orderVO);
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return Optional.empty();
    }

    @Override
    public String validateOrderDetails(OrderVO orderVO) {
        return null;
    }

    @Override
    public Optional<OrderVO> saveOrder(OrderVO orderVO) {
        try{
            OrderEntity orderEntity = new OrderEntity();
            BeanUtils.copyProperties(orderVO, orderEntity);

            if(!orderVO.getSenderUserDetails().isEmpty()){
                Set<UserEntity> senderUserDetails = new HashSet<>();
                orderVO.getSenderUserDetails().forEach(e -> {
                    UserEntity userEntity = new UserEntity();
                    BeanUtils.copyProperties(e, userEntity);
                    senderUserDetails.add(userEntity);
                });
                orderEntity.setSenderUserDetails(senderUserDetails);
            }

            if(!orderVO.getReceiverUserDetails().isEmpty()){
                Set<UserEntity> receiverUserDetails = new HashSet<>();
                orderVO.getReceiverUserDetails().forEach(e -> {
                    UserEntity userEntity = new UserEntity();
                    BeanUtils.copyProperties(e, userEntity);
                    receiverUserDetails.add(userEntity);
                });
                orderEntity.setReceiverUserDetails(receiverUserDetails);
            }

            if(!orderVO.getDeliveryUserDetails().isEmpty()){
                Set<UserEntity> deliveryUserDetails = new HashSet<>();
                orderVO.getDeliveryUserDetails().forEach(e -> {
                    UserEntity userEntity = new UserEntity();
                    BeanUtils.copyProperties(e, userEntity);
                    deliveryUserDetails.add(userEntity);
                });
                orderEntity.setDeliveryUserDetails(deliveryUserDetails);
            }

            if(!orderVO.getShippingAddress().isEmpty()){
                Set<AddressEntity> shippingAddress = new HashSet<>();
                orderVO.getShippingAddress().forEach(e -> {
                    AddressEntity addressEntity = new AddressEntity();
                    BeanUtils.copyProperties(e, addressEntity);
                    addressEntity = addressRepository.save(addressEntity);
                    shippingAddress.add(addressEntity);
                });
                orderEntity.setShippingAddress(shippingAddress);
            }

            if(!orderVO.getDeliveryAddress().isEmpty()){
                Set<AddressEntity> deliveryAddress = new HashSet<>();
                orderVO.getDeliveryAddress().forEach(e -> {
                    AddressEntity addressEntity = new AddressEntity();
                    BeanUtils.copyProperties(e, addressEntity);
                    addressEntity = addressRepository.save(addressEntity);
                    deliveryAddress.add(addressEntity);
                });
                orderEntity.setDeliveryAddress(deliveryAddress);
            }

            orderEntity = orderRepository.save(orderEntity);
            orderVO.setOrderId(orderEntity.getOrderId());
            return Optional.of(orderVO);
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    @Override
    public Optional<OrderVO> updateOrder(OrderVO orderVO) {
        try{
            OrderEntity orderEntity = new OrderEntity();
            BeanUtils.copyProperties(orderVO, orderEntity);
            orderEntity = orderRepository.save(orderEntity);
            orderVO.setOrderId(orderEntity.getOrderId());
            return Optional.of(orderVO);
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

}
