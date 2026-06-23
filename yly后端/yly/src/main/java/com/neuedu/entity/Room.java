package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 养老院房间信息表
 * </p>
 *
 * @author yy
 * @since 2025-09-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房间ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房间号，如：101、202A等
     */
    private String roomNumber;

    /**
     * 房间状态：0-空闲，1-已住满，2-维护中，3-已预订
     */
    private Byte status;

    /**
     * 房间容量，可容纳的老人数量
     */
    private Integer capacity;

    /**
     * 房间价格，单位：元/月
     */
    private BigDecimal price;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer isDeleted;
}
