package com.neuedu.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neuedu.util.HttpNeueduRequest;
import com.neuedu.util.JwtUtil;
import com.neuedu.vo.ResultJson;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@ConfigurationProperties(prefix = "ignore.white")
public class AuthorizationFilter implements Filter {

    List<String> urls ;
    @Value("${allow.origin}")
    String allowOrigin;
    @Resource(name = "hyxRedisTemplate")
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    ObjectMapper objectMapper;
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        if (!(servletRequest instanceof HttpServletRequest request) ||
                !(servletResponse instanceof HttpServletResponse response)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 处理预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 白名单放行
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String url : urls) {
            if (pathMatcher.match(url, request.getServletPath())) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // 获取 token
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            error(response, ResultJson.unauthorized("未登录"));
            return;
        }

        // ✅ 第一步：先解析 role 和 id
        Map<String, Object> claimMap;
        try {
            claimMap = JwtUtil.parseRoleAndId(token);
        } catch (Exception e) {
            error(response, ResultJson.unauthorized("非法请求"));
            return;
        }

        Long userId = (Long) claimMap.get("id");
        String role = (String) claimMap.get("role");

        if (userId == null || role == null) {
            error(response, ResultJson.unauthorized("Token 数据不完整"));
            return;
        }

        // ✅ 第二步：根据 role 决定 Redis Key
        String redisKey;
        if ("user".equals(role)) {
            redisKey = String.format("ums:%d:login", userId);
        } else if ("admin".equals(role)) {
            redisKey = String.format("admin:login:%d", userId);
        } else {
            error(response, ResultJson.unauthorized("不支持的用户类型"));
            return;
        }

        // ✅ 第三步：检查 Redis 中是否存在该会话
        if (!redisTemplate.hasKey(redisKey)) {
            error(response, ResultJson.unauthorized("登录已过期"));
            return;
        }

        // ✅ 第四步：延长登录时间
        redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);

        // ✅ 第五步：包装 request，传递用户 ID（兼容旧代码）
        HttpNeueduRequest wrappedRequest = new HttpNeueduRequest(request);
        wrappedRequest.addParams("open_id", String.valueOf(userId)); // 兼容旧 Controller
        wrappedRequest.addParams("user_id", String.valueOf(userId));
        wrappedRequest.addParams("role", role);

        // ✅ 可选：把原始 token 也传下去（如果 Controller 还要用）
        // wrappedRequest.addHeader("token", token);

        filterChain.doFilter(wrappedRequest, response);
    }

    private void error (HttpServletResponse response, ResultJson resultJson) throws IOException {
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.getWriter().write(objectMapper.writeValueAsString(resultJson));
    }
}
