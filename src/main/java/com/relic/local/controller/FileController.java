package com.relic.local.controller;

import com.relic.local.entity.FileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Relic
 * @desc
 * @date 2019-10-19 15:11
 */
@Slf4j
@Controller
public class FileController {

    @RequestMapping("/{path}")
    public String list(@PathVariable String path, Model model, HttpServletResponse response) throws IOException {
        List<FileEntity> fileList = new ArrayList<>();
        path = URLDecoder.decode(path, "utf-8");
        path = path.replaceAll(">", "\\\\");
        File file = new File(path);
        if (!file.exists()) {
            File[] files = File.listRoots();
            for (File f : files) {
                String canonicalPath = f.getCanonicalPath();
                String url = canonicalPath.replaceAll("\\\\", ">");
                fileList.add(new FileEntity().setFileName(canonicalPath).setUrl(url));
            }
        } else {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                File parentFile = file.getParentFile();
                fileList.add(new FileEntity().setFileName("..").setUrl(parentFile == null ? "" : parentFile.getCanonicalPath().replaceAll("\\\\", ">")));
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        String canonicalPath = f.getCanonicalPath();
                        String url = canonicalPath.replaceAll("\\\\", ">");
                        fileList.add(new FileEntity().setFileName(canonicalPath).setUrl(url));
                    }
                }
            } else {
                InputStream in = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(in);
                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
                byte[] buff = new byte[1024];
                ServletOutputStream os = response.getOutputStream();
                int i;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            }
        }
        model.addAttribute("fileList", fileList);
        return "showFiles";
    }

    @RequestMapping("/")
    public String notMapping(Model model, HttpServletResponse response) throws IOException {
        return list("", model, response);
    }

}
