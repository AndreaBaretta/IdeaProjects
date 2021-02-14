package com.company;

public class IntegerNumber extends Number {
    public IntegerNumber(final int num) {
        super(num);
    }

    public Number add(final Number n) {
        return new IntegerNumber(num+n.num);
    }
}
