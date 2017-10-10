package com.extjs.controller;

import com.extjs.model.EQuestionAndInfoVO;
import com.extjs.model.EQuestionInfoDTO;
import com.extjs.model.EQuestionsDTO;
import com.extjs.model.VQuestionandinfo;
import com.extjs.service.EquestionService;
import com.extjs.util.EConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2017/6/29.
 */
@Controller
@RequestMapping("equestions")
public class EquestionController {
    @Autowired
    private EquestionService equestionService;

    @RequestMapping("viewOneQuestion")
    @ResponseBody
    public String queryOneQuestion(String questionID) {
        String question = equestionService.getOneQuestion(questionID);
        return question;
    }

    @RequestMapping("/viewQuestionList")
    @ResponseBody
    public Map queryQuestions(VQuestionandinfo questionandinfo) {
//        URL url=this.getClass().getClassLoader().getResource("/");
//        String uu=request.getSession().getServletContext().getContextPath();
//        uu=request.getSession().getServletContext().getRealPath("/");
////        File dir=new File(".");
////        String url=dir.getPath();
//            String string=request.getRequestURL().toString();
//        EQuestionsDTO questionsDTO=new EQuestionsDTO();
        Map resultMap = new HashMap();
//        List<EQuestionsDTO> questionandinfoList = equestionService.getQuestionList(questionsDTO);
        List<VQuestionandinfo> questionandinfoList = equestionService.getQuestionAndInfoList(questionandinfo);
        resultMap.put("data", questionandinfoList);
        resultMap.put("total", questionandinfoList.size());
        return resultMap;
    }

    @RequestMapping(value = "/modifQuestion", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 根据id查询出当前记录对应的questionid，删除指定questionid的所有记录，重新保存至数据库中（应对一道大题被分成多条记录存储的情况）
     */
    public Map modifQuestion(EQuestionAndInfoVO questionAndInfoVO) {
//        String resultID="";
        Map resultMap = new HashMap();

        EQuestionsDTO mquestionsDTO = new EQuestionsDTO();
        EQuestionInfoDTO mquestionInfoDTO = new EQuestionInfoDTO();
        mquestionsDTO.setQuestionid(questionAndInfoVO.getQuestionid());
        mquestionInfoDTO.setQuestionid(questionAndInfoVO.getQuestionid());
        mquestionsDTO.setQuestion(questionAndInfoVO.getQuestion());
        mquestionInfoDTO.setGradeno(questionAndInfoVO.getGradeno());
        mquestionInfoDTO.setQuestiontype(questionAndInfoVO.getQuestiontype());
        mquestionInfoDTO.setDifficulty(questionAndInfoVO.getDifficulty());
        mquestionInfoDTO.setSubjectno(questionAndInfoVO.getSubjectno());


        try {
            equestionService.updateOneQuestionAndInfo(mquestionsDTO, mquestionInfoDTO);
            resultMap.put("success", true);
            resultMap.put("msg", "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("msg", "添加失败!" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping("/exportQuestions")
    @ResponseBody
    public Map exportQuestions(HttpServletRequest request, VQuestionandinfo questionandinfo) {
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        Map resultMap = new HashMap();

        String path = equestionService.exportToHTML(rootPath, questionandinfo);
        resultMap.put("data", path);
        resultMap.put("total", 1);

        return resultMap;
    }

    //    @RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
//    @ResponseBody
//    public static void downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException, FileNotFoundException {
//        String res = "success";
//        Map resultMap=new HashMap();
//        String tmpFileName = request.getSession().getServletContext().getRealPath("/") + EConstants.exportHtmlPath;
//        if (StringUtils.isEmpty(fileName) && StringUtils.isEmpty(tmpFileName)) {
////            return resultMap;
//        }
//        File file = new File(tmpFileName+"/"+fileName);
//        if (!file.exists()) {
////            return resultMap;
//        }
//        try {
//        String postfix = fileName.substring(fileName.lastIndexOf("."));
//        response.reset();
//        String userAgent = request.getHeader("User-Agent");
//        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
//            response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "utf-8") + postfix + "\"");
//        } else {
//            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1"); // 下载的文件名显示编码处理
//            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + postfix + "\"");
//        }
//        response.setContentType("application/x-msdownload;charset=UTF-8");
//        FileInputStream fis = new FileInputStream(file);
//        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
//
//        byte[] buffer = new byte[2048];
//        int readlength = 0;
//        while ((readlength = fis.read(buffer)) != -1) {
//            bos.write(buffer, 0, readlength);
//        }
//            resultMap.put("data","success");
//            resultMap.put("total",1);
//            fis.close();
//
////            bos.flush();
//            bos.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        return resultMap;
//    }


    @RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 下载文件
     * @param response
     * @param csvFilePath
     *              文件路径
     * @param fileName
     *              文件名称
     * @throws IOException
     */
    public Map exportFile(HttpServletResponse response, HttpServletRequest request, String fileName)
            throws IOException {

        Map resultMap = new HashMap();
        InputStream in = null;
        try {
            String csvFilePath = request.getSession().getServletContext().getRealPath("/") + EConstants.exportHtmlPath;
            response.setContentType("application/csv;charset=GBK");
            response.setHeader("Content-Disposition",
                    "attachment;  filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1"));
            //URLEncoder.encode(fileName, "GBK")



            in = new FileInputStream(csvFilePath + "/" + fileName);
            int len = 0;
            byte[] buffer = new byte[2048];
            response.setCharacterEncoding("GBK");
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                //out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        resultMap.put("data", "success");
        resultMap.put("total", 1);
        return resultMap;
    }
}