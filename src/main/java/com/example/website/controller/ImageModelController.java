package com.example.website.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ImageModelController {

    private final ImageModel imageModel;

    public ImageModelController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/ai/image/{input}")
    @Cacheable(value = "aiCache", key = "#input")
    public String image (@PathVariable String input, HttpServletResponse resp){
        ImageOptions options = ImageOptionsBuilder.builder()
                .withModel("wanx-v1")
                //.withN(1)
                //.withWidth(800)
                .build();


        ImagePrompt imagePrompt = new ImagePrompt(input, options);
        ImageResponse response = imageModel.call(imagePrompt);
        String imageUrl = response.getResult().getOutput().getUrl();

        try {
            // 创建outputs目录（如果不存在）
            Path outputsDir = Paths.get("outputs");
            if (!Files.exists(outputsDir)) {
                Files.createDirectories(outputsDir);
            }

            // 生成唯一的文件名（使用时间戳）
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "image_" + timestamp + ".png";
            Path outputPath = outputsDir.resolve(fileName);

            // 下载图片
            URL url = new URL(imageUrl);
            InputStream in = url.openStream();
            byte[] imageData = in.readAllBytes();

            // 保存图片到outputs目录
            Files.write(outputPath, imageData);

            return "/" + fileName;
        } catch (IOException e) {
            return "图片生成出错：" + e.getMessage();
        }
    }
}
