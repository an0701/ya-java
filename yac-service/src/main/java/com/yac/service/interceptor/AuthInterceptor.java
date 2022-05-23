package com.yac.service.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.wall.violation.ErrorCode;
import com.yac.core.common.api.ResultCode;
import com.yac.core.common.pojo.bo.JwtModelBO;
import com.yac.core.common.util.TokenUtil;
import com.yac.service.annotation.PassToken;
import com.yac.service.annotation.UserLoginToken;
import com.yac.service.module.auth.model.AuthClient;
import com.yac.service.module.auth.service.AuthClientService;
import com.yac.service.util.ResponseUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Swagger拦截器
 *
 * @author anri
 * @date 2020/10/10 13:54
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    AuthClientService authClientService;

    /**
     * 1. 在所有接口访问后更新所对应 token 的失效时间
     * 2. 增删改操作自动刷新缓存
     * 3. 增删改操作修改数据接收模块的初始状态，使其也
     * 自动刷新缓存
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        if (null == token) {
            return;
        }
    }

    /**
     * 在所有接口访问前判断是否有 token
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        /**
         * 从 http 请求头中取出 token
         */
        String token = request.getHeader("token");
        /**
         * 如果不是映射到方法直接通过
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        /**
         * 检查是否有 PassToken 注释，有则跳过认证
         */
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        /**
         * 检查有没有需要用户权限的注解
         */
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (null == token) {
                    return ResponseUtil.errResponse(
                            response, ResultCode.TOKEN_NULL_ERROR.getCode(), ResultCode.TOKEN_NULL_ERROR.getMessage());
                }
                /**
                 * token验证
                 */
                JwtModelBO jwtModelBO = TokenUtil.parseJwt(token);
                if (ObjectUtil.isNotNull(jwtModelBO) && ObjectUtil.isNotNull(jwtModelBO.getClientId())) {
                    AuthClient authClient = authClientService.getByClientId(jwtModelBO.getClientId());
                    if(ObjectUtil.isNull(authClient) || ObjectUtil.isNull(TokenUtil.verifyJwt(token, authClient.getJwtsecret()))){
                        return ResponseUtil.errResponse(
                                response, ResultCode.TOKEN_VERIFY_ERROR.getCode(), ResultCode.TOKEN_VERIFY_ERROR.getMessage());
                    }
                }
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}