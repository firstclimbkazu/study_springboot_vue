package demo.app.testb;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class testBController {

    @RequestMapping(value = "/testb/")
    String hello() {
        return "Hello testB!";
    }
}
