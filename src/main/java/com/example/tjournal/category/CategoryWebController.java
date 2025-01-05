package com.example.tjournal.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/category")
public class CategoryWebController {
    @GetMapping("/page")
    public String categoryPage() {
        return "category/category"; // templates/category/category.html
    }
    @GetMapping("/seoul")
    public String categorySeoul() {
        return "category/seoul"; // templates/category/category.html
    }
}
