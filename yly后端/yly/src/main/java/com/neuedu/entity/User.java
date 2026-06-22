package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 监护人登录表
 * </p>
 *
 * @author hyx
 * @since 2025-08-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 监护人名字
     */
    private String name;

    /**
     * 监护人手机号
     */
    private String phone;

    /**
     * 监护人邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Byte sex;

    /**
     * 密码
     */
    private String password;
}
