package com.neuedu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {
    @PostMapping("/success")
    String paySuccess(){
        System.out.println("返回到后端的支付成功");
        return "11111";
    }
}
