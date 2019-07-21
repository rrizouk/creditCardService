package com.sapient.creditcardprocessing.config;

import com.sapient.creditcardprocessing.repository.CreditCardProcessingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@ComponentScan(value = {"com.sapient.creditcardprocessing,com.sapient.creditcardprocessing.repository"})
@PropertySources({@PropertySource(value = "classpath:sql/db.properties")})
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = {CreditCardProcessingRepository.class,}, entityManagerFactoryRef = "entityManagerFactory")
@EnableWebMvc
public class CreditCardProcessingConfig extends WebMvcAutoConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Database database(@Value("${spring.datasource.vendor}") String databaseVendor) {
        return Database.valueOf(databaseVendor);
    }

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.url}") String url,
                                 @Value("${spring.datasource.driverClassName}") String driver,
                                 @Value("${spring.datasource.username}") String user, @Value("${spring.datasource.password}") String pass) {
        return new DriverManagerDataSource(url, user, pass);
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource,
                                                                                         Database database, @Value("${spring.jpa.schemaUpdate}") boolean schemaUpdate) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter(database, schemaUpdate));
        factoryBean.setPackagesToScan("com.sapient.creditcardprocessing.domain","com.sapient.creditcardprocessing.service","com.sapient.creditcardprocessing.repository");
        factoryBean.setPersistenceUnitName("creditCardDetails");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(Database database, boolean schemaUpdate) {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(schemaUpdate);
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setDatabase(database);
        return hibernateJpaVendorAdapter;
    }
}
