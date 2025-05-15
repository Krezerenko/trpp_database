package io.github.krezerenko.trpp_database;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController
{
    @GetMapping("/check")
    public String check()
    {
        return "Hello World";
    }
}
