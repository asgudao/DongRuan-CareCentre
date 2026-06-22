package com.neuedu.service;

import com.neuedu.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理员信息表 服务类
 * </p>
 *
 * @author yy
 * @since 2025-08-25
 */
public interface AdminService extends IService<Admin> {
    String login(String username, String password);
}
