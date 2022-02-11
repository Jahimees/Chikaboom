package net.chikaboom.util;

/**
 * Перечесление сравнений-sql.
 */
public enum SqlComparatorType {
    EQUAL {{ setValue("=?");}},
    NOT_EQUAL {{ setValue("!=?");}},
    GREATER {{ setValue(">?");}},
    LESS {{ setValue("<?");}},
    GREATER_OR_EQUAL {{ setValue(">=?");}},
    LESS_OR_EQUAL {{ setValue("<=?");}},
    IS_NULL {{ setValue(" IS NULL");}},
    IS_NOT_NULL {{ setValue(" IS NOT NULL");}};

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
