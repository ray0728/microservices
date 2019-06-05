package com.rcircle.service.stream.utils.core;

;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

@Component
public class CommandExecutor {
    private Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private ProcessBuilder processBuilder;
    public static Random random = new Random();

    public CommandExecutor setCmd(List<String> cmd) {
        processBuilder = new ProcessBuilder(cmd);
        return this;
    }

    @Async("taskExecutor")
    public Future<Integer> asyncProcess(Object flag, CommandCallback callback) {
        int ret = process(flag, callback);
        return new AsyncResult<>(ret);
    }

    public int process(Object flag, CommandCallback callback) {
        long start = System.currentTimeMillis();
        int ret = 0;
        if (callback != null) {
            ret = processWithFinish(flag, callback);
        } else {
            processWithoutFinish();
        }
        long end = System.currentTimeMillis();
        logger.info("finished:" + (end - start));
        return ret;
    }


    private int processWithFinish(Object flag, CommandCallback callback) {
        int ret = 0;
        try {
            if (callback != null) {
                callback.preProcess(flag);
            }
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            if (callback != null) {
                byte[] cache = new byte[1024];
                InputStream inputstream = process.getInputStream();
                while (inputstream.read(cache) != -1l) {
                    callback.processing(flag, new String(cache));
                }
                inputstream.close();
            }
            ret = process.waitFor();
            if (callback != null) {
                callback.afterProcess(flag);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void processWithoutFinish() {
        try {
            Process process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
