package com.neuedu.service.impl;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.neuedu.config.NeueduException;
import com.neuedu.entity.Elder;
import com.neuedu.mapper.ElderMapper;
import com.neuedu.service.ElderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.service.PayService;
import com.neuedu.util.PayContent;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 养老院老人信息表 服务实现类
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
@Service

public class ElderServiceImpl extends ServiceImpl<ElderMapper, Elder> implements ElderService {
    @Resource
    PayService payService;
    @Override
    public String add(Elder elder, Long openId) throws AlipayApiException, JsonProcessingException {
        elder.setuserId(openId);
        Random random =new Random(openId);
        String outTradeNo=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))+ String.valueOf(random.nextInt(1000000,9999999));
        elder.setoutTradeNo(outTradeNo);
        if(this.save(elder)) {
            PayContent payContent = new PayContent(
                    outTradeNo,
                    String.valueOf(String.valueOf(elder.getPrice())),
                    "入住费",
                    "入住费",
                    "FAST_INSTANT_TRADE_PAY"
            );
            return payService.pay(payContent);
        }
        throw new NeueduException("缴费失败");
    }

    @PostMapping("/pay")
    ResultJson<String> pay(PayContent payContent) throws AlipayApiException, JsonProcessingException {
        payContent.setSubject("ruzhu费");
        payContent.setProduct_code("FAST_INSTANT_TRADE_PAY");
        return ResultJson.success(payService.pay(payContent));
    }

    @Override
    public List<Elder> list(Long open_id, String checkInTime, Integer active) {
        QueryWrapper<Elder> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",open_id);
        if(null!=active){
            wrapper.eq("active",active);
        }
        return this.list(wrapper);
    }

    @Override
    public List<Elder> listAll(Long open_id, String checkInTime, Integer active) {
        QueryWrapper<Elder> wrapper=new QueryWrapper<>();
        wrapper.ne("user_id",-1L);
        if(null!=active){
            wrapper.eq("active",active);
        }
        // 获取所有老人信息，不做任何过滤
        return this.list(wrapper);
    }

    @Override
    public Boolean update(HttpServletRequest request) throws AlipayApiException {
        if(payService.check(request)){
            String outTradeNo=request.getParameter("out_trade_no");
            Elder elder=new Elder();
            elder.setActive(1);
            UpdateWrapper<Elder> wrapper=new UpdateWrapper<>();
            wrapper.eq("out_trade_no",outTradeNo);
            return this.update(elder,wrapper);
        }else{
            throw new NeueduException("支付失败");
        }
    }

    @Override
    public boolean delByOutTradeNo(String outTradeNo) {
        // 如果outTradeNo为null或空字符串，说明参数不合法，返回false表示删除失败
        if (outTradeNo == null || outTradeNo.isEmpty()) {
            return false;
        }
        QueryWrapper<Elder> wrapper = new QueryWrapper<>();
        wrapper.eq("out_trade_no", outTradeNo);
        Elder elder = new Elder();
        elder.setuserId(-1L);  // 将active字段设置为-1表示已删除
        return this.update(elder, wrapper);
    }

    @Override
    public String addByAdmin(Elder elder) throws AlipayApiException, JsonProcessingException {
        // 管理员添加老人信息时，userId设置为null
        elder.setuserId(null);
        Random random = new Random(System.currentTimeMillis());
        String outTradeNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) +
                String.valueOf(random.nextInt(1000000, 9999999));
        elder.setoutTradeNo(outTradeNo);
        if(this.save(elder)) {
            PayContent payContent = new PayContent(
                    outTradeNo,
                    String.valueOf(elder.getPrice()),
                    "入住费",
                    "入住费",
                    "FAST_INSTANT_TRADE_PAY"
            );
            return payService.adminPay(payContent);
        }
        throw new NeueduException("缴费失败");
    }

    @Override
    public String adminPay(String outTradeNo) throws AlipayApiException, JsonProcessingException {
        // 根据订单号查询老人信息
        QueryWrapper<Elder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_trade_no", outTradeNo);
        Elder elder = this.getOne(queryWrapper);

        if (elder == null) {
            throw new NeueduException("未找到对应的订单信息，订单号: " + outTradeNo);
        }

        PayContent payContent = new PayContent(
                outTradeNo,
                String.valueOf(elder.getPrice()),
                "入住费",
                "入住费",
                "FAST_INSTANT_TRADE_PAY"
        );

        return payService.adminPay(payContent);
    }

    @Resource
    private ElderMapper elderMapper;  // ✅ 注入 Mapper
    public Integer getRoomNumbers(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return 0;
        }
        QueryWrapper<Elder> wrapper = new QueryWrapper<>();
        wrapper.eq("room_number", roomNumber);
        wrapper.ne("user_id",0);
        return Math.toIntExact(elderMapper.selectCount(wrapper)); // 直接查数据库
    }
    public Integer getNurseNumbers(Integer nurseId) {
        if (nurseId == 0) {
            return 0;
        }
        QueryWrapper<Elder> wrapper = new QueryWrapper<>();
        wrapper.eq("nurse_id", nurseId);
        wrapper.ne("user_id",0);
        return Math.toIntExact(elderMapper.selectCount(wrapper)); // 直接查数据库
    }
}
