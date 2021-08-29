package com.docomo.purchase_service.controller;

import com.docomo.purchase_service.service.MsisdnNotFoundException;
import com.docomo.purchase_service.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PurchaseService service;

    @Test
    public void shouldReturnStatusWhenSubscriptionExists() throws Exception {
        String msisdn = "dummy";
        double status = 123.0;
        when(service.getStatus(any())).thenReturn(status);

        mockMvc.perform(get("/status").param("msisdn", msisdn))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(Double.toString(status))));

        ArgumentCaptor<String> msisdinCaptor = ArgumentCaptor.forClass(String.class);
        verify(service).getStatus(msisdinCaptor.capture());
        assertThat(msisdinCaptor.getValue()).isEqualTo(msisdinCaptor.getValue());
    }

    @Test
    public void shouldReturnNotFoundWhenSubscriptionDoesNotExist() throws Exception {
        String msisdn = "dummy";
        doThrow(MsisdnNotFoundException.class).when(service).getStatus(any());

        mockMvc.perform(get("/status").param("msisdn", msisdn))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnEmptyPayloadWhenChargingCustomer() throws Exception {
        String msisdn = "12345";
        double amount = 50;

        PurchaseControllerDTO body = new PurchaseControllerDTO(msisdn, amount);

        mockMvc.perform(post("/charge")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<String> msisdnCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);
        verify(service).charge(msisdnCaptor.capture(), amountCaptor.capture());
        assertThat(msisdnCaptor.getValue()).isEqualTo(msisdn);
        assertThat(amountCaptor.getValue()).isEqualTo(amount);
    }

    @Test
    public void shouldReturnEmptyPayloadWhenRefundingCustomer() throws Exception {
        String msisdn = "67891";
        double amount = 50;

        PurchaseControllerDTO body = new PurchaseControllerDTO(msisdn, amount);

        mockMvc.perform(post("/refund")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<String> msisdnCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> amountCaptor = ArgumentCaptor.forClass(Double.class);
        verify(service).refund(msisdnCaptor.capture(), amountCaptor.capture());
        assertThat(msisdnCaptor.getValue()).isEqualTo(msisdn);
        assertThat(amountCaptor.getValue()).isEqualTo(amount);
    }

    @Test
    public void shouldReturnBadRequestWhenAmountIsNegative() throws Exception {
        String msisdn = "47582";
        double amount = -50;

        PurchaseControllerDTO body = new PurchaseControllerDTO(msisdn, amount);

        mockMvc.perform(post("/refund")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenMsisdnIsBlank() throws Exception {
        String msisdn = "";
        double amount = 50;

        PurchaseControllerDTO body = new PurchaseControllerDTO(msisdn, amount);

        mockMvc.perform(post("/refund")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
