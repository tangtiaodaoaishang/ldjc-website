package com.example.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: website
 * @description:
 * @author: ldjc
 * @create: 2024-11-21 20:59
 **/
@Controller
public class IndexController {
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/resume")
    public String resume(){
        return "resume";
    }

    @GetMapping("/chat-demo")
    public String chatdemo(){
        return "chat-demo";
    }

    @GetMapping("/image-demo")
    public String imagedemo(){
        return "image-demo";
    }

    @GetMapping("/tts-demo")
    public String ttsdemo(){
        return "tts-demo";
    }

    @GetMapping("/chat")
    public String chatdemo2(){
        return "chat";
    }

    @GetMapping("/image")
    public String image(){
        return "image";
    }

    @GetMapping("/tts")
    public String tts(){
        return "tts";
    }

    @GetMapping("/a")
    public String a(){
        return "a";
    }





}
