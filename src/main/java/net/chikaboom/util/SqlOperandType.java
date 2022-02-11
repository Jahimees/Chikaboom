package net.chikaboom.util;

//TODO не поддерживает скобки -> f1=? AND (f2=3 OR f3=4)

/**
 * Перечисление типов операндов sql.
 */
public enum SqlOperandType {
    AND {{ setValue(" AND ");}},
    OR {{ setValue(" OR ");}};

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
