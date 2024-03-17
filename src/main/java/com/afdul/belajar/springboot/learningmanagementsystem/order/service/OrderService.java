package com.afdul.belajar.springboot.learningmanagementsystem.order.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.repository.CartItemRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.Order;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.OrderItem;
import com.afdul.belajar.springboot.learningmanagementsystem.order.repository.OrderItemRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.order.repository.OrderRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public void createOrderItems(Course course, Order order) throws Exception {

//
//        Create Order Item after Success
//        - get list data item/course yang diorder
//        - get order status
//        - jika order status == complete, maka masukan data order item
//        - data order item
//        id
//        order_id
//        course_id
//        purchased_by

        OrderItem orderItem = new OrderItem();

        orderItem.setCourse(course);
        orderItem.setOrder(order);
        orderItem.setPurchasedBy(order.getPurchasedBy());
        orderItem.setStatus(order.getStatus());

        orderItemRepository.save(orderItem);


    }
}