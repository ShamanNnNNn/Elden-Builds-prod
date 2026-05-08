package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index(){
        return "index.html";
    }

    @GetMapping("/my-builds")
    public String myBuilds(){
        return "my-builds.html";
    }

    @GetMapping("/moderation")
    public String moderation() {
        return "moderation";
    }
    @GetMapping("/build/{id}")
    public String buildDetail() {
        return "guide";
    }
    @GetMapping("/build-editor/{id}")
    public String buildEditor() {
        return "build-editor";
    }
}
