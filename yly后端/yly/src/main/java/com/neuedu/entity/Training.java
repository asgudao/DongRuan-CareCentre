package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 养老院康复训练记录表
 * </p>
 *
 * @author yy
 * @since 2025-08-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 老人ID，外键关联elder表
     */
    private Long elderId;

    /**
     * 康复训练计划名称，如：下肢力量恢复
     */
    private String planName;

    /**
     * 负责护士姓名（冗余存储，便于查询展示）
     */
    private String organizer;

    /**
     * 负责护士ID，外键关联nurse表id
     */
    private Long nurseId;

    /**
     * 训练计划开始日期
     */
    private LocalDate startDate;

    /**
     * 计划结束日期，可为空表示进行中
     */
    private LocalDate endDate;

    /**
     * 单次训练时长（分钟）
     */
    private Integer duration;

    /**
     * 训练内容详情，如具体动作、流程
     */
    private String details;



    private String elderName;
}
