package com.afdul.belajar.springboot.learningmanagementsystem.order.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.order.model.Order;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<ArrayList<OrderItem>> findByOrder(Order order);

}
