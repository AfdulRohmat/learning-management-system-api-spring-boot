package com.afdul.belajar.springboot.learningmanagementsystem.order.repository;

import com.afdul.belajar.springboot.learningmanagementsystem.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderIdAndTransactionId(String orderId, String transactionId);


}
