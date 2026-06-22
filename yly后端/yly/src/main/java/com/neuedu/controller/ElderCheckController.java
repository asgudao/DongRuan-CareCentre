package com.neuedu.controller;

import com.neuedu.entity.Elder;
import com.neuedu.entity.ElderCheck;
import com.neuedu.service.ElderCheckService;
import com.neuedu.service.ElderService;
import com.neuedu.vo.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yy
 * @since 2025-08-27
 */
@RestController
@RequestMapping("/elderCheck")
public class ElderCheckController {

    @Autowired
    private ElderCheckService elderCheckService;

    @Autowired
    private ElderService elderService;

    /**
     * 获取所有老人体检记录列表
     *
     * @return 体检记录列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultJson<List<ElderCheck>> listElderChecks() {
        List<ElderCheck> elderChecks = elderCheckService.list();
        return ResultJson.success(elderChecks, "获取体检记录列表成功");
    }

    /**
     * 根据日期筛选老人体检记录列表
     *
     * @param checkDate 体检日期
     * @return 体检记录列表
     */
    @RequestMapping(value = "/listByDate", method = RequestMethod.GET)
    public ResultJson<List<ElderCheck>> listElderChecksByDate(LocalDate checkDate) {
        if (checkDate == null) {
            return ResultJson.failed("日期参数不能为空");
        }

        // 使用MyBatis Plus QueryWrapper进行条件查询
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ElderCheck> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("check_date", checkDate);

        List<ElderCheck> elderChecks = elderCheckService.list(queryWrapper);
        return ResultJson.success(elderChecks, "获取体检记录列表成功");
    }

    /**
     * 添加老人体检记录
     *
     * @param elderCheck 老人体检信息
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultJson<String> addElderCheck(ElderCheck elderCheck) {
        System.out.println(elderCheck);
        if (elderCheck.getElderId() != null && (elderCheck.getElderName() == null || elderCheck.getElderName().isEmpty())) {
            Elder elder = elderService.getById(elderCheck.getElderId());
            if (elder != null) {
                elderCheck.setElderName(elder.getName());
            }
        }

        boolean isSaved = elderCheckService.save(elderCheck);
        if (isSaved) {
            return ResultJson.success("添加体检记录成功");
        } else {
            return ResultJson.failed("添加体检记录失败");
        }
    }

}
