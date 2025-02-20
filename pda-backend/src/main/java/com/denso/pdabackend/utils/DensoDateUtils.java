package com.denso.pdabackend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DensoDateUtils {

    
    /**
     * 날짜객체반환
     * <pre>
     * LocalDateTime localDateTime = DateUtils.getDate()
     * </pre>
     * @return LocalDateTime
     * @since 2024-07-07
     * @author 정윤재
     */
    public static LocalDateTime getDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime;
    }

    /**
     * 날짜객체반환(날짜,포맷 지정)
     * <pre>
     * LocalDateTime localDateTime = DateUtils.getDate(2024-12-31,'yyyy-MM-dd')
     * </pre>
     * @param date
     * @param format
     * @return
     */
    public static LocalDateTime getDate(String date,String format){

        LocalDateTime localDateTime = LocalDateTime.parse(date,  DateTimeFormatter.ofPattern(format));
        
        return localDateTime;
    }

    /**
     * 오늘날짜 반환
     * <pre>
     *  int date = DateUtils.today()  //2024-01-01 반환됨.
     * </pre>
     * @return
     * @since 2024-07-07
     * @author 정윤재
     */
    public static String today(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 올해년 반환
     * <pre>
     *  int date = DateUtils.getYear()  //2024 반환됨.
     * </pre>
     * @return
     * @since 2024-07-07
     * @author 정윤재
     */
    public static int getYear(){
        return getDate().getYear();
    }

    /**
     * 이번달 반환
     * <pre>
     *  int date = DateUtils.getYearMonth()  //202411 반환됨.
     * </pre>
     * @return
     * @since 2024-07-07
     * @author 정윤재
     */
    public static String getYearMonth(){
        
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
    }

    /**
     * 기준일 월의 마지막날
     * <pre>
     *  int date = DateUtils.lastDayOfMonth(20241125)  //20241130 반환됨.
     * </pre>
     
     * @param date yyyyMMdd
     * @return
     * @since 2024-07-07
     * @author 정윤재
     */
    public static String lastDayOfMonth(String date){
        LocalDate localDate =  LocalDate.parse(date,DateTimeFormatter.BASIC_ISO_DATE);
        return localDate.withDayOfMonth(localDate.lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 기준일 월의 첫날
     * <pre>
     *  int date = DateUtils.firstDayOfMonth(20241125)  //20241101 반환됨.
     * </pre>
     * 
     * @param date yyyyMMdd
     * @return
     * @since 2024-07-07
     * @author 정윤재
     */
    public static String firstDayOfMonth(String date){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate =  LocalDate.parse(date,inputFormatter);
        return localDate.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
    }

    /**
     * 날짜형식 변환
     * <pre>
     *   int date = DateUtils.parseDate(20241101,"yyyyMMdd","yyyyMM")   //202411 반환됨.
     * </pre>
     * 
     * @param dat 변환 할 날짜
     * @param fromFormat 변환 할 날짜의 포맷
     * @param toFormat  변경할 날짜 포맷
     * @return 변경된 포맷으로 int형으로 날짜 반환
     * 
     * @since 2024-11-07
     * @author 정윤재
     */
    public static String parseDate(String dat,String fromFormat, String toFormat){
        
        if(dat.length() != fromFormat.length()) return dat;
        
        LocalDateTime dateTime = LocalDateTime.parse(dat,DateTimeFormatter.ofPattern(fromFormat));
        String parseDate = dateTime.format(DateTimeFormatter.ofPattern(toFormat));
        return parseDate;
    }
    
    



    
}
