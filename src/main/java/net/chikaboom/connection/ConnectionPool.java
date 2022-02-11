package net.chikaboom.connection;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static net.chikaboom.util.constant.LoggerMessageConstant.*;

/**
 * Стандартный пул соединений
 */
public class ConnectionPool {
    private static final Logger logger = Logger.getLogger(ConnectionPool.class);
    private DataSource mysqlDataSource;

    public ConnectionPool(DataSource mysqlDataSource) {
        this.mysqlDataSource = mysqlDataSource;
    }

    public Connection getConnection() {
        logger.info(GETTING_CONNECTION);
        Connection connection = null;
        try {

            DataSource dataSource = mysqlDataSource;
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(ERROR_GETTING_CONNECTION, e);
        }

        logger.info(CONNECTION_GOT);
        return connection;
    }
}
