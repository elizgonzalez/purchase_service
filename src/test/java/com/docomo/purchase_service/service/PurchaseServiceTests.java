package com.docomo.purchase_service.service;

import com.docomo.purchase_service.repository.SubscriptionActivity;
import com.docomo.purchase_service.repository.SubscriptionActivityRepository;
import com.docomo.purchase_service.repository.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTests {

    @Mock
    private SubscriptionActivityRepository repository;

    private PurchaseServiceImpl sut;

    @BeforeEach
    public void setUp() {
        sut = new PurchaseServiceImpl(repository);
    }

    @Test
    public void shouldThrowExceptionWhenMsisdinNotFound() {
        String msisdin = "msisdin";
        when(repository.findByMsisdn(any())).thenReturn(null);

        assertThrows(MsisdnNotFoundException.class, () -> {
            sut.getStatus(msisdin);
        });

        ArgumentCaptor<String> msisdinCaptor = ArgumentCaptor.forClass(String.class);
        verify(repository).findByMsisdn(msisdinCaptor.capture());
        assertThat(msisdinCaptor.getValue()).isEqualTo(msisdin);
    }

    @Test
    public void shouldReturnStatusWhenMsisdinIsFound() {
        String msisdin = "msisdin";
        double status = 999;
        when(repository.findByMsisdn(any())).thenReturn(new SubscriptionActivity(null, msisdin, Instant.now(), null, status));

        assertThat(sut.getStatus(msisdin)).isEqualTo(status);

        ArgumentCaptor<String> msisdinCaptor = ArgumentCaptor.forClass(String.class);
        verify(repository).findByMsisdn(msisdinCaptor.capture());
        assertThat(msisdinCaptor.getValue()).isEqualTo(msisdin);
    }

    @Test
    public void shouldAddChargeToSubscription() {
        String msisdin = "msisdin";
        double charge = 999;

        sut.charge(msisdin, charge);

        ArgumentCaptor<String> msisdinCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(repository).addTransactionToSubscriptionAndUpdateStatus(msisdinCaptor.capture(), transactionCaptor.capture());
        assertThat(msisdinCaptor.getValue()).isEqualTo(msisdin);
        assertThat(transactionCaptor.getValue().getAmount()).isEqualTo(charge);
    }

    @Test
    public void shouldSubtractRefundFromSubscription() {
        String msisdin = "msisdin";
        double refund = 999;

        sut.refund(msisdin, refund);

        ArgumentCaptor<String> msisdinCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(repository).addTransactionToSubscriptionAndUpdateStatus(msisdinCaptor.capture(), transactionCaptor.capture());
        assertThat(msisdinCaptor.getValue()).isEqualTo(msisdin);
        assertThat(transactionCaptor.getValue().getAmount()).isEqualTo(-refund);
    }
}
