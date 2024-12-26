package com.example.website.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.chat.MessageFormat;

import com.example.website.helper.FrameExtraHelper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//视频分析
@RestController
@RequestMapping("/ai/multi")
public class MultiModelController {

    @Resource
    private ChatModel chatModel;

    @Resource
    private ResourceLoader resourceLoader;

    private static final String DEFAULT_PROMPT = "这些是什么？";

    private static final String DEFAULT_MODEL = "qwen-vl-max-latest";

    //图片分析
    @GetMapping("/image")
    public String imagesBinary(
            @RequestParam(value = "prompt", required = false, defaultValue = DEFAULT_PROMPT)
            String prompt
    ) {

        UserMessage message = new UserMessage(
                prompt,
                new Media(
                        MimeTypeUtils.IMAGE_PNG,
                        resourceLoader.getResource("classpath:/multimodel/dog_and_girl.jpeg")
                ));
        message.getMetadata().put(DashScopeChatModel.MESSAGE_FORMAT, MessageFormat.IMAGE);

        ChatResponse response = chatModel.call(
                new Prompt(
                        message,
                        DashScopeChatOptions.builder()
                                .withModel(DEFAULT_MODEL)
                                .withMultiModel(true)
                                .build()
                )
        );

        return response.getResult().getOutput().getContent();
    }



    //图片分析升级
    @GetMapping("/image1/{img}")
    public String imagesBinary1(@PathVariable("img") String img,
            @RequestParam(value = "prompt", required = false, defaultValue = DEFAULT_PROMPT)
            String prompt
    ) {

        UserMessage message = new UserMessage(
                prompt,
                new Media(
                        MimeTypeUtils.IMAGE_PNG,
                        resourceLoader.getResource(img)
                ));
        message.getMetadata().put(DashScopeChatModel.MESSAGE_FORMAT, MessageFormat.IMAGE);

        ChatResponse response = chatModel.call(
                new Prompt(
                        message,
                        DashScopeChatOptions.builder()
                                .withModel(DEFAULT_MODEL)
                                .withMultiModel(true)
                                .build()
                )
        );

        return response.getResult().getOutput().getContent();
    }

    //视频分析
    @GetMapping("/video")
    public String video(
            @RequestParam(value = "prompt", required = false, defaultValue = DEFAULT_PROMPT)
            String prompt
    ) {

        List<Media> mediaList = FrameExtraHelper.createMediaList(10);

        UserMessage message = new UserMessage(prompt, mediaList);
        message.getMetadata().put(DashScopeChatModel.MESSAGE_FORMAT, MessageFormat.VIDEO);

        ChatResponse response = chatModel.call(
                new Prompt(
                        message,
                        DashScopeChatOptions.builder()
                                .withModel(DEFAULT_MODEL)
                                .withMultiModel(true)
                                .build()
                )
        );

        return response.getResult().getOutput().getContent();
    }
}
