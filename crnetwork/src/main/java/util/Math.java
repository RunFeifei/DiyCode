package util;


/**
 * Created by Lei Guoting on 16-10-26.
 */
public class Math {

    /**
     * @param source 原数字
     * @param deciCount 保留小数的位数
     * @return 四舍五入后的数字
     *
     * e.g.
     * round(1.238, 1);
     * 输出: 1.2
     *
     * round(1.258, 1);
     * 输出: 1.3
     */
    public static double round(double source, int deciCount) {
        if (deciCount < 0) {
            return source;
        }

        if (deciCount == 0) {
            return java.lang.Math.round(source);
        }

        return java.lang.Math.round(source * java.lang.Math.pow(10, deciCount)) * java.lang.Math.pow(0.1D, deciCount);
    }

    /**
     * @param source 原数字
     * @param deciCount 保留小数的位数
     * @return
     *
     * e.g.
     * floor(1.258, 1);
     * 输出: 1.2
     */
    public static double floor(double source, int deciCount) {
        if (deciCount < 0) {
            return source;
        }

        if (deciCount == 0) {
            return java.lang.Math.floor(source);
        }

        return java.lang.Math.floor(source * java.lang.Math.pow(10, deciCount)) * java.lang.Math.pow(0.1D, deciCount);
    }

    /**
     * @param source 原数字
     * @param deciCount 保留小数的位数
     * @return
     *
     * e.g.
     * ceil(1.238, 1);
     * 输出: 1.3
     */
    public static double ceil(double source, int deciCount){
        if (deciCount < 0) {
            return source;
        }

        if (deciCount == 0) {
            return java.lang.Math.ceil(source);
        }

        return java.lang.Math.ceil(source * java.lang.Math.pow(10, deciCount)) * java.lang.Math.pow(0.1D, deciCount);
    }
}
