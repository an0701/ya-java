package com.yac.service.module.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yac.service.module.auth.mapper.AuthClientMapper;
import com.yac.service.module.auth.model.AuthClient;
import com.yac.service.module.auth.service.AuthClientService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author xiaoya
 * @since 2022-05-17
 */
@Service
public class AuthClientServiceImpl extends ServiceImpl<AuthClientMapper, AuthClient> implements AuthClientService {

    @Override
    public AuthClient getByClientId(String clientId) {
        return baseMapper.selectByClientId(clientId);
    }
}
