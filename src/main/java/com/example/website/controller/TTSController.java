package com.example.website.controller;

import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

//文字转音频的类
@RestController
@RequestMapping("/ai")
public class TTSController implements ApplicationRunner {

    @Resource
    private SpeechSynthesisModel speechSynthesisModel;

    private static final String TEXT = "60岁庄鑫凯美女声音说抽瞌睡,嘎洒帽子嘛，冲个括子,鬼火绿,憨不鲁粗";

    private static final String FILE_PATH = "outputs/";

    @PostMapping("/tts")
    //缓存注解
    @Cacheable(value = "aiCache", key = "#input")
    public String tts(String input) throws IOException {
        SpeechSynthesisResponse response = speechSynthesisModel.call(
                new SpeechSynthesisPrompt(input)
        );

//        File file = new File(FILE_PATH + "output.mp3");

        // 生成唯一的文件名（使用 UUID）
        String randomUUID = UUID.randomUUID().toString();
        String filename = "audio_" + randomUUID + ".mp3";
        File file = new File(FILE_PATH + filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
            fos.write(byteBuffer.array());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        return "/"+filename;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
