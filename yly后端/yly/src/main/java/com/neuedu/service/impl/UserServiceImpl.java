package com.neuedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neuedu.config.NeueduException;
import com.neuedu.entity.User;
import com.neuedu.mapper.UserMapper;
import com.neuedu.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 监护人登录表 服务实现类
 * </p>
 *
 * @author hyx
 * @since 2025-08-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    PasswordEncoder passwordEncoder;
    @Resource(name= "hyxRedisTemplate")
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public String login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", username)
                .or().eq("email", username);
        User user = this.getOne(wrapper);
        if (null == user ||  !passwordEncoder.matches(password, user.getPassword())) {
            throw new NeueduException("用户或密码错误");
        }
        String key =String.format("ums:%d:login",user.getId());
        redisTemplate.opsForValue().set(key,user,30, TimeUnit.MINUTES);
        return JwtUtil.createToken(user);
    }
    @Override
    public boolean check(String field, String value, Long id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(field, value);
        if (null != id) {
            wrapper.ne("id", id);
        }
        return this.count(wrapper) == 0;
    }

    @Override
    public boolean regist(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return this.save(user);
    }


}
