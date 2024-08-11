package kr.kkj.server.ringoredisclient.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
//        return "redirect:/build/index.html";
        return "redirect:/index.html";
    }
}
