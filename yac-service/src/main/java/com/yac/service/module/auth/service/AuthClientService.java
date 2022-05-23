package com.yac.service.module.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yac.service.module.auth.model.AuthClient;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author xiaoya
 * @since 2022-05-17
 */
public interface AuthClientService extends IService<AuthClient> {
    public AuthClient getByClientId(String clientId);
}
