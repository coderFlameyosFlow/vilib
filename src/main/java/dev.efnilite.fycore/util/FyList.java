package dev.efnilite.fycore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FyList<V> extends ArrayList<V> {

    public static <V> List<V> sort(List<V> list) {
        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public static <V> List<V> sort(Set<V> set) {
        return set.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}