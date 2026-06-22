package com.neuedu.controller;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.neuedu.entity.Elder;
import com.neuedu.service.ElderService;
import com.neuedu.service.PayService;
import com.neuedu.util.PayContent;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 养老院老人信息表 前端控制器
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
@RestController
@RequestMapping("/elder")
public class ElderController {

    @Resource
    ElderService elderService;
    @Resource
    PayService payService;
    @PostMapping("/add")
    ResultJson<String> add(Elder elder, Long open_id) throws AlipayApiException, JsonProcessingException {
        return ResultJson.success(elderService.add(elder,open_id),"预约成功");
    }
    @GetMapping("/list")
    ResultJson<List<Elder>> list(Long open_id, String checkInTime, Integer active){
        return ResultJson.success(elderService.list(open_id,checkInTime,active));
    }
    @PostMapping("/pay")
    ResultJson<String> pay(PayContent payContent) throws AlipayApiException, JsonProcessingException {
        payContent.setSubject("护理费");
        payContent.setProduct_code("FAST_INSTANT_TRADE_PAY");
        return ResultJson.success(payService.pay(payContent));
    }

    @PostMapping("/updatePay")
    ResultJson<Boolean> updatePay(HttpServletRequest request) throws AlipayApiException {
        return ResultJson.success(elderService.update(request));
    }
    @PostMapping("/del")
    ResultJson<Boolean> del(@RequestParam(value = "out_trade_no", required = false) String outTradeNo){
        if (outTradeNo == null || outTradeNo.isEmpty()) {
            return ResultJson.failed("单号不能为空");
        }
        return ResultJson.success(elderService.delByOutTradeNo(outTradeNo));
    }

}
