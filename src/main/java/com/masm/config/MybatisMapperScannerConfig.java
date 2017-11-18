package com.masm.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by masiming on 2017/11/18 22:28.
 */
@Configuration
public class MybatisMapperScannerConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scanner = new MapperScannerConfigurer();
        scanner.setSqlSessionFactoryBeanName("sqlSessionFactory");  //set 手动配置的sqlSessionFactory
        scanner.setBasePackage("com.masm.mapper");
        return scanner;
    }
}
