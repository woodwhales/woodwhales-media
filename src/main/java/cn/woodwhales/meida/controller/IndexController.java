package cn.woodwhales.meida.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woodwhales on 2023-01-22 0:05
 */
@RestController
@RequestMapping
public class IndexController {

    @GetMapping({"", "/index"})
    public String index() {
        return "ok";
    }
}
