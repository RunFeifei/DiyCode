package util;

import java.util.regex.Pattern;

/**
 * Created by Lei Guoting on 16-9-30.
 */
public class Strings {
    public static final String EMPTY = "";

    private Strings() {/*hide*/}

    public static boolean isEmpty(CharSequence source) {
        return source == null || source.length() == 0;
    }

    public static boolean isBlank(CharSequence source) {
        return isEmpty(source) || isEmpty(source.toString().trim());
    }

    public static String null2Empty(String source) {
        if (source == null) {
            return EMPTY;
        } else {
            return source;
        }
    }

    public static int length(String source) {
        if (isEmpty(source)) {
            return 0;
        } else {
            return source.length();
        }
    }

    public static boolean isHexString(CharSequence source) {
        if (isEmpty(source)) {
            return false;
        }

        int length = source.length();
        if (length % 2 != 0) {
            return false;
        }

        char chr;
        for (int i = 0; i < length; i++) {
            chr = source.charAt(i);
            if (!((chr >= '0' && chr <= '9') || (chr >= 'A' && chr <= 'F') || (chr >= 'a' && chr <= 'f'))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEqual(CharSequence str1, CharSequence str2) {
        return equals(str1, str2);
    }

    public static boolean equals(CharSequence str1, CharSequence str2) {
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

    public static boolean equalsIgnoreCase(String str1, String str2) {
        boolean handled = false;
        if (str1 == str2) {
            handled = true;
        } else if (str1 != null) {
            handled = str1.equalsIgnoreCase(str2);
        } else if (str2 != null) {
            handled = str2.equalsIgnoreCase(str1);
        }
        return handled;
    }

    public static int compares(String str1, String str2) {
        int handled;
        if (str1 == str2) {
            handled = 0;
        } else if (str1 == null && str2 != null) {
            handled = -1;
        } else if (str1 != null && str2 == null) {
            handled = 1;
        } else {
            handled = str1.compareTo(str2);
        }
        return handled;
    }

    public static int comparesIgnoreCase(String str1, String str2) {
        int handled;
        if (str1 == str2) {
            handled = 0;
        } else if (str1 == null && str2 != null) {
            handled = -1;
        } else if (str1 != null && str2 == null) {
            handled = 1;
        } else {
            handled = str1.compareToIgnoreCase(str2);
        }
        return handled;
    }

    public static boolean isChineseName(String userName) {
        if (isEmpty(userName)) {
            return false;
        }
        Pattern p = Pattern.compile("(^[\u4e00-\u9fa5][\u4e00-\u9fa5·]{0,30}[\u4e00-\u9fa5]$|[\u4e00-\u9fa5])");
        return p.matcher(userName).matches();
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
