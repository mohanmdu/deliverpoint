package com.delivery.product.services;

import com.delivery.product.enumeration.OrderStatus;
import com.delivery.product.mapper.OrderVO;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderVO> findAllOrder();

    Optional<OrderVO> findByOrderId(Long orderId);

    String validateOrderDetails(OrderVO orderVO);

    Optional<OrderVO> saveOrder(OrderVO orderVO);

    Optional<OrderVO> bookOrderByDelivery(Long orderVO, Long userId);

    Optional<OrderVO> startOrderByDelivery(Long orderId, Long userId);

    Optional<OrderVO> completedOrderByDelivery(Long orderId, Long userId);

    List<OrderVO> findAllActiveOrder(String userId, OrderStatus orderStatus);
}
