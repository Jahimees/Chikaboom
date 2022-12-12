package net.chikaboom.util.constant;

public enum ApplicationRole {
    MASTER(1),
    CLIENT(2);

    private final int val;
    private ApplicationRole(int v) { val = v; }
    public int getValue() { return val; }
}
