package com.neuedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neuedu.config.NeueduException;
import com.neuedu.entity.Admin;
import com.neuedu.mapper.AdminMapper;
import com.neuedu.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 管理员信息表 服务实现类
 * </p>
 *
 * @author yy
 * @since 2025-08-25
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource(name= "hyxRedisTemplate")
    RedisTemplate<String,Object> redisTemplate;
    @Override
    public String login(String adminname, String password) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", adminname)
                .or().eq("email", adminname);
        Admin admin = this.getOne(wrapper);

        if (admin == null || !password.equals(admin.getPassword())) {
            throw new NeueduException("用户名或密码错误");
        }

        // 只存储用户ID，不存储整个对象
        String redisKey = String.format("admin:login:%d", admin.getId());
        redisTemplate.opsForValue().set(redisKey, admin.getId().toString(), 30, TimeUnit.MINUTES);

        return JwtUtil.createToken(admin);
    }
}
