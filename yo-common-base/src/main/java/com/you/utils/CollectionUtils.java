package com.you.utils;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类（统一出口，便于切换）
 * @author Michael Liu
 * @create 2020-02-29 23:07
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {
    /**
     * 判断集合是否为null或空白集合
     * @param collection 集合
     * @return 为null或空白集合返回true，其他返回false
     */
    static boolean isEmptyCollection(@Nullable Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断map是否为null或空白map
     * @param map
     * @return 为null或空白map返回true，其他返回false
     */
    static boolean isEmptyMap(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
