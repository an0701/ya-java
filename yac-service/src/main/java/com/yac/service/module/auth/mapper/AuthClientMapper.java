package com.yac.service.module.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yac.service.module.auth.model.AuthClient;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author xiaoya
 * @since 2022-05-17
 */
public interface AuthClientMapper extends BaseMapper<AuthClient> {
    AuthClient selectByClientId(String clientId);
}
