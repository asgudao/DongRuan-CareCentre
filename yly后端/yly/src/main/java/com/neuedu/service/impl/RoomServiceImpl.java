package com.neuedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neuedu.entity.Room;
import com.neuedu.mapper.RoomMapper;
import com.neuedu.service.RoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.vo.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 养老院房间信息表 服务实现类
 * </p>
 *
 * @author yy
 * @since 2025-09-03
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    @Override
    public List<Room> list() {
        QueryWrapper<Room> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        return this.list(wrapper);
    }

    @Override
    public List<Room> getAll() {
        QueryWrapper<Room> wrapper = new QueryWrapper<>();
        return this.list(wrapper);
    }

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public ResultJson<Boolean> updateRoom(Room room) {
        try {
            if (room.getId() == null) {
                return ResultJson.failed("房间ID不能为空");
            }
            Room existing = roomMapper.selectById(room.getId());
            if (existing == null) {
                return ResultJson.failed("房间不存在");
            }
            int result = roomMapper.updateById(room);
            return result > 0 ? ResultJson.success(true, "修改成功") : ResultJson.failed("修改失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.failed("更新失败");
        }
    }

    @Override
    public ResultJson<Boolean> deleteRoom(Long id) {
        try {
            if (id == null || id <= 0) {
                return ResultJson.failed("房间ID不能为空");
            }
            Room existing = roomMapper.selectById(id);
            if (existing == null) {
                return ResultJson.failed("房间不存在");
            }
            int result = roomMapper.deleteById(id);
            return result > 0 ? ResultJson.success(true, "删除成功") : ResultJson.failed("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.failed("删除失败");
        }
    }
}
