package it.swiftelink.com.vcs_doctor.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.swiftelink.com.common.app.Application;
import it.swiftelink.com.vcs_doctor.R;

public class DateUtils {
    // 日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM = "yyyy-MM";
    public static final String FORMAT_YYYY = "yyyy";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String FORMAT_MM_SS = "mm:ss";
    public static final String FORMAT_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String FORMAT_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYY2MM2DD = "yyyy.MM.dd";
    public static final String FORMAT_YYYY2MM2DD_HH_MM = "yyyy.MM.dd HH:mm";
    public static final String FORMAT_MMCDD_HH_MM = "MM月dd日 HH:mm";
    public static final String FORMAT_MMCDD = "MM月dd日";
    public static final String FORMAT_YYYYCMMCDD = "yyyy-MM";

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;

    //判断选择的日期是否是本周（分从周日开始和从周一开始两种方式）
    public static boolean isThisWeek(Date time) {
//        //周日开始计算
//      Calendar calendar = Calendar.getInstance();

        //周一开始计算
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        calendar.setTime(time);
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    //获取当前时间
    public static String getDate() {
        Date date = new Date();
        String time = date.toLocaleString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YYYY_MM);
        return dateFormat.format(date);
    }

    //获取当前时间
    public static String getDateFormat(String dateFormatStr) {
        Date date = new Date();
        String time = date.toLocaleString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        return dateFormat.format(date);
    }


    //判断选择的日期是否是今天
    public static boolean isToday(Date time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    //判断选择的日期是否是本月
    public static boolean isThisMonth(Date time) {
        return isThisTime(time, "yyyy-MM");
    }

    //判断选择的日期是否是本月
    public static boolean isThisYear(Date time) {
        return isThisTime(time, "yyyy");
    }

    //判断选择的日期是否是昨天
    public static boolean isYesterDay(Date time) {
        Calendar cal = Calendar.getInstance();
        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        if ((ct - lt) == 1) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isThisTime(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        String param = sdf.format(date);//参数时间

        String now = sdf.format(new Date());//当前时间

        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    /**
     * 获取某年某月有多少天
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取两个时间相差的天数
     *
     * @return time1 - time2相差的天数
     */
    public static int getDayOffset(long time1, long time2) {
        // 将小的时间置为当天的0点
        long offsetTime;
        if (time1 > time2) {
            offsetTime = time1 - getDayStartTime(getCalendar(time2)).getTimeInMillis();
        } else {
            offsetTime = getDayStartTime(getCalendar(time1)).getTimeInMillis() - time2;
        }
        return (int) (offsetTime / ONE_DAY);
    }

    public static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    public static Calendar getDayStartTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String getDurationInString(long time) {
        String durStr = "";
        if (time == 0) {
            return "0" + Application.getInstance().getString(R.string.label_sec);
        }
        time = time / 1000;
        long hour = time / (60 * 60);
        time = time - (60 * 60) * hour;
        long min = time / 60;
        time = time - 60 * min;
        long sec = time;
        if (hour != 0) {
            durStr = hour + Application.getInstance().getString(R.string.label_h) + min + Application.getInstance().getString(R.string.label_min) + sec + Application.getInstance().getString(R.string.label_sec);
        } else if (min != 0) {
            durStr = min + Application.getInstance().getString(R.string.label_min) + sec + Application.getInstance().getString(R.string.label_sec);
        } else {
            durStr = sec + Application.getInstance().getString(R.string.label_sec);
        }
        return durStr;
    }


    public static String getDurationInStringHMS(long time) {
        String durStr = "";
        time = time / 1000;
        long hour = time / (60 * 60);
        time = time - (60 * 60) * hour;
        long min = time / 60;
        time = time - 60 * min;
        long sec = time;
        if (hour != 0) {
            durStr = hour + "h" + min + "m";
        } else if (min != 0) {
            durStr = min + "m";
        } else {
            durStr = sec + "s";
        }
        return durStr;
    }
    public static String getDurationInStringHMS2(Context context, long time) {
        String durStr = "";
        Log.i("lqi","time: "+time);
        time = time / 1000;
        long hour = time / (60 * 60);
        Log.i("lqi","hour: "+time);
        time = time - (60 * 60) * hour;
        long min = time / 60;
        time = time - 60 * min;
        long sec = time;
        if (hour != 0) {
            durStr = hour + context.getString(R.string.hour_str) + min + context.getString(R.string.min_dec_str);
        } else if (min != 0) {
            durStr = min + context.getString(R.string.min_dec_str);
        } else {
            durStr = sec + context.getString(R.string.secone_dec_str);
        }
        return durStr;
    }

    public static String getDurationInStringH(Context context, long time) {
        String durStr = "";
        Log.i("lqi","time: "+time);
        time = time / 1000;
        long hour = time / (60 * 60);
        Log.i("lqi","hour: "+time);
        time = time - (60 * 60) * hour;
        long min = time / 60;
        if (hour != 0) {
            if(min >= 30){
                hour = hour + 1;
            }
            durStr = hour + context.getString(R.string.hour_str);
        }
        return durStr;
    }

    /**
     * 获取当前时间是星期几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();

        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convertToLong(String date, String format) {
        try {
            if (!"date".equals("") && "date" != null) {
                if (TextUtils.isEmpty(format)) {
                    format = TIME_FORMAT;
                }
                SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
                return formatter.parse(date).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @return
     */
    public static String convertToString(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(time);
        return formatter.format(date);
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @return
     */
    public static Date convertToDate(String time, String format) {
        Date date = null;
        try {
            if (time != null && !"".equals(time)) {
                SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
                date = formatter.parse(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @return
     */
    public static String convertToYMString(long time) {
        if (time > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYYY_MM, Locale.getDefault());
            Date date = new Date(time);
            return formatter.format(date);
        }
        return "";

    }


    /**
     * 获取当前时间年月
     *
     * @return
     */
    public static String getDateYM() {
        Date date = new Date();
        String time = date.toLocaleString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YYYYCMMCDD);
        return dateFormat.format(date);
    }


    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @return
     */
    public static String convertToStringYM(long time) {
        if (time > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYYYCMMCDD, Locale.getDefault());
            Date date = new Date(time);
            return formatter.format(date);
        }
        return "";

    }


}
