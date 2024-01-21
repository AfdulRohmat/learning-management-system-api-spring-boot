package com.afdul.belajar.springboot.learningmanagementsystem.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalCartResponse {
    @JsonProperty("total_cart")
    private Integer totalCart;
}
