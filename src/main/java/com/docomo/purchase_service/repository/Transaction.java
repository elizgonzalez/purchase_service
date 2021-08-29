package com.docomo.purchase_service.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class Transaction {
    private Instant time = Instant.now();
    @Field("amt")
    private double amount;

    public static Transaction of(double amount) {
        return new Transaction(null, amount);
    }
}
