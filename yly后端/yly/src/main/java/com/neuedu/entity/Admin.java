package com.neuedu.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 管理员信息表
 * </p>
 *
 * @author yy
 * @since 2025-08-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员唯一标识ID
     */
    private Long id;

    /**
     * 管理员姓名
     */
    private String name;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 管理员工号
     */
    private String adminId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer isDeleted;
}
