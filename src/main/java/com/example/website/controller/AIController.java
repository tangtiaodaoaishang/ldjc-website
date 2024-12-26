package com.example.website.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @program: xit-demo1
 * @description:
 * @author: ldjc
 * @create: 2024-11-22 14:27
 **/
@RestController
@RequestMapping("ai")
public class AIController {
    private static final Map<String, List<String>> responseMap = new HashMap<>();

    static {
        responseMap.put("general", Arrays.asList(
                "我明白你的意思了",
                "这是个很有趣的问题",
                "让我想想...",
                "确实如此",
                "你说得对"
        ));

        responseMap.put("电影", Arrays.asList(
                "《肖申克的救赎》 - 希望是一个好东西，也许是最好的，好东西是不会消亡的",
                "《泰坦尼克号》 - 经典的爱情故事",
                "《盗梦空间》 - 烧脑科幻大作",
                "《千与千寻》 - 宫崎骏的动画神作",
                "《阿甘正传》 - 生活就像一盒巧克力"
        ));

        responseMap.put("笑话", Arrays.asList(
                "人总要犯错误的，否则正确之路人满为患。",
                "日出只要在日落前出现就好，上课只要在下课前到达就好。",
                "你的丑和你的脸没有关系……",
                "你可以像猪一样的生活，但你永远都不能像猪那样快乐！",
                "穿别人的鞋，走自己的路，让他们打的找去吧。"
        ));

        responseMap.put("小说", Arrays.asList(
                "《三体》 - 刘慈欣的科幻巨作，讲述人类文明与三体文明的惊心动魄的故事",
                "《活着》 - 余华描写了人生的苦难与求生意志",
                "《百年孤独》 - 马尔克斯的魔幻现实主义代表作",
                "《围城》 - 钱钟书的讽刺小说，描写了中国知识分子的生活",
                "《平凡的世界》 - 路遥描写了中国农村生活的史诗",
                "《红楼梦》 - 中国古典文学巅峰之作",
                "《白鹿原》 - 陈忠实的关于陕西农村的家族史诗",
                "《追风筝的人》 - 卡勒德·胡赛尼讲述的救赎故事"
        ));
    }




    @GetMapping("chat0")
    public String chat0(String message){
        Random random = new Random();
        String type = (message == null || message.trim().isEmpty()) ? "general" : message.trim();
        List<String> responses = responseMap.getOrDefault(type, responseMap.get("general"));
        String response = responses.get(random.nextInt(responses.size()));
        return "来自AI的响应：" + response;

    }



    private final ChatClient chatClient;

    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    @GetMapping("/chat")
    public String chat(String message) {
        return this.chatClient.prompt()
                .user(message)
                .call()
                .content();
    }



    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
    public reactor.core.publisher.Flux<String> stream(String prompt){
        return this.chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
    }

}
