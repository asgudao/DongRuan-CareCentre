package com.neuedu.controller;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.neuedu.entity.Nurse;
import com.neuedu.service.NurseService;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
@RestController
@RequestMapping("/nurse")
public class NurseController {
    @Resource
    NurseService nurseService;

    @GetMapping("/getNurse")
    ResultJson<List<Nurse>> getNurse() {
        return ResultJson.success(nurseService.list());
    }

    @PostMapping("/add")
    ResultJson<Boolean> add(Nurse nurse) throws AlipayApiException, JsonProcessingException {
        return ResultJson.success(nurseService.add(nurse), "添加成功");
    }
}
