package com.neuedu.controller;

import com.neuedu.entity.Activity;
import com.neuedu.service.ActivityService;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 养老院活动表（存储活动基本信息） 前端控制器
 * </p>
 *
 * @author yy
 * @since 2025-08-29
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    ActivityService activityService;
    @PostMapping("/add")
    public ResultJson<Boolean> addTraining(Activity activity) {
        return activityService.save(activity) ? ResultJson.success(true, "添加成功") : ResultJson.failed("添加失败");
    }

    @GetMapping("/list")
    ResultJson<List<Activity>> getlist() {
        return ResultJson.success(activityService.list());
    }

    @GetMapping("/listByDateRange")
    ResultJson<List<Activity>> listByDateRange(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {

        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Activity> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

        // 如果提供了开始日期，则筛选结束日期大于等于开始日期的记录
        if (startDate != null) {
            queryWrapper.ge("end_date", startDate);
        }

        // 如果提供了结束日期，则筛选开始日期小于等于结束日期的记录
        if (endDate != null) {
            queryWrapper.le("start_date", endDate);
        }

        List<Activity> activities = activityService.list(queryWrapper);
        return ResultJson.success(activities);
    }

}
