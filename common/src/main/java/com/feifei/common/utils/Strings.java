package com.feifei.common.utils;

public class Strings {

    private Strings() {
        assert true:"不支持调用私有构造函数";
    }

    public static boolean isEmpty(CharSequence source) {
        return source == null || source.length() == 0;
    }

    public static boolean isBlank(CharSequence source) {
        return isEmpty(source) || isEmpty(source.toString().trim());
    }

    public static boolean isEqual(CharSequence str1, CharSequence str2) {
        boolean handled = false;
        if (str1 == str2) {
            handled = true;
        } else if (str1 != null) {
            handled = str1.equals(str2);
        } else if (str2 != null) {
            handled = str2.equals(str1);
        }
        return handled;
    }

    public static String trim(String string) {
        if (isEmpty(string)) {
            return "";
        }
        return string.trim();
    }

    public static String append(String originalStr, String newStr) {
        if (isEmpty(originalStr)) {
            originalStr = "";
        }
        if (isEmpty(newStr)) {
            newStr = "";
        }
        return new StringBuilder(originalStr).append(newStr).toString();
    }

}
