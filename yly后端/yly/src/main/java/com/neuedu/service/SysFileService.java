package com.neuedu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neuedu.entity.SysFile;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 * 文件表 服务类
 * </p>
 *
 * @author yy
 * @since 2025-09-02
 */
public interface SysFileService extends IService<SysFile> {
    SysFile getFile(String md5, String contentType, long size, long activityId);
    String upload(MultipartFile file, String bucket , Long activityId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    List<SysFile> getFilesByActivityId(Long activityId);
}
