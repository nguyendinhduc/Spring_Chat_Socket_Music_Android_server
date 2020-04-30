package com.t3h.server.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
public class FileController {
    @PostMapping(value = "/api/uploadImage")
    public Object updateImage(
            @RequestParam MultipartFile multipartFile
            ){
        try {
            String imageName = multipartFile.getOriginalFilename();
            String path = "image";
            new File(path).mkdirs();
            String fullPath = path+File.separator+imageName;
            InputStream in = multipartFile.getInputStream();
            FileOutputStream out = new FileOutputStream(fullPath);
            byte[] b = new byte[1024];
            int le = in.read(b);
            while (le >=0){
                out.write(b, 0, le);
                le = in.read(b);
            }
            in.close();
            out.close();
            return "api/getImage?name="+imageName;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "dfd";
    }

    @GetMapping(value = "/api/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@RequestParam(value = "name") String name) throws IOException {
        String path = "image" + File.separator+name;
        FileInputStream in = new FileInputStream(path);
        byte b[] = new byte[in.available()];
        in.read(b);
        in.close();
        return b;
    }
}
