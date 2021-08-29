package com.docomo.purchase_service.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Document("subscription")
public class SubscriptionActivity {

    private String id;
    @Indexed
    private String msisdn;
    private Instant createdOn;
    private Transaction[] trans;
    private double status;

    static SubscriptionActivity of(String msisdn, double status) {
        return new SubscriptionActivity(null, msisdn, null, null, status);
    }
}
