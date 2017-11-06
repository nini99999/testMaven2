package com.extjs.util;

/**
 * Created by jenny on 2017/6/4.
 */

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

public class Dom4JforXML {

    public static void main(String[] args) throws Exception {

//        List<String> questionList = new Dom4JforXML().getQuestionList("333.html");

    }

    /**
     * 获取指定文件中的body内容，根据body内容获取所有试题类型的集合，每道试题对应集合中的每条记录，返回试题类型（含试题信息)的集合
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public List<List<String>> getPaperQuestionType(String rootPath, String fileName, Map<Integer, String> map) throws Exception {
        List<List<String>> result = new ArrayList<>();

        List<String> questionTypeList = new ArrayList<>();//试题类型，包含试题
        List<String> bodyList = this.getBodyList(rootPath, fileName);

        List<String> oneTypeQuestionList;
        for (int i = 1; i < map.size(); i++) {
            String string = this.getOneQuestion(bodyList, map.get(i), map.get(i + 1));
            if (null != string && string.length() > 0) {
                questionTypeList.add(string);
            }
        }
//        List<String> stringList = this.getPaperQuestionList(qusetionTypeList, bodyList.size());
//        return stringList;


        for (String string : questionTypeList) {
            oneTypeQuestionList = new ArrayList<>();
            oneTypeQuestionList = this.getPaperQuestionsBySpecify(string, bodyList.size());
            result.add(oneTypeQuestionList);
        }
        return result;
    }

//    /**
//     * 根据试题类型（含试题信息），拆分出每道试题
//     *
//     * @param questionTypeList 试题类型（含试题信息
//     * @param count            body中的element总数量
//     * @return
//     * @throws Exception
//     */
//    public List getPaperQuestionList(List<String> questionTypeList, Integer count) throws Exception {
//        List<String> resultList = new ArrayList<String>();
//        List<String> cList;
////        int i = 0;
//        for (String string : questionTypeList) {
//            cList = new ArrayList<String>();
////            i++;
////            String string = this.getOneQuestion(questionTypeList,
////                    String.valueOf(i) + EConstants.questionIdentifier, String.valueOf(i + 1) + EConstants.questionIdentifier).replace(String.valueOf(i) + EConstants.questionIdentifier, "");
//            cList = this.getPaperQuestionsBySpecify(string, count);
//            resultList.addAll(cList);
//        }
//        return resultList;
//    }

    /**
     * 获取每种试题类型的试题集合
     *
     * @param string 指定试题类型（含试题信息)
     * @param count
     * @return
     */
    public List<String> getPaperQuestionsBySpecify(String string, Integer count) {
        List<String> resultList = new ArrayList<String>();
        String str;
        for (int i = 1; i < count; i++) {
            str = "";
            if (string.indexOf(String.valueOf(i) + EConstants.questionIdentifier) > -1 && string.indexOf(String.valueOf(i + 1) + EConstants.questionIdentifier) > -1) {

                str = string.substring(string.indexOf(String.valueOf(i) + EConstants.questionIdentifier), string.indexOf(String.valueOf(i + 1) + EConstants.questionIdentifier));
            }
            if (string.indexOf(String.valueOf(i) + EConstants.questionIdentifier) > -1 && string.indexOf(String.valueOf(i + 1) + EConstants.questionIdentifier) == -1) {

                str = string.substring(string.indexOf(String.valueOf(i) + EConstants.questionIdentifier), string.length());
            }
            if (null != str && str.length() > 0) {
                resultList.add(str.replace(String.valueOf(i) + EConstants.questionIdentifier, ""));
            }
        }
        return resultList;
    }

    private List<String> getBodyList(String rootPath, String fileName) throws Exception {

        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(new File(rootPath + EConstants.htmlOutPutPath + fileName));
        //获取根节点元素对象
        Element root = document.getRootElement();

        Element body = root.element("body");

        List<String> bodyList = this.listBodyNodes(body);

        return bodyList;
    }

    /**
     * 获取指定文件中的body内容，根据body内容获取试题集合，每道试题对应集合中的每条记录，返回试题集合
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public List<String> getQuestionList(String rootPath, String fileName) throws Exception {
        List<String> resultList = new ArrayList<String>();
        List<String> bodyList = this.getBodyList(rootPath, fileName);
        for (int i = 1; i < bodyList.size() + 1; i++) {
            String s = this.getOneQuestion(bodyList,
                    String.valueOf(i) + EConstants.questionIdentifier, String.valueOf(i + 1) + EConstants.questionIdentifier).replace(String.valueOf(i) + EConstants.questionIdentifier, "");

            if (s != null && s.length() > 0) {
                resultList.add(s);
                System.out.println(s);
            }

        }
        return resultList;

    }



    /*
    * 取一个节点下面的所有节点（包括自己）生成string
    * */

    private void listNodes(Element node, StringBuilder xml) {


        if (!"img".equals(node.getName())) {//说明不是图片
            xml.append("<" + node.getName() + ">");
        } else {
            //如果是图片，把图片的attribute属性填充上（如src,style）
            xml.append("<" + node.getName());
            for (int count = 0; count < node.attributeCount(); count++) {
                xml.append(" " + node.attribute(count).getName() + "=\"" + node.attribute(count).getText() + "\"");
            }
            xml.append(">");
        }
        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            listNodes(e, xml);
        }

        if (!(node.getTextTrim().equals(""))) {
            xml.append(node.getText());
        }

        xml.append("</" + node.getName() + ">");

    }

    /*
    * 遍历body节点下的一级节点，生成字符串集合
    * */

    private List<String> listBodyNodes(Element node) {

        List<String> list = new ArrayList<String>();

        Iterator<Element> iterator = node.elementIterator();

        while (iterator.hasNext()) {
            Element e = iterator.next();

            StringBuilder xml = new StringBuilder();
            listNodes(e, xml);
            list.add(xml.toString());
        }

        return list;

    }

    /**
     * 获取一道试题
     *
     * @param list
     * @param begin
     * @param end
     * @return 试题字符串（含完整题干、选项、图片等）
     */
    private String getOneQuestion(List<String> list, String begin, String end) {

        StringBuilder sb = new StringBuilder();
        boolean run = false;
        for (String string : list) {

            if (string.indexOf(end) > -1) {
                run = false;
                break;//如果找到匹配的题号（1.,2.之类），则结束循环，否则会把（11.,12.,21.22.之类）的内容重复附加

            }

            if (string.indexOf(begin) > -1) {
                run = true;

            }
            if (run) {
                sb.append(string);
            }

        }

        return sb.toString();

    }


}