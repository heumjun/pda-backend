package com.denso.pdabackend.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public final class MapUtils {

	public static int size(Map<?, ?> target) {
        Validate.notNull(target, "Cannot get map size of null");
        return target.size();
    }

    public static boolean isEmpty(Map<?, ?> target) {
        return target == null || target.isEmpty();
    }

    public static <X> boolean containsKey(Map<? super X, ?> target, X key) {
        Validate.notNull(target, "Cannot execute map containsKey: target is null");
        return target.containsKey(key);
    }

    public static <X> boolean containsAllKeys(Map<? super X, ?> target, X[] keys) {
        Validate.notNull(target, "Cannot execute map containsAllKeys: target is null");
        Validate.notNull(keys, "Cannot execute map containsAllKeys: keys is null");
        return containsAllKeys(target, (Collection)Arrays.asList(keys));
    }

    public static <X> boolean containsAllKeys(Map<? super X, ?> target, Collection<X> keys) {
        Validate.notNull(target, "Cannot execute map containsAllKeys: target is null");
        Validate.notNull(keys, "Cannot execute map containsAllKeys: keys is null");
        return target.keySet().containsAll(keys);
    }

    public static <X> boolean containsValue(Map<?, ? super X> target, X value) {
        Validate.notNull(target, "Cannot execute map containsValue: target is null");
        return target.containsValue(value);
    }

    public static <X> boolean containsAllValues(Map<?, ? super X> target, X[] values) {
        Validate.notNull(target, "Cannot execute map containsAllValues: target is null");
        Validate.notNull(values, "Cannot execute map containsAllValues: values is null");
        return containsAllValues(target, (Collection)Arrays.asList(values));
    }

    public static <X> boolean containsAllValues(Map<?, ? super X> target, Collection<X> values) {
        Validate.notNull(target, "Cannot execute map containsAllValues: target is null");
        Validate.notNull(values, "Cannot execute map containsAllValues: values is null");
        return target.values().containsAll(values);
    }

    private MapUtils() {
    }
    
}
