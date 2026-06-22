package com.neuedu.service;

import com.neuedu.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neuedu.vo.ResultJson;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 养老院房间信息表 服务类
 * </p>
 *
 * @author yy
 * @since 2025-09-03
 */
@Service
public interface RoomService extends IService<Room> {
    List<Room> list();
    List<Room> getAll();
    ResultJson<Boolean> updateRoom(Room room);
    ResultJson<Boolean> deleteRoom(Long id);
}