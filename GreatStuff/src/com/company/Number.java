package com.company;

public abstract class Number {
    final int num;
    public Number(final int num) {
        this.num = num;
    }
    abstract public Number add(Number n1);
}
