package com.feifei.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class Collections {
    private Collections() {/*hide*/}

    public static boolean isEmpty(Collection<?> source) {
        return source == null || source.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static int size(Collection<?> source) {
        return isEmpty(source) ? 0 : source.size();
    }

    public static void toString(List<String> list,String separator) {
        if (list == null || list.isEmpty()) {
            return;
        }
        String spr = separator;
        if (Strings.isEmpty(spr)) {
            spr = "";
        }
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            if (!Strings.isEmpty(str)) {
                builder.append(str).append(spr);
            }
        }

        if (!Strings.isEmpty(spr)) {
            int sprLen = separator.length();
            int end = builder.length();
            int start = end - sprLen;
            builder.replace(start, end, "");
        }
    }
}
