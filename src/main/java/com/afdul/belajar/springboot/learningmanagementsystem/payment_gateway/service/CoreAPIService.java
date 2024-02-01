package com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.service;

import com.afdul.belajar.springboot.learningmanagementsystem.cart.repository.CartItemRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.order.dto.Response.OrderStatusResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.EStatus;
import com.afdul.belajar.springboot.learningmanagementsystem.order.model.Order;
import com.afdul.belajar.springboot.learningmanagementsystem.order.repository.OrderRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.request.BCAVirtualAccountRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.request.BRIVirtualAccountRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.request.CheckOrderStatusRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.dto.response.ChargeResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.util.MidtransDataMockup;
import com.afdul.belajar.springboot.learningmanagementsystem.utils.ResponseHandler;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.config.MidtransConfig.sandboxClientKey;
import static com.afdul.belajar.springboot.learningmanagementsystem.payment_gateway.config.MidtransConfig.sandboxServerKey;

@Service
public class CoreAPIService {
    @Autowired
    MidtransDataMockup dataMockup = new MidtransDataMockup();
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    MidtransCoreApi coreApi = new ConfigFactory(new Config(sandboxServerKey, sandboxClientKey, false)).getCoreApi();

    @Transactional
    public ChargeResponse bcaVirtualAccount(BCAVirtualAccountRequest request) throws MidtransError {

//        dataMockup.setPaymentType("bank_transfer");
//        dataMockup.setSelectBankTransfer("bca");
//
//        Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());

        ObjectMapper mapObject = new ObjectMapper();
        Map<String, Object> requestBody = mapObject.convertValue(request, Map.class);

        JSONObject object = coreApi.chargeTransaction(requestBody);

        String result = object.toString();

        ChargeResponse chargeResponse = JSON.parseObject(result, ChargeResponse.class);

        // Save order info to db
        Order order = new Order();
        order.setOrderId(chargeResponse.getOrder_id());
        order.setTransactionId(chargeResponse.getTransaction_id());
        order.setStatus(EStatus.PENDING);
        order.setGrossAmount(chargeResponse.getGross_amount());
        order.setPaymentType(chargeResponse.getPayment_type());

        orderRepository.save(order);

        // Empty Cart
        cartItemRepository.deleteAll();

        return chargeResponse;
    }

    @Transactional
    public ChargeResponse briVirtualAccount(BRIVirtualAccountRequest request) throws MidtransError {
        ObjectMapper mapObject = new ObjectMapper();
        Map<String, Object> requestBody = mapObject.convertValue(request, Map.class);

        JSONObject object = coreApi.chargeTransaction(requestBody);

        String result = object.toString();

        ChargeResponse chargeResponse = JSON.parseObject(result, ChargeResponse.class);

        // Save order info to db
        Order order = new Order();
        order.setOrderId(chargeResponse.getOrder_id());
        order.setTransactionId(chargeResponse.getTransaction_id());
        order.setStatus(EStatus.PENDING);
        order.setGrossAmount(chargeResponse.getGross_amount());
        order.setPaymentType(chargeResponse.getPayment_type());

        orderRepository.save(order);

        return chargeResponse;
    }

    @Transactional
    public OrderStatusResponse checkOrder(CheckOrderStatusRequest request) throws Exception {
        // Get data from request and Compare order_id and transaction_id from Order DB with data from request
        Order order = orderRepository.findByOrderIdAndTransactionId(
                        request.getOrderId(),
                        request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // if matching, update order status
        switch (request.getStatusCode()) {
            case "200":
                // code block
                order.setStatus(EStatus.SUCCESS);
                break;
            case "201":
                // code block
                order.setStatus(EStatus.PENDING);
                break;
            case "202":
                // code block
                order.setStatus(EStatus.CANCEL);
                break;
            default:
                // code block
                throw new RuntimeException("Order failed to update");
        }

        // save order status
        orderRepository.save(order);

        return OrderStatusResponse.builder()
                .id(Math.toIntExact(order.getId()))
                .orderId(order.getOrderId())
                .status(order.getStatus().toString())
                .grossAmount(order.getGrossAmount())
                .paymentType(order.getPaymentType())
                .build();
    }

}
