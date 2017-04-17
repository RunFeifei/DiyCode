package util;

/**
 * Created by Lei Guoting on 16-9-30.
 */
public class Bytes {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private Bytes() {/*hide*/}

    public static boolean bytes2Boolean(byte[] bytes, int offset) {
        if (bytes == null || bytes.length == 0 || offset < 0 || offset >= bytes.length) {
            return false;
        }
        return bytes[offset] != 0;
    }

    public static long bytes2Long(byte[] bytes, int offset) {
        if (bytes == null || bytes.length == 0 || offset < 0 || offset + 8 > bytes.length) {
            return 0;
        }

        return ((((long) bytes[offset] & 0XFF) << 56)
                | (((long) bytes[offset + 1] & 0XFF) << 48)
                | (((long) bytes[offset + 2] & 0XFF) << 40)
                | (((long) bytes[offset + 3] & 0XFF) << 32)
                | (((long) bytes[offset + 4] & 0XFF) << 24)
                | (((long) bytes[offset + 5] & 0XFF) << 16)
                | (((long) bytes[offset + 6] & 0XFF) << 8)
                | (((long) bytes[offset + 7] & 0XFF) << 0));
    }

    public static byte[] long2Bytes(long source) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((source >> 56) & 0xFF);
        bytes[1] = (byte) ((source >> 48) & 0xFF);
        bytes[2] = (byte) ((source >> 40) & 0xFF);
        bytes[3] = (byte) ((source >> 32) & 0xFF);
        bytes[4] = (byte) ((source >> 24) & 0xFF);
        bytes[5] = (byte) ((source >> 16) & 0xFF);
        bytes[6] = (byte) ((source >> 8) & 0xFF);
        bytes[7] = (byte) ((source >> 0) & 0xFF);
        return bytes;
    }

    public static boolean long2Bytes(long source, byte[] bytes, int offset) {
        if (bytes == null || offset + 8 > bytes.length) {
            return false;
        }

        bytes[offset + 0] = (byte) ((source >> 56) & 0xFF);
        bytes[offset + 1] = (byte) ((source >> 48) & 0xFF);
        bytes[offset + 2] = (byte) ((source >> 40) & 0xFF);
        bytes[offset + 3] = (byte) ((source >> 32) & 0xFF);
        bytes[offset + 4] = (byte) ((source >> 24) & 0xFF);
        bytes[offset + 5] = (byte) ((source >> 16) & 0xFF);
        bytes[offset + 6] = (byte) ((source >> 8) & 0xFF);
        bytes[offset + 7] = (byte) ((source >> 0) & 0xFF);
        return true;
    }

    public static int bytes2Int(byte[] bytes, int offset) {
        if (bytes == null || bytes.length == 0 || offset < 0 || offset + 4 > bytes.length) {
            return 0;
        }

        return ((((int) bytes[offset] & 0XFF) << 24)
                | (((int) bytes[offset + 1] & 0XFF) << 16)
                | (((int) bytes[offset + 2] & 0XFF) << 8)
                | (((int) bytes[offset + 3] & 0XFF) << 0));
    }

    public static byte[] int2Bytes(int source) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((source >> 24) & 0xFF);
        bytes[1] = (byte) ((source >> 16) & 0xFF);
        bytes[2] = (byte) ((source >> 8) & 0xFF);
        bytes[3] = (byte) ((source >> 0) & 0xFF);
        return bytes;
    }

    public static boolean int2Bytes(int source, byte[] bytes, int offset) {
        if (bytes == null || offset + 4 > bytes.length) {
            return false;
        }

        bytes[offset + 0] = (byte) ((source >> 24) & 0xFF);
        bytes[offset + 1] = (byte) ((source >> 16) & 0xFF);
        bytes[offset + 2] = (byte) ((source >> 8) & 0xFF);
        bytes[offset + 3] = (byte) ((source >> 0) & 0xFF);
        return true;
    }

    public static short bytes2Short(byte[] bytes, int offset) {
        if (bytes == null || bytes.length == 0 || offset < 0 || offset + 2 > bytes.length) {
            return 0;
        }

        return (short) ((((short) bytes[offset] & 0XFF) << 8) | (((short) bytes[offset + 1] & 0XFF) << 0));
    }

    public static byte[] short2Bytes(short source) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((source >> 8) & 0xFF);
        bytes[1] = (byte) ((source >> 0) & 0xFF);
        return bytes;
    }

    public static boolean short2Bytes(short source, byte[] bytes, int offset) {
        if (bytes == null || offset + 2 > bytes.length) {
            return false;
        }

        bytes[offset + 0] = (byte) ((source >> 8) & 0xFF);
        bytes[offset + 1] = (byte) ((source >> 0) & 0xFF);
        return true;
    }

    public static double bytes2Double(byte[] bytes, int offset) {
        if (bytes == null || bytes.length == 0 || offset < 0 || offset + 8 > bytes.length) {
            return 0;
        }

        return Double.longBitsToDouble(bytes2Long(bytes, offset));
    }

    public static float bytes2Float(byte[] bytes, int offset) {
        if (bytes == null || bytes.length == 0 || offset < 0 || offset + 4 > bytes.length) {
            return 0;
        }
        return Float.intBitsToFloat(bytes2Int(bytes, offset));
    }

    public static byte[] hexString2Bytes(String source) {
        if (source == null || source.length() == 0) {
            return null;
        }

        int length = source.length();
        if (length % 2 != 0) {
            return null;
        }

        char highChar;
        char lowChar;
        byte[] bytes = new byte[length / 2];
        for (int i = 0, index = 0; i < length; i += 2, index++) {
            highChar = source.charAt(i);
            lowChar = source.charAt(i + 1);
            if (highChar >= '0' && highChar <= '9') {
                highChar -= '0';
            } else if (highChar >= 'A' && highChar <= 'F') {
                highChar = (char) (highChar - 'A' + 10);
            } else if (highChar >= 'a' && highChar <= 'f') {
                highChar = (char) (highChar - 'a' + 10);
            } else {
                return null;
            }

            if (lowChar >= '0' && lowChar <= '9') {
                lowChar -= '0';
            } else if (lowChar >= 'A' && lowChar <= 'F') {
                lowChar = (char) (lowChar - 'A' + 10);
            } else if (lowChar >= 'a' && lowChar <= 'f') {
                lowChar = (char) (lowChar - 'a' + 10);
            } else {
                return null;
            }
            bytes[index] = (byte) ((lowChar & 0x0F) | ((highChar << 4) & 0xF0));
        }
        return bytes;
    }

    public static boolean hexString2Bytes(String source, byte[] bytes, int offset) {
        if (source == null || source.length() == 0 || bytes == null || bytes.length == 0) {
            return false;
        }

        int length = source.length();
        if (length % 2 != 0) {
            return false;
        }


        final int bytesSize = length / 2;
        if (offset + bytesSize > bytes.length) {
            return false;
        }

        char highChar;
        char lowChar;
        for (int i = 0, index = offset; i < length; i += 2, index++) {
            highChar = source.charAt(i);
            lowChar = source.charAt(i + 1);
            if (highChar >= '0' && highChar <= '9') {
                highChar -= '0';
            } else if (highChar >= 'A' && highChar <= 'F') {
                highChar = (char) (highChar - 'A' + 10);
            } else if (highChar >= 'a' && highChar <= 'f') {
                highChar = (char) (highChar - 'a' + 10);
            } else {
                return false;
            }

            if (lowChar >= '0' && lowChar <= '9') {
                lowChar -= '0';
            } else if (lowChar >= 'A' && lowChar <= 'F') {
                lowChar = (char) (lowChar - 'A' + 10);
            } else if (lowChar >= 'a' && lowChar <= 'f') {
                lowChar = (char) (lowChar - 'a' + 10);
            } else {
                return false;
            }
            bytes[index] = (byte) ((lowChar & 0x0F) | ((highChar << 4) & 0xF0));
        }
        return true;
    }

    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        } else {
            return bytes2HexString(bytes, 0, bytes.length);
        }
    }

    public static String bytes2HexString(byte[] bytes, int offset, int size) {
        if (bytes == null || offset < 0 || size <= 0) {
            return "";
        }

        final int length = bytes.length;
        if (length == 0 || offset + size > length) {
            return "";
        }

        byte byt;
        final int lastIndex = offset + size;
        final char[] hexes = new char[length * 2];
        for (int i = offset, index = 0; i < lastIndex; i++) {
            byt = bytes[i];
            hexes[index++] = DIGITS[(byt & 0XF0) >> 4];
            hexes[index++] = DIGITS[byt & 0X0F];
        }
        return new String(hexes);
    }

    public static int compares(byte[] bs1, byte[] bs2) {
        if (bs1.length < bs2.length) {
            return -1;
        } else if (bs1.length > bs2.length) {
            return 1;
        } else {
            for (int i = 0; i < bs1.length; ++i) {
                if (bs1[i] < bs2[i]) {
                    return -1;
                } else if (bs1[i] > bs2[i]) {
                    return 1;
                }
            }
            return 0;
        }
    }

    public static boolean isAllMatch(byte[] bytes, byte key) {
        if (bytes == null) {
            return false;
        }

        if (bytes.length == 0) {
            return false;
        }

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != key) {
                return false;
            }
        }
        return true;
    }

    public static boolean reset(byte[] bytes){
        if(bytes == null || bytes.length == 0){
            return false;
        }

        byte reset = (byte) 0;
        java.util.Arrays.fill(bytes, reset);
        return true;
    }
}
