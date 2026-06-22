package com.neuedu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neuedu.entity.Nurse;
import com.neuedu.entity.Room;
import com.neuedu.entity.Training;
import com.neuedu.service.ElderService;
import com.neuedu.service.NurseService;
import com.neuedu.service.RoomService;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 养老院房间信息表 前端控制器
 * </p>
 *
 * @author yy
 * @since 2025-09-03
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Resource
    RoomService roomService;
    @Resource
    ElderService elderService;
    @GetMapping("/getRoom")
    ResultJson<List<Room>> getRoom() {
        return ResultJson.success(roomService.list());
    }
    @GetMapping("/getAllRoom")
    ResultJson<List<Room>> getAllRoom() {
        return ResultJson.success(roomService.getAll());
    }
    @PostMapping("/addRoom")
    public ResultJson<Boolean> addRoom(Room room) {
        return roomService.save(room) ? ResultJson.success(true, "添加成功") : ResultJson.failed("添加失败");
    }
    @PostMapping("/update")
    public ResultJson<Boolean> updateRoom(Room room) {
        return roomService.updateRoom(room);
    }
    @PostMapping("/delete")
    public ResultJson<Boolean> deleteRoom(@RequestParam Long id) {
        return roomService.deleteRoom(id);
    }

    @PostMapping("/updateRoomStatus")
    public ResultJson updateRoomStatus(@RequestParam String roomNumber) {
        try {
            // 参数校验
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                return ResultJson.failed("房间号不能为空");
            }
            // 查询房间
            QueryWrapper<Room> wrapper = new QueryWrapper<>();
            wrapper.eq("room_number", roomNumber);
            Room room = roomService.getOne(wrapper);
            if (room == null) {
                return ResultJson.failed("房间不存在：" + roomNumber);
            }
            // 查询当前入住人数
            Integer currentCount = elderService.getRoomNumbers(roomNumber);
            // 根据容量设置状态
            if (currentCount >= room.getCapacity()) {
                room.setStatus((byte) 1); // 已满
            } else {
                room.setStatus((byte) 0); // 可入住
            }
            // 更新数据库
            boolean updateSuccess = roomService.updateById(room);
            if (updateSuccess) {
                return ResultJson.success("房间状态更新成功"); // ✅ 返回成功 JSON
            } else {
                return ResultJson.failed("房间状态更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.failed("服务器内部错误");
        }
    }
}
