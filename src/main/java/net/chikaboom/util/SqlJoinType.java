package net.chikaboom.util;

/**
 * Перечесление типов присоединений sql.
 */
public enum SqlJoinType {
    LEFT_JOIN {{ setValue(" LEFT ");}},
    RIGHT_JOIN {{ setValue(" RIGHT ");}},
    INNER_JOIN {{ setValue(" INNER ");}},
    LEFT_OUTER_JOIN {{ setValue(" LEFT OUTER ");}},
    RIGHT_OUTER_JOIN {{ setValue(" RIGHT OUTER ");}},
    NONE_JOIN {{ setValue("");}};

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value + "JOIN ";
    }
}
