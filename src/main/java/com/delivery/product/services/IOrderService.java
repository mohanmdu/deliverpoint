package com.delivery.product.services;

import com.delivery.product.mapper.OrderVO;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<OrderVO> findAllOrder();

    Optional<OrderVO> findByOrderId(Long orderId);

    String validateOrderDetails(OrderVO orderVO);

    Optional<OrderVO> saveOrder(OrderVO orderVO);

    Optional<OrderVO> updateOrder(OrderVO orderVO);
}
