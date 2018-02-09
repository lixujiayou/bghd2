package com.bghd.express.utils.tools;

import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包<br>
 * 
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {

		if (input == null || "".equals(input.trim()) || "null".equalsIgnoreCase(input)) {
			return true;
		}

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	
    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmptyStrs(CharSequence... strs) {
        for (CharSequence str : strs) {
            if (isEmpty(str.toString())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }
    
    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(String phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
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
     * 对象转整
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     * 
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     * 
     * @param data
     *            要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     * 
     * @param s
     *            16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    
    
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    
    public static boolean isEnglish(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

	

	/**
	 * 将null字符串转换为空串
	 * 
	 * @param srcStr
	 *            待处理的字符串
	 * @return String 处理的的字符串
	 */
	public static String toVisualString(String srcStr) {
		if (srcStr == null || srcStr.equals("")) {
			return "";
		} else {
			return srcStr;
		}
	}

	/**
	 * 将字段填充到指定的长度
	 * 
	 * @param srcStr
	 *            String 操作字符串
	 * @param length
	 *            int 指定长度
	 * @param withChar
	 *            char 填充的字符
	 * @param isPadToEnd
	 *            boolean 是否填充在字符的尾部 true ：是 ,false:填充在头部
	 * @return String
	 */
	public static String pad(String srcStr, int length, char withChar,
                             boolean isPadToEnd) {
		if (srcStr == null) {
			return null;
		}
		if (srcStr.length() >= length) {
			return srcStr;
		}

		StringBuffer sb = new StringBuffer(srcStr);
		for (int i = 0; i < length - srcStr.length(); i++) {
			if (isPadToEnd) {
				sb.append(withChar);
			} else {
				sb.insert(0, withChar);
			}
		}
		return sb.toString();

	}

	/**
	 * 数字字符串转化为整数
	 * 
	 * @param srcStr
	 *            String 待转化的数字字符串
	 * @param defaultValue
	 *            int 当srcStr为null,空字符串,或者不能转换为数字时返回的缺省值
	 * @return int 返回由数字字符串转化成的数字，当srcStr为空或空字符串时，返回缺省值defaultValue
	 */
	public static int getInt(String srcStr, int defaultValue) {
		if (StringUtils.isEmpty(srcStr))
			return defaultValue;
		int result = defaultValue;
		try {
			Double db = Double.valueOf(srcStr);
			result = db.intValue();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 数字字符串转化为整数，在转化时发生异常，则返回0
	 * 
	 * @param srcStr
	 *            String 待转化的数字字符串
	 * @return int 返回由数字字符串转化成的整数，当srcStr为空或空字符串时，返回0
	 */
	public static int getInt(String srcStr) {
		return getInt(srcStr, 0);
	}

	/**
	 * 由给出的字符串生成唯一的数字
	 * 
	 * @param key
	 * @return
	 */
	public static int oneByOneHash(String key) {
		int hash, i;
		for (hash = 0, i = 0; i < key.length(); ++i) {
			hash += key.charAt(i);
			hash += (hash << 10);
			hash ^= (hash >> 6);
		}
		hash += (hash << 3);
		hash ^= (hash >> 11);
		hash += (hash << 15);
		// return (hash & M_MASK);
		return hash<0?-hash:hash;
	}

	/**
	 *  随机生成9由数字和3个字符组成的字符串
	 * @return
	 */
	public static String randomString() {
		long lseed = System.currentTimeMillis();
		Random random = new Random(lseed);// 设置随机生成种子
		int identifyCodeLength = 6;// 随机字符串长度为6位
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < identifyCodeLength; i++) {
			sb.append(random.nextInt(9));
		}
		// 在生成的字符串中，随机插入三个字符
		for (int i = 0; i < 3; i++) {
			char zimu = (char) ('a' + Math.random() * ('z' - 'a' + 1));
			int num = random.nextInt() % sb.length();
			if (num < 0)
				num = -num;
			sb.insert(num, zimu);
		}
		return sb.toString();
	}
	
	public static byte[] getBytes(String src, Charset charSet) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            try {
                return src.getBytes(charSet.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return src.getBytes(charSet);
        }
    }
	
	public static String replaceBlank(String str) {
		return str.replaceAll("\\s*|\t|\r|\n", str);

	}
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	/**
	 * 返回 yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String toDateString(Date date) {
		return dateFormater.get().format(date);
	}

	/**
	 * 是否全部为数字
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}


	/**
	 * 判断字符串是否为数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * String  转 Data
	 * @return
	 */
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	public static Date stringToDate(String strTime)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}


	/**
	 * date2比date1多的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1,Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if(year1 != year2)   //同一年
		{
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++)
			{
				if(i%4==0 && i%100!=0 || i%400==0)    //闰年
				{
					timeDistance += 366;
				}
				else    //不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2-day1) ;
		}
		else    //不同年
		{
			//System.out.println("判断day2 - day1 : " + (day2-day1));
			return day2-day1;
		}
	}


	/**
	 * 判断哪个日期较近
	 *
	 * return 0: date1近  2错误
	 */
	public static int timeCompare(String date1,String date2){
		//格式化时间
		SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd");
		try {

			Date beginTime=CurrentTime.parse(date1);
			Date endTime=CurrentTime.parse(date2);
			//判断是否大于两天
			if((endTime.getTime() > beginTime.getTime())) {
				return 1;
			}
			return 0;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 2;
		}

	}
}