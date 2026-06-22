package com.neuedu.controller;

import com.neuedu.entity.SysFile;
import com.neuedu.service.SysFileService;
import com.neuedu.vo.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文件夹 前端控制器
 * </p>
 *
 * @author yy
 * @since 2025-09-02
 */
@RestController
@RequestMapping("/sysFile")
public class SysFileController {

    @Autowired
    private SysFileService sysFileService; // ✅ 命名规范：小写开头

    /**
     * 文件上传接口
     */
    @PostMapping("/upload")
    public ResultJson<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucket") String bucket,
            @RequestParam("activityId") Long activityId) {

        try {
            // 调用 Service 上传
            String path = sysFileService.upload(file, bucket ,activityId);
            return ResultJson.success(path, "上传成功"); // ✅ 返回成功格式
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.failed("上传失败: " + e.getMessage()); // ✅ 返回失败格式
        }
    }

    /**
     * 根据 activityId 获取该活动下的所有图片
     */
    @GetMapping("/{id}/images")
    public ResultJson<List<SysFile>> getActivityImages(@PathVariable Long id) {
        try {
            List<SysFile> files = sysFileService.getFilesByActivityId(id);
            return ResultJson.success(files, "获取图片列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.failed("获取图片列表失败");
        }
    }
}