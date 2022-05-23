package com.yac.service.module.auth.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yac.core.common.api.CommonResult;
import com.yac.core.common.pojo.bo.JwtModelBO;
import com.yac.core.common.util.TokenUtil;
import com.yac.service.annotation.PassToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author xiaoya
 * @since 2022-05-17
 */
@Api(value = "AuthClientController", tags = "权限验证")
@RestController
@RequestMapping("/api/v2/auth/authClient")
public class AuthClientController {

    @PassToken
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取token")
    @GetMapping("/token")
    public CommonResult<String> getAllBuckets(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",1);
        claims.put("username","password");
        claims.put("clientId", "yxlinks" );
//        claims.put("jwtsecret",  "2022031001");
        claims.put("expriedTime",  3600L);
        JwtModelBO jwtModelBO = JwtModelBO.builder()
                .clientId("yxlinks")
                .expriedTime(60L)
                .build();
        String jwt = TokenUtil.createJWT(claims, "yxlinks", "2022031001", 60L);
        return CommonResult.success(jwt);
    }
}

