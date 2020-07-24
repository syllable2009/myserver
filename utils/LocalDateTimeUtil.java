package com;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;


/**
 * @author jiaxiaopeng
 * 2020-04-23 11:23
 **/
@Slf4j
public class LocalDateTimeUtil {


    public static final String DATE_FORMAT_YYYYMM = "yyyy-MM";
    private static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDDHH = "yyyyMMddHH";
    public static final String DATE_FORMAT_YYYYMMDD_HHMM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_YYYYMMDD_HHMMSS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";


    /**
     * 获取时间的格式化字符串
     */
    public static String formatDate(LocalDateTime time, String format) {
        if (Objects.isNull(time) || StringUtils.isEmpty(format)) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(format));
    }

    public static String formatTo_yyyyMMddHHmmss(String time) {
        if (StringUtils.isBlank(time) || time.length() == 19) {
            return time;
        }
        if (time.length() == 10) {
            return time + " 00:00:00";
        }
        return null;
    }

    public static String formatTo_yyyyMMdd(String time) {
        if (StringUtils.isBlank(time) || time.length() <= 10) {
            return time;
        }
        return time.substring(0, 10);
    }

    public static String formatHHmm(LocalDateTime time) {
        if (Objects.isNull(time)) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD_HHMMSS)).substring(11, 16);
    }

    public static LocalDateTime getExpansionTime(LocalDateTime time, int day) {
        if (Objects.isNull(time)) {
            return null;
        }
        return time.plusDays(day);
    }

    public static LocalDateTime formatString(String str, String format) {
        if (StringUtils.isBlank(str) || StringUtils.isEmpty(format)) {
            return null;
        }
        if (format.equals(DATE_FORMAT_YYYYMMDD)) {
            DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern(format);
            LocalDate ld = LocalDate.parse(str, dateformatter);
            return ld.atStartOfDay();
        } else {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(format));
        }
    }

    public static LocalDateTime formatStringToStartOfDay(String str, String format) {
        if (StringUtils.isBlank(str) || StringUtils.isEmpty(format)) {
            return null;
        }
        return LocalDateTimeUtil.getStartOfDay(formatString(str, format));
    }


    public static long toTimeStamp(LocalDateTime dateTime) {
        return dateTime == null ? 0 : dateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static Timestamp toTimeStampObject(LocalDateTime dateTime) {
        return dateTime == null ? null : new Timestamp(dateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
    }


    /**
     * 判断是否为同一天
     */
    private static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth()
                && date1.getDayOfMonth() == date2.getDayOfMonth()) {
            return true;
        }
        return false;
    }

    public static boolean isErrorDateStr(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isEmpty(format)) {
            return true;
        }
        try {
            formatString(dateStr, format);
            return true;
        } catch (Exception e) {
            log.error("is Error DateStr, dataStr :{},format:{}", dateStr, format);
        }
        return false;
    }

    /**
     * LocalDateTime 转为 Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 时分秒归零
     */
    public static LocalDateTime formatYyyyMMdd(LocalDateTime localDateTime) {
        return localDateTime.withHour(0).withMinute(0).withSecond(0);
    }

    /**
     * <通过时间秒毫秒数判断两个时间的间隔的天数>
     */
    public static Integer differentDays(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return (int) Duration.between(start, end).toDays();
        }
        return null;
    }

    public static boolean isErrorStartDateBeforeEndDate(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return true;
        }
        return !startTime.isBefore(endTime) || isSameDay(startTime, endTime);
    }

    public static boolean isStartDateAfterEndDate(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return true;
        }
        return startTime.isAfter(endTime);
    }

    public static Integer getDurationDate(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            int duration = (int) Duration.between(start, end).toDays();
            return duration <= 0 ? 1 : duration;
        }
        return null;
    }

    /**
     * 是否在指定日期范围内，精确到日
     */
    public static boolean isBetweenDate(LocalDateTime outStartDay, LocalDateTime outEndDay, LocalDateTime targetDay) {
        if (outStartDay == null || outEndDay == null || targetDay == null) {
            return false;
        }
        outStartDay = getStartOfDay(outStartDay);
        outEndDay = getEndOfDay(outEndDay);

        return !(targetDay.isBefore(outStartDay) || targetDay.isAfter(outEndDay));
    }

    /**
     * 是否在指定日期范围内，精确到秒
     */
    public static boolean isBetweenDatetime(LocalDateTime outStartDay, LocalDateTime outEndDay,
                                            LocalDateTime targetDay) {
        if (outStartDay == null || outEndDay == null || targetDay == null) {
            return false;
        }
        return (outStartDay.isBefore(targetDay) && outEndDay.isAfter(targetDay));
    }

    public static void checkStartEndDatetimeException(String startDatetime, String endDatetime, String format) {
        LocalDateTime startTime = formatString(startDatetime, format);
        LocalDateTime endTime = formatString(endDatetime, format);

        CheckUtils.checkObjectException(startTime, "开始时间");
        CheckUtils.checkObjectException(endTime, "结束时间");
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("开始时间不能大于结束时间");
        }
    }

    /**
     * 时间校验：开始时间<=结束时间 且时间不能为空
     */
    public static String checkStartEndDatetimeWithErrorMsg(String startDatetime, String endDatetime, String format) {
        try {
            LocalDateTime startTime = formatString(startDatetime, format);
            LocalDateTime endTime = formatString(endDatetime, format);
            if (startTime == null) {
                return "开始时间格式不正确";
            }
            if (endTime == null) {
                return "结束时间格式不正确";
            }
            if (startTime.isAfter(endTime)) {
                return "开始时间不能大于结束时间";
            }
        } catch (Exception e) {
            log.error("字符串时间格式转换错误,startDatetime:{},endDatetime:{}", startDatetime, endDatetime);
            return "日期格式错误";
        }
        return null;
    }

    // 获取某天最大时间 2019-10-15 23:59:59
    public static LocalDateTime getEndOfDay(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.with(LocalTime.MAX);

    }

    // 获取某天最小时间 2019-10-15 00:00:00
    public static LocalDateTime getStartOfDay(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.with(LocalTime.MIN);

    }

    public static void main(String[] args) {

        System.out.println(formatTo_yyyyMMddHHmmss("2020-05-27"));
        System.out.println(formatTo_yyyyMMddHHmmss("2020-05-27 10:00:00"));

        //System.out.println(formatDate(LocalDateTime.now(), DATE_FORMAT_YYYYMMDD));

        //System.out.println(LocalDateTime.now().plusDays(1));
    }
}
