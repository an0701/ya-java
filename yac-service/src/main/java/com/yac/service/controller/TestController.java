package com.yac.service.controller;


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
@Api(value = "TestController", tags = "接口测试")
@RestController
@RequestMapping("/api/v2/test")
public class TestController {

    @PassToken
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "test")
    @GetMapping("/")
    public CommonResult<String> test(){

        return CommonResult.success();
    }
}

