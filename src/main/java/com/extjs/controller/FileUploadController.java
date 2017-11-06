package com.extjs.controller;

import com.alibaba.fastjson.JSON;

import com.extjs.model.EPaperQTypeDTO;
import com.extjs.model.EPaperQuestionsDTO;
import com.extjs.model.VPaperQuestionAndInfo;
import com.extjs.service.EpaperQTypeService;
import com.extjs.service.EpaperQuestionService;
import com.extjs.service.EquestionService;
import com.extjs.util.Dom4JforXML;
import com.extjs.util.EConstants;
import com.extjs.util.PoiWordToHtml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.sql.Date;
import java.util.*;

/**
 * Created by jenny on 2017/6/7.
 */
@Controller
@RequestMapping("fileUpload")
public class FileUploadController {
    @Autowired
    private EquestionService equestionService;
    @Autowired
    private EpaperQuestionService epaperQuestionService;

    @Autowired
    private EpaperQTypeService epaperQTypeService;

    @RequestMapping("paperQuestionUpload")
    @ResponseBody
    /**
     *试卷试题表导入
     */
    public String paperQuestionUpload(HttpServletRequest request, @RequestParam("subjectno") String subjectno, @RequestParam("gradeno") String gradeno, @RequestParam("paperid") String paperid) throws Exception {

//       List<EPaperQuestionsDTO> paperQuestionsDTOList=epaperQuestionService.
        EPaperQTypeDTO paperQTypeDTO = new EPaperQTypeDTO();
        paperQTypeDTO.setTpno(paperid);
        List<EPaperQTypeDTO> paperQTypeDTOList = epaperQTypeService.queryEpaperQType(paperQTypeDTO);
        Map map = new HashMap<>();
        int i = 0;
        String typeName = "";
        for (EPaperQTypeDTO ePaperQTypeDTO : paperQTypeDTOList) {
            i++;
//            map.put(i,"[" +EConstants.convertToChinese.get(i) + ".]" + ePaperQTypeDTO.getQuestiontypename());
            typeName = "[" + String.valueOf(i) + "]." + ePaperQTypeDTO.getQuestiontypename();
            ePaperQTypeDTO.setQuestiontypename(typeName);
            map.put(i, typeName);
        }
        List<String> questionByType = new ArrayList<>();

        String rootPath = request.getSession().getServletContext().getRealPath("/");
        Date date = new Date(System.currentTimeMillis());
        //判断当天日期文件夹是否存在，不存在则创建之
        File dateFolder = new File(rootPath + EConstants.docOutPutPath + date.toString());
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }
        long startTime = System.currentTimeMillis();
        String htmlOutPutPathWithName = rootPath + EConstants.htmlOutPutPath + startTime + ".html";
        File htmlOutPutPathFolder = new File(rootPath + EConstants.htmlOutPutPath);
        if (!htmlOutPutPathFolder.exists()) {
            htmlOutPutPathFolder.mkdirs();
        }

        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        ServletContext servletContext = request.getSession().getServletContext();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);

        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {

                    String path = dateFolder.getPath() + "/" + String.valueOf(startTime) + file.getOriginalFilename();
                    //上传
                    file.transferTo(new File(path));
                    //服务器端word生成html
                    PoiWordToHtml poiWordToHtml = new PoiWordToHtml();
                    poiWordToHtml.convert2Html(path, htmlOutPutPathWithName, rootPath + EConstants.picOutPutPath, rootPath);

                    //解析html，并生成试题
                    Dom4JforXML dom4JforXML = new Dom4JforXML();

                    List<List<String>> questionTypeList = dom4JforXML.getPaperQuestionType(rootPath, startTime + ".html", map);

//                    loop questionList,插入至数据库试卷试题表、试题信息表中
                    VPaperQuestionAndInfo paperQuestionAndInfo;
                    int j = 0;
                    for (List<String> list : questionTypeList) {
                        j++;

                        String questionType = paperQTypeDTOList.get(j).getQuestiontype();
//                        int m = 0;
                        for (String string : list) {
                            paperQuestionAndInfo = new VPaperQuestionAndInfo();
                            paperQuestionAndInfo.setQuestion(string);
                            paperQuestionAndInfo.setQuestiontype(questionType);
                            paperQuestionAndInfo.setPaperid(paperid);
                            paperQuestionAndInfo.setGradeno(gradeno);
                            paperQuestionAndInfo.setSubjectno(subjectno);
                            epaperQuestionService.addPaperQuestionAndInfo(paperQuestionAndInfo);
                        }

//                        equestionService.addOneQuestionAndInfo(string, gradeno, subjectno, questionType, Float.parseFloat("0"));
//                        dom4JforXML.getPaperQuestionsBySpecify(string, map.size());
                    }
                }

            }

        }

        long endTime = System.currentTimeMillis();
        System.out.println("方法运行时间：" + String.valueOf(endTime - startTime) + "ms");
        return JSON.toJSONString("Congratulations,all things have done！");
    }

    @RequestMapping("springUpload")
    @ResponseBody
    /**
     * 基础题库表导入
     */
    public String springUpload(HttpServletRequest request, @RequestParam("subjectno") String subjectno, @RequestParam("gradeno") String gradeno, @RequestParam("questiontype") String questionType) throws Exception {
//        String gradeno = request.getParameter("gradeno");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        Date date = new Date(System.currentTimeMillis());
        //判断当天日期文件夹是否存在，不存在则创建之
        File dateFolder = new File(rootPath + EConstants.docOutPutPath + date.toString());
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }
        long startTime = System.currentTimeMillis();
        String htmlOutPutPathWithName = rootPath + EConstants.htmlOutPutPath + startTime + ".html";
        File htmlOutPutPathFolder = new File(rootPath + EConstants.htmlOutPutPath);
        if (!htmlOutPutPathFolder.exists()) {
            htmlOutPutPathFolder.mkdirs();
        }

        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        ServletContext servletContext = request.getSession().getServletContext();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);

        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {

                    String path = dateFolder.getPath() + "/" + String.valueOf(startTime) + file.getOriginalFilename();
                    //上传
                    file.transferTo(new File(path));
                    //服务器端word生成html
                    PoiWordToHtml poiWordToHtml = new PoiWordToHtml();
                    poiWordToHtml.convert2Html(path, htmlOutPutPathWithName, rootPath + EConstants.picOutPutPath, rootPath);

                    //解析html，并生成试题
                    Dom4JforXML dom4JforXML = new Dom4JforXML();
                    List<String> questionList = dom4JforXML.getQuestionList(rootPath, startTime + ".html");
                    //loop questionList,插入至数据库基础试题库表、试题信息表中
                    for (String string : questionList) {
                        equestionService.addOneQuestionAndInfo(string, gradeno, subjectno, questionType, Float.parseFloat("0"));
                    }
                }

            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("方法运行时间：" + String.valueOf(endTime - startTime) + "ms");
        return JSON.toJSONString("Congratulations,all things have done！");
    }
}
