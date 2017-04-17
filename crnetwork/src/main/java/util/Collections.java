package util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Lei Guoting on 16-10-10.
 */

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

    public static String toString(List<String> list, String separator) {
        return toString(list, separator, true);
    }

    public static String toString(List<String> list, String separator, boolean isStrict) {
        StringBuilder builder = new StringBuilder();
        toString(list, builder, separator, isStrict);
        return builder.toString();
    }

    public static void toString(List<String> list, StringBuilder builder, String separator) {
        toString(list, builder, separator, true);
    }

    public static void toString(List<String> list, StringBuilder builder, String separator, boolean isStrict) {
        if (list == null || list.isEmpty()) {
            return;
        }

        String spr = separator;
        if (Strings.isEmpty(spr)) {
            spr = Strings.null2Empty(spr);
        }
        if (isStrict) {
            for (String str : list) {
                if (!Strings.isEmpty(str)) {
                    builder.append(str).append(spr);
                }
            }
        } else {
            for (String str : list) {
                builder.append(Strings.null2Empty(str)).append(spr);
            }
        }

        if (!Strings.isEmpty(spr)) {
            int sprLen = separator.length();
            int end = builder.length();
            int start = end - sprLen;
            builder.replace(start, end, Strings.EMPTY);
        }
    }
}
