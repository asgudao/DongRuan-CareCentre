package com.neuedu.service;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.neuedu.entity.Elder;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 养老院老人信息表 服务类
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
public interface ElderService extends IService<Elder> {
    String add(Elder elder,Long openId) throws AlipayApiException, JsonProcessingException;
    List<Elder> list(Long open_id, String checkInTime, Integer active);
    Boolean update(HttpServletRequest request) throws AlipayApiException;
    List<Elder> listAll(Long open_id, String checkInTime, Integer active);
    boolean delByOutTradeNo(String outTradeNo);
    String adminPay(String outTradeNo) throws AlipayApiException, JsonProcessingException;
    String addByAdmin(Elder elder) throws AlipayApiException, JsonProcessingException;
    Integer getRoomNumbers(String roomNumber);
    Integer getNurseNumbers(Integer nurseId);
}
