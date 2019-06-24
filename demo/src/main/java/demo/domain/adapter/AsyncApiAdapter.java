package demo.domain.adapter;


import java.io.IOException;
import java.lang.InterruptedException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncApiAdapter {
    protected final static Logger log = LoggerFactory.getLogger(AsyncApiAdapter.class);

    @Async("Thread2")
    public CompletableFuture<String> findName(String name, Long sleepTime) throws InterruptedException {
        log.warn("Async function started. processName: " + name + "sleepTime: " + sleepTime);
        Thread.sleep(sleepTime);


        
        // 非同期処理のハンドリングができるようにCompletableFutureに実際に使いたい返却値をぶっこんで利用する
        return CompletableFuture.completedFuture(name);
    }

    @Async("Thread2")
    public CompletableFuture<String> execute(String args[]) {
        String text = new String();
        try {
            Process process = new ProcessBuilder(args).start();
            InputStream is = process.getInputStream();

            /* プロセス実行側での文字列等の出力によっては、
            文字コードが一致しないと、受け取る際に文字化けを起こす*/
//          InputStreamReader isr = new InputStreamReader(is, "Shift-JIS");
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");

            BufferedReader reader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
            // コンソール出力される文字列の格納
            text = builder.toString();
            // 終了コードの格納(0:正常終了 1:異常終了)
            int ret = process.waitFor();
            System.out.println(text);
            System.out.println(ret);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(text);
    }

    public CompletableFuture<String> executeGet() {
        String text = new String();
        System.out.println("===== HTTP GET Start =====");
        try {
            URL url = new URL("http://localhost:8080/get?param=value");

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                                                                       StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===== HTTP GET End =====");
        return CompletableFuture.completedFuture(text);

    }

    public CompletableFuture<String> executePost() {
        String text = new String();
        System.out.println("===== HTTP POST Start =====");
        try {
            URL url = new URL("http://localhost:8080/post");

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),
                                                                                  StandardCharsets.UTF_8));
                writer.write("POST Body");
                writer.write("\r\n");
                writer.write("Hello Http Server!!");
                writer.write("\r\n");
                writer.flush();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                                                                       StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===== HTTP POST End =====");
        return CompletableFuture.completedFuture(text);

    }
}