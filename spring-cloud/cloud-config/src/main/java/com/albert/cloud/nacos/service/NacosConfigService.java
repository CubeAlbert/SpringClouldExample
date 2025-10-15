package com.albert.cloud.nacos.service;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NacosConfigService {

    private final ConfigService configService;
    private final NamingService namingService;

    private final String nacosServerAddr;

    @Autowired
    public NacosConfigService(@Value("${albert.nacos.server-addr}") String nacosServerAddr) throws NacosException {
        this.nacosServerAddr = nacosServerAddr;
        this.configService = NacosFactory.createConfigService(nacosServerAddr);
        this.namingService = NacosFactory.createNamingService(nacosServerAddr);
    }
}
