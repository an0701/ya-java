package com.yac.service.auth.service.impl;

import com.yac.service.auth.model.AuthClient;
import com.yac.service.auth.mapper.AuthClientMapper;
import com.yac.service.auth.service.AuthClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
