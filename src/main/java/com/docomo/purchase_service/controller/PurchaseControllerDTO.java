package com.docomo.purchase_service.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
public class PurchaseControllerDTO {
    @NotBlank
    private String msisdn;
    @Positive
    private double amount;
}
