package com.extjs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by jenny on 2017/6/6.
 */
public class RegexString {
    /**
     * 正则查找字符串中的<img **>,并将其结尾补充</img>
     *
     * @param inputString
     * @return
     */
    public String fillImgEndTag(String inputString,String endTag,String regex) throws PatternSyntaxException {

//        String imgEndTag = "</img>";
//        String regex = "<img.*?>";//正则匹配表达式，匹配对象为<img **>
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            inputString = inputString.replace(matcher.group(), matcher.group() + endTag);
//            System.out.println(matcher.group()+imgEndTag);
        }

//        System.out.println(inputString);
        return inputString;
    }
//    /**
//     * 正则替换
//     */
//    public String regexReplace(String inputString,String befor,String end,String regex){
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(inputString);
//        while (matcher.find()){
//            inputString = inputString.replace(matcher.group(), );
//        }
//    }
}
