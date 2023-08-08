package net.chikaboom.util.constant;

/**
 * Описывает существующие роли в системе
 */
public enum ApplicationRole {
    ROLE_MASTER(1),
    ROLE_CLIENT(2);

    private final int val;

    ApplicationRole(int v) {
        val = v;
    }

    public int getValue() {
        return val;
    }
}
