package cn.woodwhales.meida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author woodwhales on 2023-01-22 0:05
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping({"", "/index"})
    public String index() {
        return "index";
    }

}
