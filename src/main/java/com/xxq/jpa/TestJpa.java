package com.xxq.jpa;

import com.xxq.jpa.domain.User;
import com.xxq.jpa.repository.UserRepository;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class TestJpa {

    public static void main(String[] args) {
        // 配置数据源，例如使用HikariCP、Druid等连接池
        DataSource dataSource = configureDataSource();

        // 配置JPA实体扫描路径
        String entityScanPackage = "com.xxq.jpa.domain";

        // 配置JPA Repository扫描路径
//        String repositoryBasePackage = "com.derbysoft.futurex.sc.channel.adapter.domain.repository";

        // 配置JPA属性
        Map<String, String> jpaProperties = configureJpaProperties();

        // 创建EntityManagerFactory
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("h2PersistenceUnitName",
//                jpaProperties);


        AbstractJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.H2);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");

        Map<String , Object> properties =
                new HibernateProperties().determineHibernateProperties(
                        jpaProperties,
                        new HibernateSettings()
                );
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setPackagesToScan(entityScanPackage);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        localContainerEntityManagerFactoryBean.getJpaPropertyMap().putAll(properties);
        localContainerEntityManagerFactoryBean.afterPropertiesSet();

        EntityManagerFactory entityManagerFactory1 = localContainerEntityManagerFactoryBean.getObject();
        // 创建PlatformTransactionManager
        PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory1);

        // 创建Spring Data JPA上下文
//        JpaContext jpaContext = new DefaultJpaContext(Set.of(entityManagerFactory.createEntityManager()));
        JpaRepositoryFactory repositoryFactory = new JpaRepositoryFactory(entityManagerFactory1.createEntityManager());

        UserRepository repository = repositoryFactory.getRepository(UserRepository.class);
        User socialHotel = new User();
        repository.save(socialHotel);

        System.out.println(repository.findAll());
        entityManagerFactory1.close();
    }

    private static Map<String, String> configureJpaProperties() {
        Map<String,String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");

        // 配置数据库方言
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        return properties;
    }

    private static DataSource configureDataSource() {
        JdbcDataSource datasource = new JdbcDataSource();
        datasource.setUser("sa");
        datasource.setURL("jdbc:h2:mem:testdb;MODE=LEGACY");
        return datasource;
    }
}
