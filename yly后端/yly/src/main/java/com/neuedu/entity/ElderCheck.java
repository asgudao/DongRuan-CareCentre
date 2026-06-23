package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author yy
 * @since 2025-08-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("elder_check")
public class ElderCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检查记录ID
     */
    @TableId(value = "check_id", type = IdType.AUTO)
    private Integer checkId;

    /**
     * 老人唯一标识
     */
    private Integer elderId;

    /**
     * 老人姓名
     */
    private String elderName;

    /**
     * 检查日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkDate;

    /**
     * 血压(mmHg)
     */
    private Integer bloodPressure;

    /**
     * 血糖(mmol/L)
     */
    private BigDecimal bloodGlucose;

    /**
     * 血脂(mmol/L)
     */
    private BigDecimal bloodLipid;

    /**
     * 检查人
     */
    private String checkPerson;

    /**
     * 体重 (kg)
     */
    private Double weight;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer isDeleted;
}
