package com.delivery.product.repository;

import com.delivery.product.enumeration.OrderStatus;
import com.delivery.product.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByOrderStatusAndCreatedBy(OrderStatus orderStatus, String userName);
}
