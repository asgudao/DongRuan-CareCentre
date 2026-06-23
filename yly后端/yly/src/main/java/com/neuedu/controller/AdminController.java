package com.neuedu.controller;

import com.neuedu.entity.Admin;
import com.neuedu.entity.Elder;
import com.neuedu.service.AdminService;
import com.neuedu.service.ElderService;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 管理员信息表 前端控制器
 * </p>
 *
 * @author yy
 * @since 2025-08-25
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;
    @Resource
    ElderService elderService;
    @PostMapping("/login")
    ResultJson<String> login(String username, String password) {
        return ResultJson.success(adminService.login(username, password));
    }
    @GetMapping("/list")
    ResultJson<List<Elder>> getAllElders(Long open_id, String checkInTime, Integer active) {
        List<Elder> elders = elderService.listAll(open_id, checkInTime, active);
        return ResultJson.success(elders);
    }
    @PostMapping("/pay")
    public ResultJson<String> adminPay(@RequestParam("out_trade_no") String outTradeNo,
                                       @RequestParam(value = "total_amount", required = false) String totalAmount) {
        try {
            String result = elderService.adminPay(outTradeNo);
            return ResultJson.success(result);
        } catch (Exception e) {
            return ResultJson.failed(e.getMessage());
        }
    }

}
