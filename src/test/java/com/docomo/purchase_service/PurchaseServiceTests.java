package com.docomo.purchase_service;

import com.docomo.purchase_service.controller.PurchaseControllerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PurchaseServiceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void shouldChargeCustomer() throws Exception {
        String msisdn = UUID.randomUUID().toString();
        double amount = 50;
        HttpEntity<PurchaseControllerDTO> entity
                = new HttpEntity<>(new PurchaseControllerDTO(msisdn, amount));
        this.restTemplate.postForObject(getHost() + "/charge", entity, Void.class);

        PurchaseControllerDTO response = restTemplate.getForObject(getHost() + "/status?msisdn={msisdn}",
                PurchaseControllerDTO.class, msisdn);
        assertThat(response.getAmount()).isEqualTo(amount);
    }

    @Test
    public void shouldRefundCustomer() throws Exception {
        String msisdn = UUID.randomUUID().toString();
        double amount = 50;
        HttpEntity<PurchaseControllerDTO> entity
                = new HttpEntity<>(new PurchaseControllerDTO(msisdn, amount));
        this.restTemplate.postForObject(getHost() + "/refund", entity, Void.class);

        PurchaseControllerDTO response = restTemplate.getForObject(getHost() + "/status?msisdn={msisdn}",
                PurchaseControllerDTO.class, msisdn);
        assertThat(response.getAmount()).isEqualTo(-amount);
    }

    private String getHost() {
        return "http://localhost:" + port;
    }
}
