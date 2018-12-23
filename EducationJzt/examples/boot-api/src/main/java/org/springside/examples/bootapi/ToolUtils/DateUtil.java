package org.springside.examples.bootapi.ToolUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ZhangLei on 2018/12/18 0018
 */
public class DateUtil {

    private static String DATE_TIME_FORMAT="yyyyMMddHHmmss", TIME_FORMAT="HHmmss",DATE_FORMAT="yyyyMMdd";
    private static Logger LogUtil = LoggerFactory.getLogger(DateUtil.class);

    public static double toDouble(String s)
    {
        try
        {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException e)
        {
            LogUtil.error("浮点型转换异常： [" + s + "]");
        }

        return -0.0001D;
    }

    public static String toString(int value)
    {
        DecimalFormat df = new DecimalFormat();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(true);
        df.applyPattern("#,##0");
        return df.format(value);
    }

    public static String toString(long value)
    {
        DecimalFormat df = new DecimalFormat();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(true);
        df.applyPattern("#,##0");
        return df.format(value);
    }

    public static String toString(String value)
    {
        return value;
    }

    public static String toString(long l, int width, char lead)
    {
        if (width > 0)
        {
            char c = lead;
            if (c != '#')
                c = '0';
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < width; ++i)
            {
                sb.append(c);
            }
            NumberFormat formatter = new DecimalFormat(sb.toString());
            String s = formatter.format(l);

            if ((lead != '0') && (c == '0'))
            {
                sb = new StringBuffer(s);
                for (int i = 0; i < sb.length(); ++i)
                {
                    if (sb.charAt(i) != '0')
                        break;
                    sb.setCharAt(i, lead);
                }
                s = sb.toString();
            }
            return s;
        }
        return toString(l);
    }

    public static String toString(int n, int width, char lead)
    {
        return toString(n, width, lead);
    }

    public static String toString(int n, int width)
    {
        return toString(n, width, ' ');
    }

    public static String toHexString(int n, int width)
    {
        StringBuffer sb = new StringBuffer();
        char c = '0';
        String s = Integer.toHexString(n);
        for (int i = 0; i < width - s.length(); ++i)
        {
            sb.append(c);
        }
        sb.append(s);
        s = sb.toString();
        return s;
    }

    public static String toString(double value)
    {
        DecimalFormat df = new DecimalFormat();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(true);
        df.applyPattern("#,##0.00");
        return df.format(value);
    }

    public static String toString(double f, int width, int precision, char lead)
    {
        if ((width > 0) || (precision > 0))
        {
            char c = lead;
            if (c != '#')
                c = '0';
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < width - precision; ++i)
            {
                sb.append(c);
            }
            sb.append('.');
            for (int i = 0; i < precision; ++i)
            {
                sb.append(c);
            }
            NumberFormat formatter = new DecimalFormat(sb.toString());
            String s = formatter.format(f);

            if ((lead != '0') && (c == '0'))
            {
                sb = new StringBuffer(s);
                for (int i = 0; i < sb.length(); ++i)
                {
                    if (sb.charAt(i) != '0')
                        break;
                    sb.setCharAt(i, lead);
                }
                s = sb.toString();
            }
            return s;
        }
        return toString(f);
    }

    public static String toString(double f, int width, int precision)
    {
        return toString(f, width, precision, ' ');
    }

    public static String toString(Double f, int divisor, int precision)
    {
        if (f == null)
        {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("#,##0.");
        for (int i = 0; i < precision; ++i)
        {
            sb.append('0');
        }
        NumberFormat formatter = new DecimalFormat(sb.toString());
        String s = formatter.format(f.doubleValue() / divisor);

        return s;
    }

    public static String toString(double value, String pattern)
    {
        DecimalFormat df = new DecimalFormat();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(true);
        df.applyPattern(pattern);
        return df.format(value);
    }

    public static String toString(java.util.Date date)
    {
        if (date == null)
            return null;
        return toString(date, "yyyy-MM-dd");
    }

    public static String toCode(int n)
    {
        return toString(n, "#");
    }

    public static String toCode(int n, int width)
    {
        return toString(n, width, ' ');
    }

    public static String toCode(int n, int width, char lead)
    {
        return toString(n, width, lead);
    }

    public static String toString(java.sql.Date date, String pattern)
    {
        if (date == null)
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String toString(java.util.Date date, String pattern)
    {
        if (date == null)
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static java.util.Date toDate(String s, String pattern)  throws ParseException
    {
        if (s == null)
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(s);
    }

    /**
     *  根据传入的时间判断格式化的pattern, 长度为14的直接转换；    带-格式化为yyyy-MM-dd；其余的格式化为yyyyMMdd
     * @param s
     * @return
     */
    public static Date toDateWithDateTimeFormat(String s) {
        try {
            String pattern = s.contains(":")||s.length()==14  ? DATE_TIME_FORMAT : s.contains("-") ? "yyyy-MM-dd" : DATE_FORMAT;
            return toDate(s, pattern);
        } catch (Exception e) {
            LogUtil.error("转换日期错误,参数为 " + s, e);
            return null;
        }

    }

    public static String getDateFormat(String s){
        Date d = null;
        try {
            d = toDate(s,"yyyyMMdd");
        } catch (ParseException e) {
            LogUtil.error("转换日期异常" + s, e);
        }
        return toString(d);
    }

    public static String getDateTimeStr(java.util.Date date) {
        return toString(date,DATE_TIME_FORMAT);
    }

    public static String getTodayDateTimeStr() {
        return toString(new Date(),DATE_TIME_FORMAT);
    }

    public static String getTodayTimeStr() {
        return toString(new Date(),TIME_FORMAT);
    }

    public static String getTodayDateStr() {
        return toString(new Date(),DATE_FORMAT);
    }

    public static String getTimeStr(java.util.Date date) {
        return toString(date,TIME_FORMAT);
    }

    public static String getDateStr(java.util.Date date) {
        return toString(date,DATE_FORMAT);
    }

    public static Date trimTime(java.util.Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date maxD = null;
        try {
            maxD = sf.parse(sf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return maxD;
    }

    public static java.sql.Date toDate(String s)
    {
        if (s == null)
            return null;
        String d = s.trim();
        String fmt = null;

        if (d.length() < 6)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(true);
        try
        {
            if (d.length() == 6)
                fmt = "yyMMdd";
            else if (d.charAt(2) == '-')
                fmt = "yy-MM-dd";
            else if (d.charAt(4) == '-')
                fmt = "yyyy-MM-dd";
            else if (d.charAt(4) == '/')
                fmt = "yyyy/MM/dd";
            else if (d.length() == 8) {
                fmt = "yyyyMMdd";
            }
            else {
                fmt = "yyyy.MM.dd";
            }
            if (d.length() > 10)
            {
                fmt = fmt + " HH:mm:ss";
            }
            sdf.applyPattern(fmt);
            return new java.sql.Date(sdf.parse(d).getTime());
        }
        catch (Exception e)
        {
            LogUtil.error("toDate异常",e);
        }
        return null;
    }

    public static java.util.Date toDate(int n) throws ParseException
    {
        return toDate(Integer.toString(n), "yyyyMMdd");
    }

    public static java.sql.Date toDate(java.util.Date date)
    {
        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Date toDate(long date)
    {
        return new java.sql.Date(date);
    }


    /**
     * 两个日期的差 end-start
     * @param smdate 开始日期
     * @param bdate 结束日期
     * @return 结束日期 - 开始日期
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 86400000L;
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 两个日期的差 end-start
     * @param start 开始日期
     * @param end 结束日期
     * @return 结束日期 - 开始日期
     * @throws ParseException
     */
    public static int daysBetween(Date start, Date end) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 86400000L;
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String getMiPre(Date d,int mini){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date afterDate = new Date(d.getTime() + 60000*mini);
        return sdf.format(afterDate);
    }
    /**
     * 起息日期和当前日期的比较
     */
    public static int isMoreThanOneDay(String allowinvesttime) {
        int num = 1;
        try {
            Date now = new Date();
            Date nowTime = toDateWithDateTimeFormat(getDateTimeStr(now));
            Date allowTime = toDateWithDateTimeFormat(allowinvesttime);
            if (Math.ceil(allowTime.getTime() - nowTime.getTime()) <= 0) {
                num = -1;
            } else if ((Math.ceil(allowTime.getTime() - nowTime.getTime()) * 24 * 60 * 60) > 0
                    && Math.ceil((allowTime.getTime() - nowTime.getTime()) * 24 * 60 * 60) < 3600) {
                num = 0;
            }
        } catch (Exception e) {
            LogUtil.error("isMoreThanOneDay异常---------",e);
        }
        return num;
    }
    /**
     * 得到几天前的时间
     * @param d
     * @param day
     * @return
     */
    public static String getDateBefore(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        now.getTime();
        return toString(now.getTime(),DATE_FORMAT);
    }
    public static void main(String[] args) {
        System.out.println(DateUtil.getDateStr(new Date()));
        System.out.println(DateUtil.getDateBefore(new Date(),2));
    }

}
