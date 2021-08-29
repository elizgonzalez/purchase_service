package com.docomo.purchase_service.controller;

import com.docomo.purchase_service.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PurchaseController {

    private final PurchaseService service;

    @SuppressWarnings("unused")
    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    @GetMapping(value = "status", produces = MediaType.APPLICATION_JSON_VALUE)
    public PurchaseControllerDTO getStatus(@RequestParam("msisdn") String msisdn) {
        double status = service.getStatus(msisdn);
        return new PurchaseControllerDTO(msisdn, status);
    }

    @PostMapping(value = "charge", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void charge(@Validated @RequestBody PurchaseControllerDTO body) {
        service.charge(body.getMsisdn(), body.getAmount());
    }

    @PostMapping(value = "refund", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void refund(@Validated @RequestBody PurchaseControllerDTO body) {
        service.refund(body.getMsisdn(), body.getAmount());
    }
}
