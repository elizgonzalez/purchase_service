package com.docomo.purchase_service.service;

import com.docomo.purchase_service.repository.SubscriptionActivity;
import com.docomo.purchase_service.repository.SubscriptionActivityRepository;
import com.docomo.purchase_service.repository.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private SubscriptionActivityRepository repository;

    @SuppressWarnings("unused")
    public PurchaseServiceImpl(SubscriptionActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public double getStatus(final String msisdin) {
        SubscriptionActivity subscriptionActivity = Optional.ofNullable(repository.findByMsisdn(msisdin))
                .orElseThrow(() -> new MsisdnNotFoundException(msisdin));
        return subscriptionActivity.getStatus();
    }

    @Override
    public void charge(final String msisdin, double amount) {
        repository.addTransactionToSubscriptionAndUpdateStatus(msisdin, Transaction.of(amount));
    }

    @Override
    public void refund(final String msisdin, double amount) {
        charge(msisdin, -amount);
    }
}
