package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 养老院活动表（存储活动基本信息）
 * </p>
 *
 * @author yy
 * @since 2025-08-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID（自增主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 开始日期（格式：YYYY-MM-DD）
     */
    private LocalDate startDate;

    /**
     * 结束日期（格式：YYYY-MM-DD）
     */
    private LocalDate endDate;

    /**
     * 组织方
     */
    private String organizer;

    /**
     * 活动详情（长文本）
     */
    private String detail;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer isDeleted;
}