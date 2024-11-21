package com.example.website;

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


}
