package demo.app.testa;
import demo.domain.service.AsyncProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@RequiredArgsConstructor
@RestController
public class TestAController {

    protected final static Logger log = LoggerFactory.getLogger(TestAController.class);
    @Autowired
    private AsyncProcessService asyncProcessService;
    @RequestMapping(value = "/testa/")
    public List<String> findUsers() throws Exception {

        List<String> names = asyncProcessService.findName();
        return names;
    }
}
