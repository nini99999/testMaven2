package com.extjs.util;

/**
 * Created by jenny on 2017/6/4.
 */

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dom4JforXML {

    public static void main(String[] args) throws Exception {

//        List<String> questionList = new Dom4JforXML().getQuestionList("333.html");

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
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(new File(rootPath + EConstants.htmlOutPutPath + fileName));
        //获取根节点元素对象
        Element root = document.getRootElement();

        Element body = root.element("body");

        List<String> bodyList = this.listBodyNodes(body);
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