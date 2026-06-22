package com.neuedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neuedu.entity.Nurse;
import com.neuedu.mapper.NurseMapper;
import com.neuedu.service.NurseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
@Service
public class NurseServiceImpl extends ServiceImpl<NurseMapper, Nurse> implements NurseService {

    @Override
    public List<Nurse> list() {
        QueryWrapper<Nurse> wrapper = new QueryWrapper<>();
        wrapper.eq("work_status", 1);
        return this.list(wrapper);
    }
    @Override
    public Boolean add(Nurse nurse) {
        nurse.setWorkStatus((byte) 1);
        return this.save(nurse);
    }
}
