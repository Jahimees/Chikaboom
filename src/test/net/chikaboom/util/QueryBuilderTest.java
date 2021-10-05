package net.chikaboom.util;

import net.chikaboom.exception.EmptyListException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.LoggerMessageConstant.GOT_EMPTY_LIST_ERROR;

public class QueryBuilderTest {

    static QueryBuilder queryBuilder;
    static List<String> fieldNames;
    static List<String> tableNames;

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
    }

    @AfterEach
    public void afterEach() {
        queryBuilder.clear();
    }

    @Test
    public void selectTest() {
        String actual1 = queryBuilder.select().build();
        String expected1 = "SELECT * ;";

        queryBuilder.clear();
        String actual2 = queryBuilder.select(fieldNames).build();
        String expected2 = "SELECT (field1,field2,field3) ;";

        queryBuilder.clear();
        String actual3 = queryBuilder.select(new ArrayList<>()).build();
        String expected3 = "SELECT * ;";

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
        Assertions.assertEquals(expected3, actual3);
    }

    @Test
    public void fromTest() {
        String actual1 = queryBuilder.from("table1").build();
        String expected1 = "FROM table1 ;";

        queryBuilder.clear();
        String actual2 = null;
        try {
            actual2 = queryBuilder.from(tableNames).build();
        } catch (EmptyListException e) {
            e.printStackTrace();
        }
        String expected2 = "FROM table1,table2 ;";

        queryBuilder.clear();
        Exception actual3 = null;
        try {
            queryBuilder.from(new ArrayList<>()).build();
        } catch (EmptyListException e) {
            actual3 = e;
        }
        Exception expected3 = new EmptyListException(GOT_EMPTY_LIST_ERROR);

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
        Assertions.assertEquals(expected3.getClass(), actual3.getClass());
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
        String actual3 = queryBuilder.where().build();
        String expected3 = "WHERE ;";

        queryBuilder.clear();
        String actual4 = queryBuilder.where()
                .compare("field1", SqlComparatorType.EQUAL).and()
                .compare("field2", SqlComparatorType.GREATER_OR_EQUAL).or()
                .compare("field3", SqlComparatorType.IS_NOT_NULL).or()
                .compare("field4", SqlComparatorType.GREATER).and()
                .compare("field5", SqlComparatorType.IS_NULL).and()
                .compare("field6", SqlComparatorType.LESS).or()
                .compare("field7", SqlComparatorType.LESS_OR_EQUAL).and()
                .compare("field8", SqlComparatorType.NOT_EQUAL).build();
        String expected4 = "WHERE field1=? AND field2>=? OR field3 IS NOT NULL " +
                "OR field4>? AND field5 IS NULL AND field6<? OR field7<=? AND field8!=?;";

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
        Assertions.assertEquals(expected3, actual3);
        Assertions.assertEquals(expected4, actual4);
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
