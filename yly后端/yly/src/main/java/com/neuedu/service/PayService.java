package com.neuedu.service;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.neuedu.util.PayContent;
import jakarta.servlet.http.HttpServletRequest;

public interface PayService {
    String pay(PayContent payContent) throws JsonProcessingException, AlipayApiException;
    boolean check(HttpServletRequest request) throws AlipayApiException;
    String adminPay(PayContent payContent) throws JsonProcessingException, AlipayApiException;
}
