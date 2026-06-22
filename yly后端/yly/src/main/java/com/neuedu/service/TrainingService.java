package com.neuedu.service;

import com.neuedu.entity.Training;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 养老院康复训练记录表 服务类
 * </p>
 *
 * @author yy
 * @since 2025-08-27
 */
public interface TrainingService extends IService<Training> {
    List<Training> list();
}
