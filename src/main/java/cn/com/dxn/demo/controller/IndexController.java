package cn.com.dxn.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    @RequestMapping()
    public String index(Model model) {
        model.addAttribute("url", "https://www.baidu.com");
        return "index";
    }
}
