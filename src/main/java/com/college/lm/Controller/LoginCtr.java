package com.college.lm.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginCtr {
    @GetMapping("")
    public String getLogin(){
        return "home";
    }
}