package com.hdu.testboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author: xs
 * @since: 2020-06-21
 */
@Controller
public class IndexController {

    @GetMapping("/hello")
    @ResponseBody
    public String index(){
        System.out.println("Index Controller");
        return "hello";
    }
}
