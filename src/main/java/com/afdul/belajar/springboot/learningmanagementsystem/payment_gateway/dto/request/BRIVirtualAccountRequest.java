package com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BRIVirtualAccountRequest {
    public String payment_type;
    public TransactionDetails transaction_details;
    public CustomerDetails customer_details;
    public ArrayList<ItemDetail> item_details;
    public BankTransfer bank_transfer;


    public static class BankTransfer {
        public String bank;
        public String va_number;
    }

    public static class CustomerDetails {
        public String email;
        public String first_name;
        public String last_name;
        public String phone;
    }

    public static class ItemDetail {
        public String id;
        public int price;
        public int quantity;
        public String name;
    }


    public static class TransactionDetails {
        public int gross_amount;
        public String order_id;
    }


}
