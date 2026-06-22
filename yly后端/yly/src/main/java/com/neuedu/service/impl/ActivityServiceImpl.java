package com.neuedu.service.impl;

import com.neuedu.entity.Activity;
import com.neuedu.mapper.ActivityMapper;
import com.neuedu.service.ActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 养老院活动表（存储活动基本信息） 服务实现类
 * </p>
 *
 * @author yy
 * @since 2025-08-29
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Override
    public List<Activity> list() {
        return super.list();
    }
}
