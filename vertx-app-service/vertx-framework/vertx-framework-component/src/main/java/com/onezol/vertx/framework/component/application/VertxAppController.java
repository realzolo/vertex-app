package com.onezol.vertx.framework.component.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class VertxAppController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:/console";
    }

}
