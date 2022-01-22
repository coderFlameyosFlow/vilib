package dev.efnilite.fycore.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FyMap<K, V> extends HashMap<K, V> {

    public Map<K, V> sortKeys() {
        return entrySet()
                .stream()
                .sorted()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
