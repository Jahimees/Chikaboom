package net.chikaboom.config;

import jakarta.servlet.MultipartConfigElement;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.unit.DataSize;

import javax.sql.DataSource;

/**
 * Определяет базовую конфигурацию приложения, в том числе местанахождение используемых файлов properties.
 */
@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:constants.properties")
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

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Конфигурация бина подключения к базе данных.
     */
    @Bean
    public DataSource getDataSource() {
        logger.info("Creating dataSource bean:");
        logger.info("DriverClassName: " + driverClassName + "\nurl: " + url);

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driverClassName);
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);

        logger.info("DataSource bean created");

        return driverManagerDataSource;
    }

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
}
