package com.rcircle.service.stream;

import com.rcircle.service.stream.utils.core.CommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamApplicationTests {

    @Autowired
    CommandExecutor commandExecutor;
    @Test
    public void contextLoads() throws Exception {
        commandExecutor.doTaskOne();
        commandExecutor.doTaskTwo();
        commandExecutor.doTaskThree();
        Thread.currentThread().join();
    }

}
