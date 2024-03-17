package com.afdul.belajar.springboot.learningmanagementsystem.order.controller;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.model.CartItem;
import com.afdul.belajar.springboot.learningmanagementsystem.cart.repository.CartItemRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.order.dto.Response.OrderStatusResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.Order;
import com.afdul.belajar.springboot.learningmanagementsystem.order.repository.OrderRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.order.service.OrderService;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.request.BCAVirtualAccountRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.request.CheckOrderStatusRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.response.ChargeResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.service.CoreAPIService;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoreAPIService coreAPIService;

    @Autowired
    private OrderService orderService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/payment/bca-va", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> chargeBCAVA(
            @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable
    ) throws MidtransError {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Double totalPrice = cartItemRepository.getTotalPrice(user).orElseThrow(() -> new RuntimeException("Item not found"));
        List<CartItem> cartItems = cartItemRepository.findByUser(user, pageable);
        String orderId = "ORDER_" + timestamp.getTime();

        // Get Data transaction_details
        BCAVirtualAccountRequest.TransactionDetails transactionDetails = BCAVirtualAccountRequest.TransactionDetails.builder()
                .order_id(orderId)
                .gross_amount(totalPrice.toString())
                .build();

        // Get data customer_details (from user input soon)
        BCAVirtualAccountRequest.CustomerDetails customerDetails = BCAVirtualAccountRequest.CustomerDetails.builder()
                .email(user.getEmail())
                .first_name(user.getUsername())
                .last_name("")
                .phone("0895106834275")
                .build();

        // Get item detail dari cart
        ArrayList<BCAVirtualAccountRequest.ItemDetail> itemDetails = new ArrayList<BCAVirtualAccountRequest.ItemDetail>();

        // looping all course inside cart, add to itemDetails array
        cartItems.forEach(cartItem -> {
            BCAVirtualAccountRequest.ItemDetail itemDetail = BCAVirtualAccountRequest.ItemDetail.builder()
                    .id(cartItem.getCourse().getId().toString())
                    .price(cartItem.getCourse().getPrice().toString())
                    .quantity(cartItem.getQuantity().toString())
                    .name(cartItem.getCourse().getName())
                    .build();

            itemDetails.add(itemDetail);


        });

        // Get data BankTransfer
        BCAVirtualAccountRequest.BankTransfer bankTransfer = BCAVirtualAccountRequest.BankTransfer.builder()
                .bank("bca")
                .va_number("111111")
                .build();

        BCAVirtualAccountRequest requestBody = BCAVirtualAccountRequest.builder()
                .payment_type("bank_transfer")
                .transaction_details(transactionDetails)
                .customer_details(customerDetails)
                .item_details(itemDetails)
                .bank_transfer(bankTransfer)
                .build();

        // Send requestBody to service
        ChargeResponse chargeResponse = coreAPIService.bcaVirtualAccount(requestBody);


        return ResponseHandler.generateResponse("Order successfully created ", HttpStatus.OK, chargeResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/check-status",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> checkOrderStatus(@Valid CheckOrderStatusRequest request) {
        try {
            OrderStatusResponse response = coreAPIService.checkOrder(request);

            return ResponseHandler.generateResponse("Order status updated", HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
