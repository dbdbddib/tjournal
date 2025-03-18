package com.example.tjournal.emailapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmailPageController {

    @GetMapping("/email")
    public String showEmailPage() {
        // templates 폴더 안의 email.html 파일을 찾아 렌더링
        return "email";
    }
}