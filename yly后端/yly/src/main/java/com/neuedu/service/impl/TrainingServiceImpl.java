package com.neuedu.service.impl;

import com.neuedu.entity.Training;
import com.neuedu.mapper.TrainingMapper;
import com.neuedu.service.TrainingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 养老院康复训练记录表 服务实现类
 * </p>
 *
 * @author yy
 * @since 2025-08-27
 */
@Service
public class TrainingServiceImpl extends ServiceImpl<TrainingMapper, Training> implements TrainingService {
    @Override
    public List<Training> list() {
        return super.list();
    }
}
