package com.neuedu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neuedu.entity.Training;
import com.neuedu.service.TrainingService;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 养老院康复训练记录表 前端控制器
 * </p>
 *
 * @author yy
 * @since 2025-08-27
 */
@RestController
@RequestMapping("/training")
public class TrainingController {
    @Resource
    TrainingService trainingService;

    @GetMapping("/list")
    ResultJson<List<Training>> getlist() {
        return ResultJson.success(trainingService.list());
    }

    @GetMapping("/listByElderId")
    public ResultJson<List<Training>> getlistByElderId(@RequestParam Integer elderId) {
        // 查询数据库，获取与指定elderId相关的培训列表
        List<Training> list = trainingService.list(
                new QueryWrapper<Training>()
                        .eq("elder_id", elderId)
        );

        // 返回成功的结果，包含培训列表
        return ResultJson.success(list);
    }

    @PostMapping("/add")
    public ResultJson<Boolean> addTraining(Training training) {
        return trainingService.save(training) ? ResultJson.success(true, "添加成功") : ResultJson.failed("添加失败");
    }

    @GetMapping("/listByDateRange")
    ResultJson<List<Training>> listByDateRange(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {

        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Training> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

        // 如果提供了开始日期，则筛选结束日期大于等于开始日期的记录
        if (startDate != null) {
            queryWrapper.ge("end_date", startDate);
        }

        // 如果提供了结束日期，则筛选开始日期小于等于结束日期的记录
        if (endDate != null) {
            queryWrapper.le("start_date", endDate);
        }

        List<Training> trainings = trainingService.list(queryWrapper);
        return ResultJson.success(trainings);
    }

}
