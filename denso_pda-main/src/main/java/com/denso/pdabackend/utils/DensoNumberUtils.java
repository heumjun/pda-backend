package com.denso.pdabackend.utils;

/**
 * 숫자형 관련 util class
 */

public final class DensoNumberUtils {

    /**
     * int형 숫자 내림함수
     * <pre>
     *  getFloorNumber(1235,1) = 1230
     *  getFloorNumber(1235,2) = 1200
     * </pre>
     * @param digit 내림할 숫주
     * @param len   내림할 자리수
     * @return int
     * @since 2024-12-03
     * @author 정윤재
     */
    public static int getFloorNumber(int digit, int len){

        if(digit < 0 || len < 0) return digit;

        len = (int)Math.pow(10,len);  //제곱근
        return (digit/len) * len;

    }

    /**
     * long형 숫자 내림함수
     * <pre>
     *  getFloorNumber(1235,1) = 1230
     *  getFloorNumber(1235,2) = 1200
     * </pre>
     * @param digit 내림할 숫자
     * @param len   내림할 자리수
     * @return long
     * @since 2024-12-03
     * @author 정윤재
     */

    public static long getFloorNumber(long digit, int len){
        if(digit < 0 || len < 0) return digit;

        len = (int)Math.pow(10,len);  //제곱근
        return (digit/len) * len;
        
    }        

    /**
     * double형 숫자 내림함수
     * <pre>
     *  getFloorNumber(1235.12345,1) = 1230.0
     *  getFloorNumber(1235.12545,-3) = 1235.12
     * </pre>
     * @param digit 내림할 숫자
     * @param len   내림할 자리수(마이너스일경우 소수점자리)
     * @return double
     * @since 2024-12-03
     * @author 정윤재
     */
    public static double getFloorNumber(double digit, int len){

        if(digit < 0) return digit;

        if(len > 0){
            len = (int)Math.pow(10,len);  //제곱근
            return Math.floor(digit/len) * len;
        }else{
            len = (int)Math.pow(10, -len -1);
            return Math.floor(digit*len)/len;
        }

    }

    /**
     * int형 숫자 반올림
     * <pre>
     *  getRoundNumber(1235,1) = 1240
     *  getRoundNumber(1235,2) = 1200
     * </pre>
     * @param digit 반올림할 숫자
     * @param len   반올림할 자리수(마이너스일경우 소수점자리)
     * @return int
     * @since 2024-12-03
     * @author 정윤재
     */
    public static int getRoundNumber(int digit, int len){

        if(digit < 0 || len < 0) return digit;

        double factor = Math.pow(10, len);
        return (int) (Math.round(digit / factor) * factor);
        
    }

    /**
     * long형 숫자 반올림
     * <pre>
     *  getRoundNumber(1235,1) = 1240
     *  getRoundNumber(1235,2) = 1200
     * </pre>
     * @param digit 반올림할 숫자
     * @param len   반올림할 자리수(마이너스일경우 소수점자리)
     * @return long
     * @since 2024-12-03
     * @author 정윤재
     */
    public static long getRoundNumber(long digit, int len){

        if(digit < 0 || len < 0) return digit;

        double factor = Math.pow(10, len);
        return (long) (Math.round(digit / factor) * factor);
        
    }

    /**
     * double형 숫자 반올림
     * <pre>
     *  getRoundNumber(1235.15345,1) = 1240.0
     *  getRoundNumber(1235.15545,-3) = 1235.16
     * </pre>
     * @param digit 반올림할 숫주
     * @param len   반올림할 자리수(마이너스일경우 소수점자리)
     * @return double
     * @since 2024-12-03
     * @author 정윤재
     */
    public static double getRoundNumber(double digit, int len){

        if(digit < 0) return digit;

        if(len > 0){
            double factor = Math.pow(10, len);
            return Math.round(digit / factor) * factor;
        }else{
            double factor = Math.pow(10, -len -1);
            return Math.round(digit * factor) / factor;
        }
        
    }


    /**
     * int형 숫자 올림
     * <pre>
     *  getRoundNumber(1235,1) = 1240
     *  getRoundNumber(1235,2) = 1300
     * </pre>
     * @param digit 올림할 숫주
     * @param len   올림할 자리수
     * @return double
     * @since 2024-12-03
     * @author 정윤재
     */
    public static int getCeilNumber(int digit, int len){

        if(digit < 0 || len < 0) return digit;

        double factor = Math.pow(10, len);
        return (int) (Math.ceil(digit / factor) * factor);
        
    }

    
    /**
     * long형 숫자 올림
     * <pre>
     *  getRoundNumber(1235,1) = 1240
     *  getRoundNumber(1235,2) = 1300
     * </pre>
     * @param digit 올림할 숫자
     * @param len   올림할 자리수
     * @return double
     * @since 2024-12-03
     * @author 정윤재
     */
    public static long getCeilNumber(long digit, int len){

        if(digit < 0 || len < 0) return digit;

        double factor = Math.pow(10, len);
        return (long) (Math.round(digit / factor) * factor);
        
    }

    /**
     * double형 숫자 올림
     * <pre>
     *  getRoundNumber(1231.15345,1) = 1240
     *  getRoundNumber(1235.15545,-4) = 1235.156
     * </pre>
     * @param digit 올림할 숫자
     * @param len   올림할 자리수(마이너스일경우 소수점자리)
     * @return double
     * @since 2024-12-03
     * @author 정윤재
     */
    public static double getCeilNumber(double digit, int len){

        if(digit < 0) return digit;

        if(len > 0){
            double factor = Math.pow(10, len);
            return Math.ceil(digit / factor) * factor;
        }else{
            double factor = Math.pow(10, -len -1);
            return Math.ceil(digit * factor) / factor;
        }
        
    }


}
