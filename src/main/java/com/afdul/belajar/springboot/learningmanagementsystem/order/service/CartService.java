package com.afdul.belajar.springboot.learningmanagementsystem.order.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.CourseRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.order.dto.request.CartItemRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.order.dto.response.CartResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.CartItem;
import com.afdul.belajar.springboot.learningmanagementsystem.order.repository.CartItemRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    // ADD ITEM TO CART
    @Transactional
    public void addItemToCart(Long courseId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if course with particular userId and courseId is already exist or not
        long courseAlreadyAdded = cartItemRepository.countByCourseAndUser(course, user);
        if (courseAlreadyAdded > 0) {
            throw new RuntimeException("Course already on cart");
        }

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setCourse(course);
        cartItem.setQuantity(1);

        cartItemRepository.save(cartItem);
    }

    // GET ALL ITEM
    @Transactional
    public List<CartResponse> getAllItem() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Item not found"));;
//        return cartItemRepository.findByUser(user);
        return cartItems.stream()
                .map(cartItem -> CartResponse.builder()
                        .id(cartItem.getId())
                        .course(cartItem.getCourse())
                        .quantity(cartItem.getQuantity())
                        .build()).collect(Collectors.toList());

    }


    // DELETE ITEM BY ID

    // DELETE ALL ITEM
}
