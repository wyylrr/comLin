package com.sxjs.common.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 字符串操作工具类
 */

public class StringUtils {

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String notNull(String s) {
        return isEmpty(s) ? "" : s;
    }

    public static String formatDouble(double value) {
        BigDecimal b2 = new BigDecimal(value);
        b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(b2.doubleValue());
    }

    /**
     * 描述：标准化日期时间类型的数据，不足两位的补0.
     *
     * @param dateTime 预格式的时间字符串，如:2012-3-2 12:2:20
     * @return String 格式化好的时间字符串，如:2012-03-20 12:02:20
     */
    public static String dateTimeFormat(String dateTime) {
        StringBuilder sb = new StringBuilder();
        try {
            if (isEmpty(dateTime)) {
                return null;
            }
            String[] dateAndTime = dateTime.split(" ");
            if (dateAndTime.length > 0) {
                for (String str : dateAndTime) {
                    if (str.indexOf("-") != -1) {
                        String[] date = str.split("-");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append("-");
                            }
                        }
                    } else if (str.indexOf(":") != -1) {
                        sb.append(" ");
                        String[] date = str.split(":");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append(":");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    /**
     * 描述：不足2个字符的在前面补“0”.
     *
     * @param str 指定的字符串
     * @return 至少2个字符的字符串
     */
    public static String strFormat2(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return 字符串的长度（中文字符计2个）
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if (temp.matches(chinese)) {
                    // 中文字符长度为2
                    valueLength += 2;
                } else {
                    // 其他字符长度为1
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    public static String Min2BitString(int num) {
        return num > 9 ? String.valueOf(num) : ("0" + num);
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    public static String urlRemoveParameter(String url, String name) {
        if (!isEmpty(url) && !isEmpty(name)) {
            int index = url.indexOf(name + "=");
            if (index != -1) {
                StringBuilder sb = new StringBuilder();
                sb.append(url.substring(0, index - 1));
                int idx = url.indexOf("&", index);
                if (idx != -1) {
                    sb.append(url.substring(idx));
                }
                url = sb.toString();
            }

        }
        return url;
    }


    public static String URLRequest(String strUrlParam, String key) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        arrSplit = strUrlParam.split("[&]");

        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }

        if (mapRequest.containsKey(key)) {
            return mapRequest.get(key);
        }
        return "";
    }

    /***
     * 获取url 指定name的value;
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }


    public static int str2int(String text) {
        int myNum = 0;

        try {
            myNum = Integer.parseInt(text);
        } catch (Exception nfe) {
            nfe.getStackTrace();
        }
        return myNum;
    }


    public static double str2double(String text) {
        double myNum = 0;

        try {
            myNum = Double.parseDouble(text);
        } catch (Exception nfe) {
            nfe.getStackTrace();
        }
        return myNum;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * @author yjy
     * @date 2016年9月18日上午9:26:24
     */
    public static String insertSpace4(String str) {
        try {
            String input = str;
            String regex = "(.{4})";
            input = input.replaceAll(regex, "$1 ");
            return input;
        } catch (Exception e) {
            return str;
        }
    }

    public static String minTwoChar(int num) {
        if (num > 9) {
            return String.valueOf(num);
        } else {
            return "0" + String.valueOf(num);
        }

    }


    public static Map<String, String> uRLRequestParam(String strUrlParam) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }
    //long数据格式化
    public static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     * @param ss
     * @return
     */
    public static String subZeroAndDot(double ss){
        String s= String.valueOf(ss);
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 计算字符个数，一个中文两个字符
     *
     * @param content
     * @return
     */
    public static int getCharacterSize(String content){
        if (TextUtils.isEmpty(content)){
            return 0;
        }
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < content.length(); i++) {
            /* 获取一个字符 */
            String temp = content.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 划中划线
     *
     * @param textView /
     */
    public static void middleLine(TextView textView){
        if (textView == null || textView.getPaint() == null){
            return;
        }
        // 抗锯齿
        textView.getPaint().setAntiAlias(true);
        // 中划线
        textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 消除中划线
     *
     * @param textView /
     */
    public static void removeMiddleLine(TextView textView){
        if (textView == null || textView.getPaint() == null){
            return;
        }
        textView.getPaint().setFlags(0);
    }

    /**
     * 设置字体颜色
     *
     * @param textView
     * @param colorStr
     */
    public static void setTextColor(TextView textView, String colorStr){
        if (textView == null || TextUtils.isEmpty(colorStr)){
            return;
        }
        try{
            textView.setTextColor(Color.parseColor(colorStr));
        }catch (Exception e){
            // do nothing
        }
    }

    /**
     * 设置文字
     *
     * @param textView /
     * @param content /
     * @param isReset 是否重置为空
     */
    public static void setText(TextView textView, String content, boolean isReset){
        if (TextUtils.isEmpty(content)){
            if (isReset){
                textView.setText("");
            }
        }else {
            textView.setText(content);
        }
    }

    public static void setRelativeSizeString(TextView textView, String content, int start, int end, float multiParam){
        try{
            SpannableString spannableString = new SpannableString(content);
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(multiParam);
            spannableString.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }catch (Exception e){
            // do nothing
        }
    }

    public static String join(int[] arr, String delimiter) {

        if (arr.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i : arr) {
            sb.append(delimiter).append(Integer.toString(i));
        }

        return sb.substring(delimiter.length());

    }

    public static String join(String delimiter, String[] strings) {

        if (strings.length == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strings) {
            stringBuilder.append(delimiter).append(str);
        }

        return stringBuilder.substring(delimiter.length());
    }

    public static String join(String delimiter, ArrayList<Integer> strings) {

        if (strings.size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Integer str : strings) {
            stringBuilder.append(delimiter).append(str.toString());
        }

        return stringBuilder.substring(delimiter.length());
    }

    public static String joinStr(String delimiter, List<String> strings) {

        if (strings.size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strings) {
            stringBuilder.append(delimiter).append(str.toString());
        }

        return stringBuilder.substring(delimiter.length());
    }

    public static String byteToString(byte[] digest) {
        String str = "";
        String tempStr = "";
        for (int i = 0; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            }
            else {
                str = str + tempStr;
            }
        }
        return str.toLowerCase();
    }
}

