package com.neuedu.mapper;

import com.neuedu.entity.SysFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 文件夹 Mapper 接口
 * </p>
 *
 * @author yy
 * @since 2025-09-02
 */
public interface SysFileMapper extends BaseMapper<SysFile> {
    @Select("SELECT * FROM sys_file WHERE activity_id = #{activityId}")
    List<SysFile> selectFilesByActivityId(@Param("activityId") long activityId);
}
