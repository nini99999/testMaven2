package com.extjs.util;

/**
 * Created by jenny on 2017/5/30.
 */

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.Date;
import java.util.List;


public class PoiWordToHtml {

//    public static void main(String argv[]) {
//        try {
//            String inputFilePath = "//Users//jenny//downloads//xxxxxx.doc";
//            String outputFilePath = EConstants.htmlOutPutPath + "3233.html";
//            String outputPicPath = EConstants.picOutPutPath;
//            new PoiWordToHtml().convert2Html(inputFilePath, outputFilePath, outputPicPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 写文件
     *
     * @param rootPath     工程根目录
     * @param content      内容
     * @param pathWithName 服务器端文件的完整路径（含文件名）
     */
    private static void writeFile(String content, String pathWithName, String rootPath) {

//        String sss = content.substring(content.indexOf("<body"));//只获取<body>***</body>中的内容
//        String xxx = sss.substring(0, sss.length() - 8).replace(".emf", ".png");//去掉结尾行中的</html>,并将.emf替换为.png
        String xxx = content.replace(".emf", ".png");
//        xxx = xxx.replace("&", "&amp;").replace(rootPath + EConstants.htmlOutPutPath, "");
        //设置字符串中图片的相对路径
        xxx = xxx.replace("&", "&amp;").replace(rootPath, "..");
//        System.out.println(xxx);
        //处理图片:word转换时，没有生成图片结尾，因此需补全</img>
        RegexString regexString = new RegexString();
        String finalString = regexString.fillImgEndTag(xxx, "</img>", "<img.*?>");//正则补全<img>
        finalString = regexString.fillImgEndTag(finalString, "</META>", "<META.*?>");
        finalString = regexString.fillImgEndTag(finalString, "</meta>", "<meta.*?>");

//        System.out.println("------------" + finalString);

        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(pathWithName);
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
            bw.write(finalString);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * @param rootPath      工程根目录
     * @param fileName      服务器端输入文件的完整路径（含文件名）
     * @param outPutFile    服务器端输出文件的完整路径（含文件名）
     * @param outputPicPath 服务器端保存图片的完整路径
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public void convert2Html(String fileName, String outPutFile, String outputPicPath, String rootPath)
            throws TransformerException, IOException,
            ParserConfigurationException {
        //文件写入时，按照当天日期创建图片保存的文件夹
        Date date = new Date(System.currentTimeMillis());
        String dateString = date.toString() + "/";
        long ctime = System.currentTimeMillis();

        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));//WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        //设置图片的生成位置
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content,
                                      PictureType pictureType, String suggestedName,
                                      float widthInches, float heightInches) {
                return outputPicPath + dateString + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        //判断当天日期文件夹是否存在，不存在则创建之
        File dateFolder = new File(outputPicPath + date.toString());
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }

        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");

        serializer.transform(domSource, streamResult);

        String finalOutstr = new String(out.toByteArray());

/**
 * 生成图片，如果图片格式为emf，则将.emf转化为.png格式图片
 *生成文件名规则：userid+时间戳+1000*(顺序号，从第10000张开始，最大支持一个文档中包含9999张图片),--尚未添加用户名--
 */
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                try {
                    String picName = String.valueOf(ctime) + "z" + String.valueOf(10000 + i) + ".";
                    String picPath = outputPicPath + dateString + picName + pic.suggestPictureType().name().toLowerCase();

                    pic.writeImageContent(new FileOutputStream(picPath));
                    if ("EMF".equals(String.valueOf(pic.suggestPictureType()).toUpperCase())) {
//                        System.out.println(String.valueOf(i) + "---" + pic.suggestFullFileName());
                        EMFReader emfReader = new EMFReader();
//                        String outputPicName = outputPicPath + dateString + pic.suggestFullFileName();
                        emfReader.convertEMFToPNG(picPath, picPath.replace("emf", "png"));
                    }

                    finalOutstr = finalOutstr.replaceFirst(pic.suggestFullFileName(), picName + "png");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        out.close();
        writeFile(finalOutstr, outPutFile, rootPath);
    }
}