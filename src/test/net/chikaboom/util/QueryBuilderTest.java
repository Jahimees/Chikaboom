package net.chikaboom.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class QueryBuilderTest {

    static QueryBuilder queryBuilder;
    static List<String> fieldNames;
    static List<String> tableNames;
    static SqlWhereEntity sqlWhereEntity;

    @BeforeAll
    public static void initialize() {
        queryBuilder = new QueryBuilder();

        fieldNames = new ArrayList<>();
        fieldNames.add("field1");
        fieldNames.add("field2");
        fieldNames.add("field3");

        tableNames = new ArrayList<>();
        tableNames.add("table1");
        tableNames.add("table2");

        sqlWhereEntity = new SqlWhereEntity();
        Map<String, SqlComparatorType> fields = new LinkedHashMap<>();
        fields.put("field1", SqlComparatorType.EQUAL);
        fields.put("field2", SqlComparatorType.NOT_EQUAL);
        fields.put("field3", SqlComparatorType.GRATER);
        fields.put("field4", SqlComparatorType.LESS);
        fields.put("field5", SqlComparatorType.GREATER_OR_EQUAL);
        fields.put("field6", SqlComparatorType.LESS_OR_EQUAL);
        fields.put("field7", SqlComparatorType.IS_NULL);
        fields.put("field8", SqlComparatorType.IS_NOT_NULL);

        Queue<SqlOperandType> operandTypes = new LinkedList<>();
        operandTypes.add(SqlOperandType.AND);
        operandTypes.add(SqlOperandType.OR);
        operandTypes.add(SqlOperandType.AND);
        operandTypes.add(SqlOperandType.OR);
        operandTypes.add(SqlOperandType.AND);
        operandTypes.add(SqlOperandType.OR);
        operandTypes.add(SqlOperandType.AND);

        sqlWhereEntity.setFields(fields);
        sqlWhereEntity.setOperands(operandTypes);
    }

    @AfterEach
    public void afterEach() {
        queryBuilder.clear();
    }

    @Test
    public void selectTest() {
        //        TODO проверка на NULL
        String actual1 = queryBuilder.select().build();
        String expected1 = "SELECT * ;";

        queryBuilder.clear();
        String actual2 = queryBuilder.select(fieldNames).build();
        String expected2 = "SELECT (field1,field2,field3) ;";

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    public void fromTest() {
        //        TODO проверка на NULL
        String actual1 = queryBuilder.from("table1").build();
        String expected1 = "FROM table1 ;";

        queryBuilder.clear();
        String actual2 = queryBuilder.from(tableNames).build();
        String expected2 = "FROM table1,table2 ;";

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    public void where() {
        //      TODO проверка невалидных данных
        String actual1 = queryBuilder.where("field1").build();
        String expected1 = "WHERE field1=? ;";

        queryBuilder.clear();
        String actual2 = queryBuilder.where("field1", "5").build();
        String expected2 = "WHERE field1='5' ;";

        queryBuilder.clear();
        String actual3 = queryBuilder.where(sqlWhereEntity).build();
        String expected3 = "WHERE field1=? AND field2!=? OR field3>? " +
                "AND field4<? OR field5>=? AND field6<=? OR field7 IS NULL AND field8 IS NOT NULL;";

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
        Assertions.assertEquals(expected3, actual3);
    }

    @Test
    public void insertTest() {
        String actual1 = queryBuilder.insert("table1", "field1").build();
        String expected1 = "INSERT INTO table1 (field1) VALUES (?) ;";

        queryBuilder.clear();
        String actual2 = queryBuilder.insert("table1", fieldNames).build();
        String expected2 = "INSERT INTO table1 (field1,field2,field3) VALUES (?,?,?) ;";

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    public void join() {
        String actual1 = queryBuilder
                .join(SqlJoinType.INNER_JOIN, "table1", "table2", "field1", "field2")
                .build();
        String expected1 = " INNER JOIN 'table2' ON 'table1'.field1='table2'.field2 ;";

        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    public void delete() {
        String actual1 = queryBuilder.delete().build();
        String expected1 = "DELETE ;";

        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    public void update() {
        String actual1 = queryBuilder.update("table1", "fieldName1").build();
        String expected1 = "UPDATE table1 SET fieldName1=? ;";

        queryBuilder.clear();
        String actual2 = queryBuilder.update("table1", fieldNames).build();
        String expected2 = "UPDATE table1 SET field1=?,field2=?,field3=? ;";

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    public void build() {
        String actual1 = queryBuilder.build();
        String expected1 = ";";

        Assertions.assertEquals(expected1, actual1);
    }

}
