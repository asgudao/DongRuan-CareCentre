package com.neuedu.service;

import com.neuedu.entity.Activity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 养老院活动表（存储活动基本信息） 服务类
 * </p>
 *
 * @author yy
 * @since 2025-08-29
 */
public interface ActivityService extends IService<Activity> {
    List<Activity> list();
}
