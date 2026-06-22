package com.neuedu.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class AutoCoding {
    public static void main(String[] args) {
        // 定义项目目录
        String basePath = System.getProperty("user.dir");
        String url = "jdbc:mysql://192.168.80.110:3307/yly?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
        // 定义数据表
        String table = "room";
        // 创建生成器
        FastAutoGenerator generator = FastAutoGenerator.create(url, "root", "193156");
        // 全局配置
        generator.globalConfig(builder -> {
            builder.author("yy")
                    .outputDir(basePath + "/src/main/java")
                    .disableOpenDir();
        });
        // 包配置
        generator.packageConfig(builder -> {
            // 设置父包
            builder.parent("com.neuedu");
            // 配置路径
            Map<OutputFile, String> pathInfo = new HashMap<>();
            pathInfo.put(OutputFile.xml, basePath + "/src/main/resources/com/neuedu/mapper");
            builder.pathInfo(pathInfo);
        });
        // 策略配置
        generator.strategyConfig(builder -> {
            // 配置生成的表名
            builder.addInclude(table);
            // entity配置
            builder.entityBuilder()
                    .enableLombok()
                    .naming(NamingStrategy.underline_to_camel)
                    .columnNaming(NamingStrategy.underline_to_camel);
            // controller配置
            builder.controllerBuilder()
                    .enableRestStyle();
            // service配置
            builder.serviceBuilder()
                    .formatServiceFileName("%sService");
        });
        // 模板引擎
        generator.templateEngine(new FreemarkerTemplateEngine());
        // 生成代码
        generator.execute();
    }
}
