package net.chikaboom.util;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Упрощает создание строки запросов к базе данных путем использования паттерна Byilder
 *
 * С помощью данного класса запросы можно собирать как конструктор.
 * Ответственность за неправильную последовательность вызвов методов несет программист.
 *
 */
//TODO проверки на пустые коллекции. Необходима серъезная доработка по безопасности и исключительным ситуациям
public class QueryBuilder {
    private StringBuilder queryString;
    private static final Logger logger = Logger.getLogger(QueryBuilder.class);

    public QueryBuilder() {
        queryString = new StringBuilder();
    }

    /**
     * Добавляет к исходной строке выбор всех полей в таблице.
     */
    public QueryBuilder select() {
        queryString.append("SELECT * ");

        return this;
    }

    /**
     * Добавляет к исходной строке выбор определенного перечня полей в таблице.
     * @param columnNames имена полей, которые необходимо выбрать в запросе
     */
    public QueryBuilder select(List<String> columnNames) {
        if (!columnNames.isEmpty()) {
            queryString.append("SELECT (");
            columnNames.forEach(columnName -> queryString.append(columnName).append(","));
            queryString.deleteCharAt(queryString.length() - 1);
            queryString.append(") ");
        } else {
            logger.warn("Got empty ArrayList of columnNames to queryBuilder");
            queryString.append("SELECT * ");
        }

        return this;
    }

    /**
     * Добавляет к исходной строке выбор из конкретной таблицы.
     * @param tableName наименование таблицы, из которой будет делаться выборка
     */
    public QueryBuilder from(String tableName) {
        queryString.append("FROM ").append(tableName).append(" ");

        return this;
    }

    /**
     * Добавляет в исходную строку перечень таблиц из которых будет делаться выборка.
     * @param tableNames наименование таблиц из которых будет делаться выборка
     */
    public QueryBuilder from(List<String> tableNames) {
        if (!tableNames.isEmpty()) {
            queryString.append("FROM ");
            tableNames.forEach(tableName -> queryString.append(tableName).append(","));
            queryString.deleteCharAt(queryString.length() - 1).append(" ");
        } else {
            logger.error("Got empty ArrayList of tableNames to queryBuilder");
            //TODO Exception
        }

        return this;
    }

    /**
     * Добавляет к исходной строке одно условие фильтрации результатов выборки.
     * @param key наименование столюца-фильтра
     * @param value значение, которому должен быть равен столбец
     */
    @Deprecated
    public QueryBuilder where(String key, String value) {
        queryString.append("WHERE ").append(key).append("='").append(value).append("' ");

        return this;
    }

    /**
     * Добавляет к исходной строке одно условие фильтрации результатов выборки.
     * @param key наименование столюца-фильтра
     */
    public QueryBuilder where(String key) {
        queryString.append("WHERE ").append(key).append("=? ");

        return this;
    }

    //TODO REFACTORING
    /**
     * Добавляет к исходной строке множество условий фильтрации результатов выборки.
     * В передаваемой сущности содержится информация о полях-фильтрах, их типах сравнения и о том, какие операнды
     * использовать между сравнениями (Or или And).
     * @param whereEntity сущность, в которой хранится информация о полях, операциях и операндах
     */
    public QueryBuilder where(SqlWhereEntity whereEntity) {
        queryString.append("WHERE ");

        try {

            Map<String, SqlComparatorType> fields = whereEntity.getFields();
            Queue<SqlOperandType> operands = whereEntity.getOperands();

            fields.forEach((fieldName, comparatorType) -> {
                queryString.append(fieldName).append(comparatorType.getValue());
                if (!operands.isEmpty()) {
                    queryString.append(operands.poll().getValue());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Добавляет к исходной строке значение создания новой записи в указанной таблице.
     * @param tableName таблица, в которой необходимо создать новую запись
     * @param fieldName заполняет указанное поле значением пользователя
     */
    public QueryBuilder insert(String tableName, String fieldName) {
        queryString.append("INSERT INTO ").append(tableName).append(" (").append(fieldName).append(") VALUES (?) ");

        return this;
    }

    /**
     * Добавляет к исходной строке значение создания новой записи в указанной таблице.
     * @param tableName таблица, в которой необходимо создать новую запись
     * @param fieldNames заполняет указанные поля значениями пользователя
     */
    public QueryBuilder insert(String tableName, List<String> fieldNames) {
        queryString.append("INSERT INTO ").append(tableName).append(" (");
        fieldNames.forEach(fieldName -> queryString.append(fieldName).append(","));
        queryString.deleteCharAt(queryString.length() - 1);
        queryString.append(") VALUES (");
        for (int i = 0; i < fieldNames.size(); i++) {
            queryString.append("?,");
        }
        queryString.deleteCharAt(queryString.length() - 1).append(") ");

        return this;
    }

    /**
     * Добавляет к исходной строке значения объединения таблиц
     * @param joinType тип соединения
     * @param firstTable имя таблицы с которой идет присоединения
     * @param joiningTable имя присоединяемой таблицы
     * @param key1 первый ключ по которому происходит присоединение
     * @param key2 второй ключ по которому происходит присоединение
     */
    public QueryBuilder join(SqlJoinType joinType, String firstTable, String joiningTable, String key1, String key2) {
        queryString.append(joinType.getValue());
        queryString.append("'").append(joiningTable).append("' ON '").append(firstTable).append("'.")
                .append(key1).append("='").append(joiningTable).append("'.").append(key2).append(" ");

        return this;
    }

    /**
     * Добавляет к исходной строке значение удаления записи из таблицы
     */
    public QueryBuilder delete() {
        queryString.append("DELETE ");

        return this;
    }

    /**
     * Добавляет к исходной строке значение изменения одного поля в записи(ях) в определенной таблице.
     * @param tableName имя таблицы, в которой происходят изменения
     * @param fieldName имя изменяемого поля
     * @return
     */
    public QueryBuilder update(String tableName, String fieldName) {
        queryString.append("UPDATE ").append(tableName).append(" SET ").append(fieldName).append("=? ");

        return this;
    }

    /**
     * Добавляет к исходной строке значение изменения нескольких полей в записи(ях) в определенной таблице.
     * @param tableName имя таблицы, в которой происходят изменения
     * @param fieldNames имена изменяемых полей
     */
    public QueryBuilder update(String tableName, List<String> fieldNames) {
        queryString.append("UPDATE ").append(tableName).append(" SET ");
        fieldNames.forEach(fieldName -> queryString.append(fieldName).append("=?,"));
        queryString.deleteCharAt(queryString.length() - 1).append(" ");

        return this;
    }

    /**
     * Операция, завершающая построение запроса, добавляя точку с запятой в конце строки.
     * @return полную строку-запрос
     */
    public String build() {
        queryString.append(";");
        String result = queryString.toString();
        clear();

        return result;
    }

    /**
     * Очищает значения исходной строки-запроса
     */
    public void clear() {
        queryString = new StringBuilder();
    }
}
