package com.masm.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Created by masiming on 2017/11/18 21:11.
 */
@Configuration
//@EnableTransactionManagement//加上这个注解，使得支持事务
public class MybatisConfig/* implements TransactionManagementConfigurer*/ {

//    @Autowired
//    private DataSource dataSource;
//
//    @Override
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Bean("sqlSessionFactory")
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // 增加拦截器
//        MybatisPageInterceptor interceptor = new MybatisPageInterceptor();
//        interceptor.setProperties(null);
//        MybatisLogInterceptor logInterceptor = new MybatisLogInterceptor();
//        bean.setPlugins(new Interceptor[] { interceptor, logInterceptor });

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath:mybatis/*.xml"));
        return bean.getObject();
    }

//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
}
