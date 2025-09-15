package com.albert.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NacosController {

    @Value("${nacos.data.value1:001}")
    private String value1;


    @GetMapping("/get")
    public String get() {
        return value1;
    }

}
