package com.smalik.gemfire;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SampleController {

    @RequestMapping("/hello")
    public @ResponseBody String sayHello(HttpSession session) {

        Integer fooCounter = (Integer) session.getAttribute("foo");
        if (fooCounter == null) {
            fooCounter = Integer.valueOf(1);
        } else {
            fooCounter = Integer.valueOf(fooCounter.intValue() + 1);
        }
        session.setAttribute("foo", fooCounter);

        return "Hello. This is your visit #" + fooCounter;
    }
}
