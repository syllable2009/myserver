package com;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * 2020-04-16 09:55
 **/
@Slf4j
public class CtripUtils {

    //携程MD5加密，
    public static String getMD5Hash(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(source.getBytes());
            for (byte b : md5.digest()) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("getMD5Hash error,source:{},{}", source, e);
        }
        return null;
    }

    /**
     * 根据汉子获取firstName和lastName
     */
    public static String transCtripFormatPinyinByNameWithSpace(String chineseName) {
        if (StringUtils.isBlank(chineseName)) {
            return null;
        }
        StringBuilder pinyinName = new StringBuilder();
        char[] chineseNameChars = chineseName.trim().toCharArray();

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        for (int index = 0; index < chineseNameChars.length; index++) {
            char chNameChar = chineseNameChars[index];
            if (chNameChar > 128) {
                try {
                    String[] array = PinyinHelper.toHanyuPinyinStringArray(chNameChar, defaultFormat);
                    pinyinName.append(array[0]);
                    pinyinName.append(index == 0 ? " " : "");
                } catch (Exception e) {
                    log.error("转换汉语拼音错误: {}", e.getMessage(), e);
                }
            } else {
                pinyinName.append(chNameChar == '-' ? "" : chNameChar);
            }
        }

        return pinyinName.toString();
    }

    /**
     * 根据汉子名称转化成拼音名字
     */
    public static String transCtripFormatPinyinByName(String chineseName) {
        if (StringUtils.isBlank(chineseName)) {
            return null;
        }
        StringBuilder pinyinName = new StringBuilder();
        char[] chineseNameChars = chineseName.trim().toCharArray();

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        for (int index = 0; index < chineseNameChars.length; index++) {
            char chNameChar = chineseNameChars[index];
            if (chNameChar > 128) {
                try {
                    String[] array = PinyinHelper.toHanyuPinyinStringArray(chNameChar, defaultFormat);
                    if (Objects.nonNull(array) && array.length > 0) {
                        pinyinName.append(array[0]);
                        pinyinName.append(index == 0 ? "/" : "");
                    }
                } catch (Exception e) {
                    log.error("转换汉语拼音错误: {}", e.getMessage(), e);
                }
            } else {
                pinyinName.append(chNameChar == '-' ? "" : chNameChar);
            }
        }

        return pinyinName.toString();
    }

    /**
     * 根据英文名将名字转化为人名，英文名字
     */
    public static String transCtripFormatEnName(String enName) {
        if (StringUtils.isBlank(enName)) {
            return null;
        }

        String[] nameArray = enName.trim().split("\\s+");
        if (nameArray.length < 2) {
            return enName.trim();
        }

        StringBuilder formatName = new StringBuilder();
        for (int i = nameArray.length - 1; i >= 0; i--) {
            formatName.append(nameArray[i]);
            formatName.append(i == nameArray.length - 1 ? "/" : " ");
        }
        return formatName.toString();
    }

}
