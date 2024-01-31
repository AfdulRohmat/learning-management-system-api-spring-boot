package com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeResponse {

    public String status_message;
    public String transaction_id;
    public String fraud_status;
    public String transaction_status;
    public String status_code;
    public String merchant_id;
    public String gross_amount;
    public ArrayList<VaNumber> va_numbers;
    public String payment_type;
    public String transaction_time;
    public String currency;
    public String expiry_time;
    public String order_id;


    public static class VaNumber {
        public String bank;
        public String va_number;
    }


}
