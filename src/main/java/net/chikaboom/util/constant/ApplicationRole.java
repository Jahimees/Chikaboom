package net.chikaboom.util.constant;

/**
 * Описывает существующие роли в системе
 */
public enum ApplicationRole {
    MASTER(1),
    CLIENT(2);

    private final int val;

    ApplicationRole(int v) {
        val = v;
    }

    public int getValue() {
        return val;
    }
}
