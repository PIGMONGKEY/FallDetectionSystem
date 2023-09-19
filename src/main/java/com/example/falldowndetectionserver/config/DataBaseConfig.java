package com.example.falldowndetectionserver.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataBaseConfig {
    private final ApplicationContext applicationContext;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();

        bean.setDataSource(dataSource);
        bean.setMapperLocations(applicationContext.getResources("classpath:/mapper/*.xml"));
        bean.setConfigLocation(applicationContext.getResource("classpath:/config/config.xml"));

        try {
            SqlSessionFactory sqlSessionFactory = bean.getObject();
            sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);

            return sqlSessionFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
