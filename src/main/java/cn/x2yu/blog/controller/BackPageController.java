package cn.x2yu.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/admin")
public class BackPageController {
    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("admin_index.html");
        return mav;
    }

}
