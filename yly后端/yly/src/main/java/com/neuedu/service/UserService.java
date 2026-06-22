package com.neuedu.service;

import com.neuedu.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 监护人登录表 服务类
 * </p>
 *
 * @author hyx
 * @since 2025-08-20
 */
public interface UserService extends IService<User> {
    String login(String username, String password);
    boolean check(String field, String value, Long id);
    boolean regist(User user, String password);
}
