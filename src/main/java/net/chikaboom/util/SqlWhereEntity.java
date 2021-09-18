package net.chikaboom.util;

import java.util.Map;
import java.util.Queue;

/**
 * Класс-сущность, которая помогает решить проблему построения Sql-where-запросов
 */
@Deprecated
public class SqlWhereEntity {
    /**
     * Набор полей и тип их сравнения в запросе.
     * Ключом является название поля, а значением - метод сравнения:
     * Пример: ключ - login, значение - EQUAL (преобразуется в запись "=?")
     * login password cost
     */
    private Map<String, SqlComparatorType> fields;

    /**
     * Переменная, определяющее нужное количество операндов (AND, OR) для запроса
     * Where idAccount=? AND login=? AND password=?
     */
    private int operandCount = -1;

    /**
     * Набор операндов (AND, OR) для построения запросов
     */
    private Queue<SqlOperandType> operands;

    /**
     * Устанавливает значение для поля fields с одновременной проверкой количества значений в Map.
     *
     * @param fields набор полей и тих их сравнения в запросе
     */
    public void setFields(Map<String, SqlComparatorType> fields) {
        this.fields = fields;
        if (fields != null && !fields.isEmpty()) {
            operandCount = fields.size() == 1 ? 0 : fields.size() - 1;
        } else {
            operandCount = -1;
        }
    }

    /**
     * @return набор полей и тип их сравнения в запросе.
     * @throws Exception в случае, если поле operandCount равно -1
     */
    public Map<String, SqlComparatorType> getFields() throws Exception {
        if (operandCount == -1) {
            //TODO THROWS EXCEPTION
        }

        return fields;
    }

    /**
     * @return очередь операндов
     */
    public Queue<SqlOperandType> getOperands() throws Exception {
        if (operands.size() != operandCount) {
            //TODO THROWS EXCEPTION
        }

        return operands;
    }

    /**
     * Устанавливает очередь операндов
     *
     * @param operands очередь операндов
     */
    public void setOperands(Queue<SqlOperandType> operands) {
        this.operands = operands;
    }
}
