package com.albert.cloud.nacos.controller;

import com.albert.cloud.nacos.service.NacosConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ConfigController {

    private final NacosConfigService nacosConfigService;

    @Autowired
    public ConfigController(NacosConfigService nacosConfigService) {
        this.nacosConfigService = nacosConfigService;
    }

    @GetMapping("/get")
    public String get() {
        return "a";
    }
}
