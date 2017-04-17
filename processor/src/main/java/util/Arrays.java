package util;


import java.util.ArrayList;

/**
 * Created by Lei Guoting on 16-9-30.
 */
public class Arrays {
    private Arrays() {/*hide*/}

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static int length(Object[] array) {
        return isEmpty(array) ? 0 : array.length;
    }

    public static boolean copy(Object[] source, int srcOffset, Object[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static boolean copy(byte[] source, int srcOffset, byte[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static boolean copy(int[] source, int srcOffset, int[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static boolean copy(float[] source, int srcOffset, float[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static boolean copy(double[] source, int srcOffset, double[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static boolean copy(long[] source, int srcOffset, long[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static boolean copy(short[] source, int srcOffset, short[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static boolean copy(char[] source, int srcOffset, char[] dest, int destOffset, int size) {
        if (isEmpty(source)) {
            return false;
        }

        if (isEmpty(dest)) {
            return false;
        }

        if (srcOffset < 0 || destOffset < 0) {
            return false;
        }

        if ((srcOffset + size) > source.length || (destOffset + size) > dest.length) {
            return false;
        }

        System.arraycopy(source, srcOffset, dest, destOffset, size);
        return true;
    }

    public static <T> ArrayList<T> asArrayList(T[] array) {
        int size = length(array);
        ArrayList<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    public static <T> boolean isInclude(T t1, T... ts) {
        if (t1 == null || isEmpty(ts)) {
            return false;
        }
        for (T t : ts) {
            if (t != null && t1.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T > boolean isMatchAll(T t1, T... ts) {
        if (t1 == null || isEmpty(ts)) {
            return false;
        }
        boolean eaual = true;
        for (T t : ts) {
            if (t != null && t1.equals(t)) {
                eaual = eaual & t1.equals(t);
            }
        }
        return eaual;
    }
}
