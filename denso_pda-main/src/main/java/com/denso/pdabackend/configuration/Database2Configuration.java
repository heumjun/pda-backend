package com.denso.pdabackend.configuration;


import com.denso.pdabackend.PdaBackendBeanNameGenerator;
import com.denso.pdabackend.utils.YamlLoadFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
//@ConfigurationProperties(prefix = "yaml")
@EnableTransactionManagement
@PropertySource(value="classpath:application.yml",factory=YamlLoadFactory.class)
//bean 이름 중복방지 (HealthBeanNameGenerator 클래스에서 패키지명까지 bean 이름을 생성시키지만 인터페이스인 mapper는 적용이안됨. 아래 구문으로 적용시킴.)
@MapperScan(basePackages = "com.denso.pdabackend.**.mapper2",nameGenerator = PdaBackendBeanNameGenerator.class )
@RequiredArgsConstructor
public class Database2Configuration {

    private final ApplicationContext applicationContext;

    @Bean(name= "hikariConfig2")
    @ConfigurationProperties(prefix = "spring.datasource.hikari2")
    public HikariConfig hikariConfig2(){
        return new HikariConfig();
    }

    @Bean(name= "dataSource2")
    public DataSource dataSource2(){
        return new HikariDataSource(hikariConfig2());
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration2")
    public org.apache.ibatis.session.Configuration mybatisConfig2(){
        return new org.apache.ibatis.session.Configuration();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory2(DataSource dataSource2) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource2);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper2/**/*.xml"));
        sqlSessionFactoryBean.setConfiguration(mybatisConfig2());

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate2(SqlSessionFactory sqlSessionFactory2) {
        return new SqlSessionTemplate(sqlSessionFactory2);
    }

    @Bean
    public PlatformTransactionManager transactionManager2() {
        return new DataSourceTransactionManager(dataSource2());
    }


}
