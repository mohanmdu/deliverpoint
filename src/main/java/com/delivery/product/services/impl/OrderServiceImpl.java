package com.delivery.product.services.impl;

import com.delivery.product.exception.CustomeException;
import com.delivery.product.mapper.OrderVO;
import com.delivery.product.model.OrderEntity;
import com.delivery.product.repository.OrderRepository;
import com.delivery.product.services.IOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderVO> findAllOrder() {
        List<OrderVO> orderVOList = new ArrayList<>();
        try{
            orderRepository.findAll().forEach(e -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(e, orderVO);
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
