package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyx
 * @since 2025-08-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nurse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String phone;

    /**
     * 0-女，1-男
     */
    private Byte gender;

    /**
     * 0-离职，1-在职
     */
    private Byte workStatus;
}
