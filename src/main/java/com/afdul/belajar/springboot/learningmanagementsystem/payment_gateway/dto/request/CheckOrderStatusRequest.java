package com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckOrderStatusRequest {
    private String orderId;
    private String transactionId;
    private String statusCode;

}
