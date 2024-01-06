package com.muyang.mq.common;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/6
 * Time: 20:14
 */
class Demo implements Serializable {
    int id = 1;
    String name = "123";
}

class BinToolTest {

    @Test
    void toBytes() throws IOException {
        byte[] bytes = BinTool.toBytes(new Demo());
        File file = new File("./demo.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        }
    }

    @Test
    void fromBytes() {

    }
}