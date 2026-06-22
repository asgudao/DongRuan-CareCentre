package com.neuedu.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuedu.config.PayConfig;
import com.neuedu.service.PayService;
import com.neuedu.util.PayContent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.neuedu.config.PayConfig.ALIPAY_PUBLIC_KEY;

@Service
public class PayServiceImpl implements PayService {
    @Resource
    ObjectMapper objectMapper;
    @Override
    public String pay(PayContent payContent) throws JsonProcessingException, AlipayApiException {
        AlipayClient client = new DefaultAlipayClient(
                PayConfig.GETWAY,
                PayConfig.APPID,
                PayConfig.PRIVATE_KEY,
                PayConfig.FORMAT,
                PayConfig.CHARSET,
                ALIPAY_PUBLIC_KEY,
                PayConfig.SIGN_TYPE
        );
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl("http://127.0.0.1/success");
        request.setNotifyUrl("http://ff63ebac.natappfree.cc/success");
        request.setBizContent(objectMapper.writeValueAsString(payContent));
        return client.pageExecute(request).getBody();
    }

    @Override
    public boolean check(HttpServletRequest request) throws AlipayApiException {
        Map<String,String> params =new HashMap<String,String>();
        Map<String,String[]> requestParams =request.getParameterMap();
        for(Map.Entry<String,String[]> entry:requestParams.entrySet()){
            String name =entry.getKey();
            if ("open_id".equals(name)) {
                continue;
            }
            if ("user_id".equals(name)) {
                continue;
            }
            if ("role".equals(name)) {
                continue;
            }
            System.out.println( name);
            String[] values= entry.getValue();
            String str =String.join(",",values);
            params.put(name,str);
        }
        for (Map.Entry<String,String> entry:params.entrySet()){
            System.out.println(entry.getKey()+"--"+entry.getValue());
        }
        return AlipaySignature.rsaCheckV1(params,PayConfig.ALIPAY_PUBLIC_KEY,PayConfig.CHARSET,PayConfig.SIGN_TYPE);
    }

    @Override
    public String adminPay(PayContent payContent) throws JsonProcessingException, AlipayApiException {
        AlipayClient client = new DefaultAlipayClient(
                PayConfig.GETWAY,
                PayConfig.APPID,
                PayConfig.PRIVATE_KEY,
                PayConfig.FORMAT,
                PayConfig.CHARSET,
                PayConfig.ALIPAY_PUBLIC_KEY,
                PayConfig.SIGN_TYPE
        );
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl("http://127.0.0.1/adminsuccess");  // 使用管理员专用的返回URL
        request.setNotifyUrl("http://ff63ebac.natappfree.cc/adminsuccess");
        request.setBizContent(objectMapper.writeValueAsString(payContent));
        return client.pageExecute(request).getBody();
    }
}
