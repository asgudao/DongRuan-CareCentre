package com.neuedu.service;

import com.neuedu.entity.Nurse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
public interface NurseService extends IService<Nurse> {
    List<Nurse> list();

    Boolean add(Nurse nurse);
}
