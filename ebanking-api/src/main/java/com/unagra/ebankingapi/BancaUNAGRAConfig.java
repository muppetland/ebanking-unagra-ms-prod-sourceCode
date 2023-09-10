package com.unagra.ebankingapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "bancaUNAGRAEntityManagerFactory", transactionManagerRef = "bancaUNAGRATransactionManagerRef", basePackages = {
        "com.unagra.ebankingapi.repository.bancaUNAGRA" })
public class BancaUNAGRAConfig {
    @Autowired
    private Environment env;

    @Bean(name = "bancaUNAGRADataSource")
    public DataSource bancaUNAGRADataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("db2.datasource.url"));
        dataSource.setUsername(env.getProperty("db2.datasource.username"));
        dataSource.setPassword(env.getProperty("db2.datasource.password"));
        dataSource.setDriverClassName(env.getProperty("db2.datasource.driver.class"));

        return dataSource;
    }

    @Bean(name = "bancaUNAGRAEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(bancaUNAGRADataSource());
        lcemfb.setPackagesToScan("com.unagra.ebankingapi.entities.bancaUNAGRA");

        // Hibernet implementation...
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        lcemfb.setJpaVendorAdapter(vendorAdapter);

        // Agregamos propieades del Hibernate...
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("db1.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.show-sql", env.getProperty("db1.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("db1.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.default_schema", env.getProperty("db1.jpa.properties.hibernate.default_schema"));
        properties.put("hibernate.format_sql", env.getProperty("db1.jpa.properties.hibernate.format_sql"));
        properties.put("hibernate.bootstrap-mode", env.getProperty("db1.data.jpa.repositories.bootstrap-mode"));

        // set properties to hibernate adapter...
        lcemfb.setJpaPropertyMap(properties);

        return lcemfb;
    }

    @Bean(name = "bancaUNAGRATransactionManagerRef")
    public PlatformTransactionManager transactionManger() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
