package com.extjs.util;

import org.hibernate.Session;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Created by jenny on 2017/10/2.
 */
public class ExportToHtml {
    public String exportToHtml(HttpServletResponse response, StringBuilder content, String url) {
        EConstants constants = new EConstants();

        StringBuilder stringBuilder = new StringBuilder("");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = userDetails.getUsername() + "export" + formatter.format(date) + ".html";

//        String path = rootPath + constants.exportHtmlPath + "/" + userDetails.getUsername() + "export" + formatter.format(date) + ".html";


        try {
            //创建文件
//            File file = new File(path);
//
//
//            if (!file.exists()) {
//
//                file.createNewFile();
////                file.mkdirs();
//            }
//            //打开文件
//            PrintStream printStream = new PrintStream(new FileOutputStream(path));
            String imgUrl = content.toString().replaceAll("/UEditor", url + "/UEditor");//替换UEditor生成的图片URL地址
            //输入HTML文件内容
            stringBuilder.append("<html><head>");
            stringBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
            stringBuilder.append("<title>");
            stringBuilder.append(userDetails.getUsername() + "导出试题" + date);

            stringBuilder.append("</title>");
            stringBuilder.append("</head>");
            stringBuilder.append("<body>");
            stringBuilder.append("<div>");
            stringBuilder.append(imgUrl.replaceAll("../upLoads", url + "upLoads"));//替换upLoads上传的地址（word导入时生成的图片）
            stringBuilder.append("</div>");
            stringBuilder.append("</body></html>");

//            //将HTML文件内容写入文件中
//            printStream.println(stringBuilder.toString());
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            OutputStream os = response.getOutputStream();
            os.write(stringBuilder.toString().getBytes());
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }
}
