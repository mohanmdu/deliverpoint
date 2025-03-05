package com.delivery.product.services.impl;

import com.delivery.product.enumeration.OrderStatus;
import com.delivery.product.enumeration.UserType;
import com.delivery.product.exception.CustomeException;
import com.delivery.product.mapper.AddressVO;
import com.delivery.product.mapper.OrderVO;
import com.delivery.product.mapper.UserVO;
import com.delivery.product.model.AddressEntity;
import com.delivery.product.model.OrderEntity;
import com.delivery.product.model.UserEntity;
import com.delivery.product.repository.AddressRepository;
import com.delivery.product.repository.OrderRepository;
import com.delivery.product.repository.UserRepository;
import com.delivery.product.services.IOrderService;
import com.delivery.product.util.AppUtil;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Transactional
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AppUtil appUtil;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, AddressRepository addressRepository, UserRepository userRepository, AppUtil appUtil) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.appUtil = appUtil;
    }

    @Override
    public List<OrderVO> findAllOrder() {
        List<OrderVO> orderVOList = new ArrayList<>();
        try{
            orderRepository.findAll().forEach(e -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(e, orderVO);
                setOrderChildInfo(orderVO, e);
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

                setOrderChildInfo(orderVO, orderEntity.get());

                return Optional.of(orderVO);
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return Optional.empty();
    }

    @Override
    public String validateOrderDetails(OrderVO orderVO) {
        AtomicReference<String> error = new AtomicReference<>("");
        try{
            if(StringUtils.isBlank(orderVO.getOrderDesc())){
                error.set(error + " Order Description");
            }
            if(orderVO.getSenderUserDetails().isEmpty()){
                error.set(error + " Sender User Information");
            } else {
                orderVO.getSenderUserDetails().forEach(e -> {
                    if(userRepository.findById(e.getUserId()).isEmpty()){
                        error.set(error + " Wrong Sender User Information");
                    }
                });
            }
            if(orderVO.getReceiverUserDetails().isEmpty()){
                error.set(error + " Receiver User Information");
            } else {
                orderVO.getReceiverUserDetails().forEach(e -> {
                    if(userRepository.findById(e.getUserId()).isEmpty()){
                        error.set(error + " Wrong Receiver User Information");
                    }
                });
            }
            if(orderVO.getWeight() == 0){
                error.set(error + " Order Weight");
            }
            if(orderVO.getCost() == 0){
                error.set(error + " Order Cost");
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return error.get();
    }

    @Override
    public Optional<OrderVO> saveOrder(OrderVO orderVO) {
        try{
            OrderEntity orderEntity = new OrderEntity();
            BeanUtils.copyProperties(orderVO, orderEntity);

            orderEntity.setOrderDate(new Date());

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

            orderEntity.setDeliveryFees(appUtil.calculateDeliveryFees(orderVO.getOrderDistance(),orderVO.getWeight(), orderVO.getCost()));

            orderEntity = orderRepository.save(orderEntity);
            orderVO.setOrderId(orderEntity.getOrderId());
            return Optional.of(orderVO);
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    @Override
    public Optional<OrderVO> bookOrderByDelivery(Long orderId, Long userId) {
        try{
            Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);
            if(orderEntityOptional.isPresent()){
                Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
                if(userEntityOptional.isPresent()){
                    OrderEntity orderEntity = orderEntityOptional.get();
                    Set<UserEntity> deliverUserDetails = new HashSet<>();
                    deliverUserDetails.add(userEntityOptional.get());
                    orderEntity.setDeliveryUserDetails(deliverUserDetails);

                    orderEntity.setOrderStatus(OrderStatus.IN_TRANSIT);
                    orderEntity.setDeliverBookDate(new Date());
                    orderRepository.save(orderEntity);
                    return findByOrderId(orderId);
                }
            }
            return Optional.empty();
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    @Override
    public Optional<OrderVO> startOrderByDelivery(Long orderId, Long userId) {
        try{
            Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);
            if(orderEntityOptional.isPresent()){
                OrderEntity orderEntity = orderEntityOptional.get();
                orderEntity.setOrderStatus(OrderStatus.SHIPPED);
                orderEntity.setPickupStartTime(new Date());

                // speed in kilometers per hour
                double speedInKmPerHour = 50.0;
                // Calculate time taken in hours
                double timeInHours = orderEntity.getOrderDistance() / speedInKmPerHour;
                long minutes = (long) (timeInHours * 60);
                long seconds = (long) ((timeInHours * 3600) % 60);
                LocalTime startTime = LocalTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
                LocalTime endTime = startTime.plusMinutes(minutes).plusSeconds(seconds);

                LocalDateTime localDateTime = LocalDateTime.now();
                localDateTime = localDateTime.withHour(endTime.getHour()).withMinute(endTime.getMinute());
                orderEntity.setPickupEndTime(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));

                orderRepository.save(orderEntity);
                return findByOrderId(orderId);
            }
            return Optional.empty();
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    @Override
    public Optional<OrderVO> completedOrderByDelivery(Long orderId, Long userId) {
        try{
            Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);
            if(orderEntityOptional.isPresent()){
                OrderEntity orderEntity = orderEntityOptional.get();
                orderEntity.setOrderStatus(OrderStatus.DELIVERED);
                orderEntity.setOrderCompletedDate(new Date());
                orderRepository.save(orderEntity);
                return findByOrderId(orderId);
            }
            return Optional.empty();
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    private void setOrderChildInfo(OrderVO orderVO, OrderEntity e){
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
    }

    @Override
    public List<OrderVO> findAllActiveOrder(String userName, OrderStatus orderStatus) {
        List<OrderVO> orderVOList = new ArrayList<>();
        List<OrderEntity> orderEntityList = new ArrayList<>();
        try{
            orderEntityList = orderRepository.findByOrderStatusAndCreatedBy(orderStatus, userName);
            orderEntityList.forEach(e -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(e, orderVO);

                setOrderChildInfo(orderVO, e);

                orderVOList.add(orderVO);
            });
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return orderVOList;
    }

    @Override
    public Optional<OrderVO> cancelOrderByDelivery(Long orderId, Long userId, String cancelMessage) {
        try{
            Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);
            if(orderEntityOptional.isPresent()){
                OrderEntity orderEntity = orderEntityOptional.get();
                orderEntity.setOrderStatus(OrderStatus.CANCELED);
                orderEntity.setCancelMessage(cancelMessage);
                orderRepository.save(orderEntity);
                return findByOrderId(orderId);
            }
            return Optional.empty();
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
    }

    @Override
    public List<AddressVO> getAllAddressByUser(String userName, UserType userType) {
        List<AddressVO> addressVOList = new ArrayList<>();
        try{
            List<OrderEntity> orderEntityList = orderRepository.findByCreatedBy(userName);
            if(!orderEntityList.isEmpty()){
                orderEntityList.forEach(e -> {
                    if(userType.name().equalsIgnoreCase(UserType.CUSTOMER.name())){
                        if(!e.getDeliveryAddress().isEmpty()){
                            e.getDeliveryAddress().forEach(a -> {
                                AddressVO addressVO = new AddressVO();
                                BeanUtils.copyProperties(a, addressVO);
                                addressVOList.add(addressVO);
                            });
                        }
                    } else if(userType.name().equalsIgnoreCase(UserType.DELIVERY.name())){
                        if(!e.getShippingAddress().isEmpty()){
                            e.getShippingAddress().forEach(a -> {
                                AddressVO addressVO = new AddressVO();
                                BeanUtils.copyProperties(a, addressVO);
                                addressVOList.add(addressVO);
                            });
                        }
                    }
                });
            }
        }catch (Exception e){
            throw new CustomeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),e.getStackTrace());
        }
        return addressVOList;
    }

}
