package demo.domain.service;

import java.util.List;

public interface AsyncProcessService {

    public List<String> findName() throws InterruptedException;

}