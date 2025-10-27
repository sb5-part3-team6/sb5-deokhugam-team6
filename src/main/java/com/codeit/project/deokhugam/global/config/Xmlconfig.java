package com.codeit.project.deokhugam.global.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Xmlconfig {
    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }
}
