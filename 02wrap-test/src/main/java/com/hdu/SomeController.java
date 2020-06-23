package com.hdu;


import com.hdu.service.SomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {
    @Autowired
    private SomeService someService;

    @RequestMapping("/some/{param}")
    public String someServiceHandler(@PathVariable("param") String word) {
        return someService.wrap(word);
    }
}
