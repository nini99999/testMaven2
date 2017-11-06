package com.extjs.util;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jenny on 2017/3/25.
 */
public class EConstants {
    public static final Map<String, String> studyStateMap;//学习状态

    static {
        Map<String, String> m = new HashMap<String, String>();
        m.put("0", "在校");
        m.put("1", "毕业");
        m.put("2", "离校");
        studyStateMap = Collections.unmodifiableMap(m);
    }

    public static final Map<String, String> schoolStateMap;//学籍状态

    static {
        Map<String, String> m = new HashMap<String, String>();
        m.put("0", "正常");
        m.put("1", "转入");
        m.put("2", "休学");
        schoolStateMap = Collections.unmodifiableMap(m);
    }

    public static final Map<String, String> termMap;//学期

    static {
        Map<String, String> term = new HashMap<String, String>();
        term.put("0", "上学期");
        term.put("1", "下学期");
        termMap = Collections.unmodifiableMap(term);
    }

    public static final Map<String, String> examType;//考试类型

    static {
        Map<String, String> exam = new HashMap<String, String>();
        exam.put("0", "日常测验");
        exam.put("1", "周测");
        exam.put("2", "月考");
        exam.put("3", "模考");
        exam.put("4", "期中");
        exam.put("5", "期末");
        exam.put("6", "升学考试");
        examType = Collections.unmodifiableMap(exam);
    }

    public static final Map<Integer, String> convertToChinese;//数字转化为汉字大写

    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "一");
        map.put(2, "二");
        map.put(3, "三");
        map.put(4, "四");
        map.put(5, "五");
        map.put(6, "六");
        map.put(7, "七");
        map.put(8, "八");
        map.put(9, "九");
        map.put(10, "十");
        map.put(11, "十一");
        map.put(12, "十二");
        map.put(13, "十三");
        map.put(14, "十四");
        map.put(15, "十五");
        map.put(16, "十六");
        map.put(17, "十七");
        map.put(18, "十八");
        map.put(19, "十九");
        map.put(20, "二十");
        convertToChinese = Collections.unmodifiableMap(map);
    }

    public static final Integer questionLength = 1800;//题干长度
    public static final String questionTypeIdentifier = "、\u200B&nbsp;";
    /**
     * html文档/xml文档中的题号识别符
     */
    public static final String questionIdentifier = ".\u200B&nbsp;";
    /**
     * 默认图片在服务器上的生成路径（文件夹）
     */
//    public static final String picOutPutPath = "//Users//jenny//downloads//html//pics//";
    public static final String picOutPutPath = "/upLoads/html/pics/";
    /**
     * 默认html在服务器上的生成路径（文件夹）
     */
//    public static final String htmlOutPutPath = "//Users//jenny//downloads//html//";
    public static final String htmlOutPutPath = "/upLoads/html/";
    /**
     * 默认doc上传至服务器的路径（文件夹）
     */
//    public static final String docOutPutPath = "//Users//jenny//downloads//upLoadFiles//";
    public static final String docOutPutPath = "/upLoads/upLoadFiles/";

    public static final String exportHtmlPath = "/exportFiles";//html导出路径

    public static final String[] markArea = {"500-", "500~550", "550~600", "600~650", "650~700", "700+"};
    public static final String[] subjectMarkArea = {"90-", "90~100", "100~110", "110~120", "120~130", "130+"};
    public static final String[] nation = {"汉族", "壮族", "满族", "回族", "苗族", "维吾尔族", "土家族", "彝族", "蒙古族", "藏族", "布依族", "侗族", "瑶族", "朝鲜族", "白族", "哈尼族",
            "哈萨克族", "黎族", "傣族", "畲族", "傈僳族", "仡佬族", "东乡族", "高山族", "拉祜族", "水族", "佤族", "纳西族", "羌族", "土族", "仫佬族", "锡伯族",
            "柯尔克孜族", "达斡尔族", "景颇族", "毛南族", "撒拉族", "布朗族", "塔吉克族", "阿昌族", "普米族", "鄂温克族", "怒族", "京族", "基诺族", "德昂族", "保安族",
            "俄罗斯族", "裕固族", "乌孜别克族", "门巴族", "鄂伦春族", "独龙族", "塔塔尔族", "赫哲族", "珞巴族"};

}
