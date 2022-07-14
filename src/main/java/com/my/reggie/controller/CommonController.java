package com.my.reggie.controller;

import com.my.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/*
 *文件上传下载
 * */
@RestController
@RequestMapping("/common")
public class CommonController {
    /*
     * el表达式，properties的路径
     * */
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    /*
     *file是临时文件，所以要储存起来
     * */
    public R<String> upload(MultipartFile file) {
        /*
         * 获取文件名
         * */
        //获取文件名
        String originalFileName = file.getOriginalFilename();
        //动态获取文件后缀，如”jpg“
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        //使用UUID生成文件名，防止覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建目录
        File dir = new File(basePath);
        //判断文件是否存在
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            /*
             * 转存路径
             * */
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /*
     * 文件下载
     * */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流，读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流写回浏览器，浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            //响应类型
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            //
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //传输完毕关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
