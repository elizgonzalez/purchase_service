package com.docomo.purchase_service.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface PurchaseService {

    double getStatus(@NotNull String msisdin);

    void charge(@NotNull String msisdin, double amount);

    void refund(@NotNull String msisdin, double amount);
}
