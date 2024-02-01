package com.afdul.belajar.springboot.learningmanagementsystem.order.dto.Response;


import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.response.ChargeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusResponse {
    public int id;
    public String orderId;
    public String status;
    public String grossAmount;
    public String paymentType;
}
