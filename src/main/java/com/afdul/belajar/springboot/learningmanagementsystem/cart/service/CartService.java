package com.afdul.belajar.springboot.learningmanagementsystem.cart.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.dto.response.TotalPriceResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.CoursePreviewResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.CourseRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.dto.response.CartResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.model.CartItem;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.repository.CartItemRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Page<CartResponse> getAllItem(Pageable pageable) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user, pageable);

        return new PageImpl<>(cartItems.stream()
                .map(this::mapToCartResponse)
                .collect(Collectors.toList()), pageable, cartItems.size());

    }

    private CartResponse mapToCartResponse(CartItem cartItem) {

        UserInfoResponse userInfoResponse = new UserInfoResponse(
                cartItem.getUser().getId(),
                cartItem.getUser().getUsername(),
                cartItem.getUser().getEmail()
        );

        CoursePreviewResponse coursePreviewResponse = new CoursePreviewResponse(
                cartItem.getCourse().getId(),
                cartItem.getCourse().getName(),
                cartItem.getCourse().getPrice(),
                cartItem.getCourse().getThumbnail(),
                cartItem.getCourse().getRatings(),
                cartItem.getCourse().getPurchased(),
                userInfoResponse
        );

        return CartResponse.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .course(coursePreviewResponse)
                .build();
    }


    // DELETE ITEM BY ID
    @Transactional
    public void deleteItemById(Long cartId) {
        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Item not found"));
        cartItemRepository.deleteById(cartItem.getId());
    }

    // TOTAL PRICE
    @Transactional
    public TotalPriceResponse getTotalPrice() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Double totalPrice = cartItemRepository.getTotalPrice(user);

        return TotalPriceResponse.builder()
                .totalPrice(totalPrice != null ? totalPrice : Double.valueOf(0.00))
                .build();
    }
}
