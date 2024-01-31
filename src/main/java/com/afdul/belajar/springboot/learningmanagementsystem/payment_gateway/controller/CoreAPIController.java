package com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.controller;


import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.response.ChargeResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.util.MidtransDataMockup;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import com.alibaba.fastjson.JSON;
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.config.MidtransConfig.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/payment")
public class CoreAPIController {

    @Autowired
    MidtransDataMockup dataMockup = new MidtransDataMockup();


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> testPayment() throws MidtransError {

        dataMockup.setPaymentType("bank_transfer");
        dataMockup.setSelectBankTransfer("bca");

        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());

        MidtransCoreApi coreApi = new ConfigFactory(new Config(sandboxServerKey, sandboxClientKey, false)).getCoreApi();

        JSONObject object = coreApi.chargeTransaction(body);

        String result = object.toString();

        ChargeResponse chargeResponse = JSON.parseObject(result, ChargeResponse.class);

        return ResponseHandler.generateResponse("Success create course content", HttpStatus.OK, chargeResponse);

    }

}
