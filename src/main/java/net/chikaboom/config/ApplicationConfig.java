package net.chikaboom.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.servlet.MultipartConfigElement;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;

/**
 * Определяет базовую конфигурацию приложения, в том числе местанахождение используемых файлов properties.
 */
@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:constants.properties")
@RequiredArgsConstructor
public class ApplicationConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${data.size}")
    private String dataSize;

//    private final EntityManagerFactory entityManagerFactory;

    private final Logger logger = Logger.getLogger(this.getClass());


//    @Bean
//    public SessionFactory sessionFactory() {
//        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
//            throw new NullPointerException("factory is not a hibernate factory");
//        }
//        return entityManagerFactory.unwrap(SessionFactory.class);
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource());
//        sessionFactory.setPackagesToScan("net.chikaboom.model.database");
//        sessionFactory.setHibernateProperties(hibernateProperties());
//
//        return sessionFactory;
//    }


//    @Bean
//    public PlatformTransactionManager hibernateTransactionManager() {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory().getObject());
//
//        return transactionManager;
//    }

    /**
     * Конфигурация бина подключения к базе данных.
     */
//    @Bean
//    public DataSource dataSource() {
//        logger.info("Creating dataSource bean:");
//        logger.info("DriverClassName: " + driverClassName + "\nurl: " + url);
//
//        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
//        driverManagerDataSource.setDriverClassName(driverClassName);
//        driverManagerDataSource.setUrl(url);
//        driverManagerDataSource.setUsername(username);
//        driverManagerDataSource.setPassword(password);
//
//        logger.info("DataSource bean created");
//
//        return driverManagerDataSource;
//    }

    /**
     * Конфигурация файлов, получаемых с клиента на сервер.
     */
    @Bean
    public MultipartConfigElement getMultipartConfigElement() {
        logger.info("Creating multipartConfigElementBean");

        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(dataSize));
        factory.setMaxRequestSize(DataSize.parse(dataSize));

        logger.info("MultipartConfigElementBean created");

        return factory.createMultipartConfig();
    }

//    private Properties hibernateProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "none");
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySqlDialect");
//        properties.setProperty("hibernate.show_sql", "true");
//
//        return properties;
//    }

    @Bean
    private static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setDateFormat(new StdDateFormat());
    }
}
