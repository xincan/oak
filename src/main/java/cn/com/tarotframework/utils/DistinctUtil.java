package cn.com.tarotframework.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 公共去重类
 */
public class DistinctUtil {

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
