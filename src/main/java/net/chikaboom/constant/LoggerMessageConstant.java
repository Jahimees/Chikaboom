package net.chikaboom.constant;

public class LoggerMessageConstant {
    public static final String COMMAND_IS_NOT_EXISTS = "Command is not exists!";
    public static final String COMMAND_GOT = " command got.";
    public static final String ERROR_GETTING_CONNECTION = "Error getting connection. " +
            "Please check datasource settings and internet connection.";
    public static final String CONNECTION_GOT = "Connection got.";
    public static final String GETTING_CONNECTION = "Getting connection...";
    public static final String CONNECTION_POOL_CREATED = "Connection pool created.";

    //ERROR
    public static final String QUERY_EXECUTION_ERROR = "Error during executing the query.";
    public static final String CONNECTION_CLOSING_ERROR = "Connection cannot be closed.";
    public static final String SETTING_PARAMETER_ERROR = "Cannot set parameter to PreparedStatement.";
    public static final String GETTING_PARAMETER_ERROR = "Cannot get parameter from PreparedStatement.";
    public static final String GOT_EMPTY_LIST_QB_ERROR = "Got an empty List to the 'from' method in the QueryBuilder class.";
    public static final String GOT_EMPTY_LIST_ERROR = "The method cannot work with an empty list";

    //WARNING
    public static final String GOT_EMPTY_LIST_QB_WARN = "Got an empty List to the 'select' method in the QueryBuilder class. " +
            "Changing the query to 'Select *'.";
}
