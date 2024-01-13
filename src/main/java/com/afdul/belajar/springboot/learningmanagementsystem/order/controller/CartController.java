package com.afdul.belajar.springboot.learningmanagementsystem.order.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.ReviewRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.order.dto.response.CartResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.CartItem;
import com.afdul.belajar.springboot.learningmanagementsystem.order.service.CartService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    CartService cartService;

    // ADD ITEM TO CART
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/{courseId}")
    public ResponseEntity<Object> addItemToCart(@PathVariable Long courseId) {
        try {
            cartService.addItemToCart(courseId);

            return ResponseHandler.generateResponse("Success add to cart", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // GET ALL ITEM
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/")
    public ResponseEntity<Object> getAllItem() {
        try {
            List<CartResponse> response = cartService.getAllItem();

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // DELETE ITEM BY ID

    // DELETE ALL ITEM
}
