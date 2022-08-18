package com.web.controller;

import com.web.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Utilities;
import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${common.path}")
    String basePath;
    @PostMapping("/upload")
    R<String> upload(MultipartFile file) throws IOException {

        File file1 = new File(basePath);
        if (!file1.exists())
        {
            file1.mkdir();
        }

        String uuid = UUID.randomUUID().toString();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName=uuid+suffix;
        file.transferTo(new File(basePath+fileName));

        return R.success(fileName,"上传成功");
    }
    @GetMapping("/download")
    void  download(String name, HttpServletResponse response) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(basePath+name);

        ServletOutputStream outputStream = response.getOutputStream();


        int len=0;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }


        fileInputStream.close();
        outputStream.close();


    }
}
