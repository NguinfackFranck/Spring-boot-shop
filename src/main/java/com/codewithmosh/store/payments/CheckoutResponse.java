package com.codewithmosh.store.payments;

import lombok.Data;

@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkOutUrl;

    public CheckoutResponse(Long orderId, String CheckOutUrl) {
        this.orderId = orderId;
        this.checkOutUrl = CheckOutUrl;

    }
}
