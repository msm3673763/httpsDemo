package com.masm.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis配置
 *
 * @author masiming
 * @create 2017/11/18
 **/
@Configuration
public class MybatisMapperScannerConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scanner = new MapperScannerConfigurer();
        //set 手动配置的sqlSessionFactory
        scanner.setSqlSessionFactoryBeanName("sqlSessionFactory");
        scanner.setBasePackage("com.masm.mapper");
        return scanner;
    }
}
