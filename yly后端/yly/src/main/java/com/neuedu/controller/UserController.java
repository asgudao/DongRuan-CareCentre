package com.neuedu.controller;

import com.neuedu.entity.User;
import com.neuedu.service.UserService;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 监护人登录表 前端控制器
 * </p>
 *
 * @author hyx
 * @since 2025-08-20
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @PostMapping("/login")
    ResultJson<String> login(String username, String password) {
        return ResultJson.success(userService.login(username, password));
    }
    @GetMapping("/check")
    ResultJson<Boolean> check(String field, String value, Long id) {
        return ResultJson.success(userService.check(field, value, id));
    }
    @PostMapping("regist")
    ResultJson<Boolean> regist(User user, String password){
        return ResultJson.success(userService.regist(user,password));
    }
}
