package com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
public class MidtransDataMockup {
    private List<String> listedPayment;
    private Map<String, String> creditCard;
    private String paymentType = "";
    private String selectBankTransfer = "";

    public void enablePayments(List<String> listPayment) {
        listedPayment = new ArrayList<>();
        listedPayment.addAll(listPayment);
    }

    public void creditCard(Map<String, String> params) {
        creditCard = new HashMap<>();
        creditCard.putAll(params);
    }

    public Map<String, Object> initDataMock() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Map<String, String> transDetail = new HashMap<>();
        transDetail.put("order_id", "MID_JAVA_DEMO_" + timestamp.getTime());
        transDetail.put("gross_amount", "265000");

        Map<String, String> bankTransfer = new HashMap<>();
        bankTransfer.put("bank", getSelectBankTransfer());

        List<Map<String, String>> items = new ArrayList<>();

        Map<String, String> item1 = new HashMap<>();
        item1.put("id", "ID001");
        item1.put("price", "15000");
        item1.put("quantity", "1");
        item1.put("name", "Sendal Karet Rumahan");
        item1.put("brand", "Suwaslow");
        item1.put("category", "Sanitasi");
        item1.put("merchant_name", "SnowlID");

        Map<String, String> item2 = new HashMap<>();
        item2.put("id", "ID002");
        item2.put("price", "200000");
        item2.put("quantity", "1");
        item2.put("name", "Mantel Hujan");
        item2.put("brand", "Excel");
        item2.put("category", "Sanitasi");
        item2.put("merchant_name", "SnowlID");

        Map<String, String> item3 = new HashMap<>();
        item3.put("id", "ID003");
        item3.put("price", "50000");
        item3.put("quantity", "1");
        item3.put("name", "Sarung Tangan Karet");
        item3.put("brand", "Cap Anti Sobek");
        item3.put("category", "Sanitasi");
        item3.put("merchant_name", "SnowlID");

        items.add(item1);
        items.add(item2);
        items.add(item3);

        Map<String, Object> billingAddres = new HashMap<>();
        billingAddres.put("first_name", "Sita");
        billingAddres.put("last_name", "Nuria");
        billingAddres.put("email", "SitaElekTapiAkuCinta@gmail.com");
        billingAddres.put("phone", "0928282828");
        billingAddres.put("address", "Jalan Iskandarsyah II");
        billingAddres.put("city", "Jakarta Selatan");
        billingAddres.put("postal_code", "10120");
        billingAddres.put("country_code", "IDN");

        Map<String, Object> custDetail = new HashMap<>();
        custDetail.put("first_name", "Sita");
        custDetail.put("last_name", "Nuria");
        custDetail.put("email", "SitaElekTapiAkuCinta@gmail.com");
        custDetail.put("phone", "62783737373");
        custDetail.put("billing_address", billingAddres);

        Map<String, Object> body = new HashMap<>();

        if (creditCard != null) {
            body.put("credit_card", creditCard);
        }

        if (bankTransfer != null) {
            body.put("bank_transfer", bankTransfer);
        }

        body.put("transaction_details", transDetail);
        body.put("item_details", items);
        body.put("customer_details", custDetail);

        if (!getPaymentType().isEmpty()) {
            body.put("payment_type", getPaymentType());
        }

        if (listedPayment != null) {
            body.put("enabled_payments", listedPayment);
        }

        return body;
    }


}
