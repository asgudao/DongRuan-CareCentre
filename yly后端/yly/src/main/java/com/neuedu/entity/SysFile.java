package com.neuedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author yy
 * @since 2025-09-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_file")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    public SysFile(String md5, String contentType, Long size, String path , Long activityId) {
        this.md5 = md5;
        this.contentType = contentType;
        this.size = size;
        this.path = path;
        this.activityId = activityId;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件md5
     */
    private String md5;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 关联活动id
     */
    private Long activityId;
}

