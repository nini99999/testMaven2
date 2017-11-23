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
    public String fillImgEndTag(String inputString, String endTag, String regex) throws PatternSyntaxException {

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

    /**
     * 删除string中的html元素
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    /**
     * 去掉字符串里面的html代码。<br>
     * 要求数据要规范，比如大于小于号要配套,否则会被集体误杀。
     *
     * @param content
     * @return
     */
    public static String stripHtml(String content) {
// <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
// <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
// 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
// 还原HTML
// content = HTMLDecoder.decode(content);
        return content;
    }
}

