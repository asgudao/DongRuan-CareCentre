package com.neuedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neuedu.entity.SysFile;
import com.neuedu.mapper.SysFileMapper;
import com.neuedu.service.SysFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author yy
 * @since 2025-09-02
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    @Value("${minio.endpoint}")
    String endpoint;
    @Value("${minio.accesskey}")
    String accesskey;
    @Value("${minio.secretkey}")
    String secretkey;
    @Override
    public SysFile getFile(String md5, String contentType, long size, long activityId) {
        QueryWrapper<SysFile> wrapper = new QueryWrapper<>();
        wrapper.eq("size", size)
                .eq("md5", md5)
                .eq("content_type", contentType)
                .eq("activity_id", activityId);
        return this.getOne(wrapper);
    }

    @Override
    public String upload(MultipartFile file, String bucket, Long activityId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 获取文件md5 contentType size
        String md5 = DigestUtils.md5Hex(file.getInputStream());
        String contentType = file.getContentType();
        long size = file.getSize();

        // 先到数据库看看有没有上传过
        SysFile sysFile = this.getFile(md5, contentType, size, activityId);
        // 如果不为空 表示上传过 直接返回路径
        if (null != sysFile)
            return sysFile.getPath();
        // 构建Minio客户端
        MinioClient client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accesskey, secretkey)
                .build();
        // 文件防止重复
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String fileName = String.format("%s.%s", time, FilenameUtils.getExtension(file.getOriginalFilename()));
        // 定义 上传参数
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(fileName)
                .contentType(contentType)
                .stream(file.getInputStream(), size, -1)
                .build();
        // 上传
        client.putObject(args);
        // 定义路径
        String path = String.format("/%s/%s", bucket, fileName);
        // 保存到数据库
        this.save(new SysFile(md5, contentType, size, path, activityId)) ;
        return path;
    }
    @Autowired
    private SysFileMapper SysFileMapper;

    @Override
    public List<SysFile> getFilesByActivityId(Long activityId) {
        System.out.println("activity:" + activityId);
        return SysFileMapper.selectFilesByActivityId(activityId);
    }
}

