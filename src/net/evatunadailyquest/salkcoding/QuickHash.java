package net.evatunadailyquest.salkcoding;


import java.util.Arrays;
import java.util.HashSet;

public class QuickHash<T> extends HashSet<T> {

    public QuickHash(T... Value) {
        super(Value.length);
        this.addAll(Arrays.asList(Value));
    }

}

