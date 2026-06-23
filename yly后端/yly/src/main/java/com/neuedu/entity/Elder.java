package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 养老院老人信息表
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Elder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 老人ID，主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 老人姓名
     */
    private String name;

    /**
     * 家属联系电话
     */
    private String familyPhone;

    /**
     * 身份证号，18位
     */
    private String idCard;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别：0-女，1-男
     */
    private String gender;

    /**
     * 自理能力：0-能自理，1-半自理，2-不能自理
     */
    private Byte selfCareAbility;

    /**
     * 房间号，关联room表的room_number，为空表示未入住
     */
    private String roomNumber;

    /**
     * 关联用户表的的id
     */
    private Integer userId;

    /**
     * 入住时间，为空表示未入住
     */
    private String checkInTime;

    /**
     * 离开时间，为空表示当前在住
     */
    private String checkOutTime;

    /**
     * 负责护士ID，关联nurse表
     */
    private Long nurseId;

    /**
     * 支付宝生成的支付单号
     */
    private String outTradeNo;

    /**
     * 入住缴纳的金额
     */
    private BigDecimal price;

    /**
     * 是否缴费0-未交费  1-缴费
     */
    private Integer isActive;

    public void setuserId(Long userId) {
        if (userId != null) {
            // 检查是否超出 int 范围
            if (userId > Integer.MAX_VALUE || userId < Integer.MIN_VALUE) {
                throw new IllegalArgumentException("userId 超出 Integer 范围: " + userId);
            }
            this.userId = userId.intValue();
        } else {
            this.userId = null;
        }
    }

    public void setoutTradeNo(String outTradeNo) {
        this.outTradeNo=outTradeNo;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer isDeleted;
}
