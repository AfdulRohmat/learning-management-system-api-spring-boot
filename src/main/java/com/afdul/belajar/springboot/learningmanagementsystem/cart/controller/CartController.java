package com.afdul.belajar.springboot.learningmanagementsystem.cart.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.cart.dto.response.CartResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.dto.response.TotalPriceResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.service.CartService;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> getAllItem(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            Page<CartResponse> response = cartService.getAllItem(pageable);

            return ResponseHandler.generateResponse("Success get data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // DELETE ITEM BY ID
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Object> deleteItemById(@PathVariable Long cartItemId) {
        try {
            cartService.deleteItemById(cartItemId);

            return ResponseHandler.generateResponse("Success delete data", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    // TOTAL PRICE
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/total-price")
    public ResponseEntity<Object> totalPrice() {
        try {
            TotalPriceResponse response = cartService.getTotalPrice();

            return ResponseHandler.generateResponse("Success delete data", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

}
