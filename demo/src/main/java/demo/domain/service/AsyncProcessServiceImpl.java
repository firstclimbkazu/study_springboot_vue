package demo.domain.service;

import demo.domain.adapter.AsyncProcessAdapter;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncProcessServiceImpl implements AsyncProcessService{
    protected final static Logger log = LoggerFactory.getLogger(AsyncProcessServiceImpl.class);

    @Autowired
    private AsyncProcessAdapter asyncProcessAdapter;

    public List<String> findName() throws InterruptedException {
        long start = System.currentTimeMillis();
        long heavyProcessTime = 3000L;
        long lightProcessTime = 1000L;

        log.warn("request started");
        CompletableFuture<String> heavyProcess = asyncProcessAdapter.findName("heavy", heavyProcessTime);
        CompletableFuture<String> lightProcess = asyncProcessAdapter.findName("light", lightProcessTime);

        // heavyProcessが終わったら実行される処理
        heavyProcess.thenAcceptAsync(heavyProcessResult -> {
            log.warn("finished name=" + heavyProcessResult + ", sleep = " + heavyProcessTime);
        });
        // lightProcessが終わったら実行される処理
        lightProcess.thenAcceptAsync(lightProcessResult -> {
            log.warn("finished name=" + lightProcessResult + ", sleep = " + lightProcessTime);
        });

        // 指定した処理が終わったらこれ以降の処理が実行される（コメントアウトで待たずに構造実行）
        //CompletableFuture.allOf(heavyProcess, lightProcess).join();

        // 指定時間帯機で結果が変わる事を確認
        //Thread.sleep(4000L);

        // 返却値の作成
        List<String> names = new ArrayList<>();
        names.add(heavyProcess.getNow("non end heavy"));
        names.add(lightProcess.getNow("non end light"));

        Thread.sleep(10L);

        long end = System.currentTimeMillis();
        // 処理全体の時間を出力
        log.warn("request finished. time: " + ((end - start))  + "ms");

        return names;
    }

}