package com.muyang.mq.server.dao;

import com.muyang.mq.common.BinTool;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;
import com.muyang.mq.server.dao.model.Stats;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/5
 * Time: 15:25
 */
@Slf4j
@Service
public class MsgFileManager {

    private final String MSG_BIG_SIZE = "int";
    @Value("${MuYang-mq.msg-locations}")
    private String basePath;//基准路径

    public MsgFileManager() {
        if (basePath == null) {
            basePath = "./data";
            log.info("已使用默认路径进行存储");
        }
    }

    // 获取队列目录所在位置
    private String getQueueDir(String queueName) {
        return basePath + "/" + queueName;
    }

    // 这个方法用来获取该队列的 消息数据 文件路径
    private String getQueueDataPath(String queueName) {
        return getQueueDir(queueName) + "/queue_data.txt";//后期记得改bin
    }

    // 这个方法用来获取该队列的 消息统计 文件路径
    private String getQueueStatsPath(String queueName) {
        return getQueueDir(queueName) + "/queue_stats.txt";
    }

    // 读stats文件
    private Stats readStats(String queueName) {
        File file = new File(getQueueStatsPath(queueName));
        Stats stats = new Stats();
        try (FileInputStream inputStream = new FileInputStream(file); Scanner scanner = new Scanner(inputStream, "utf8")) {
            stats.setTotalCount(scanner.nextInt());
            stats.setValidCount(scanner.nextInt());
            return stats;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //  写stats文件
    private void writeStats(String queueName, Stats stat) {
        File file = new File(getQueueStatsPath(queueName));
        try (FileOutputStream outputStream = new FileOutputStream(file); PrintWriter printWriter = new PrintWriter(outputStream)) {
            printWriter.print(stat.getTotalCount() + "\t" + stat.getValidCount());
            printWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 创建队列对应的文件和目录
    public void createQueueFiles(String queueName) throws IOException {
        File dir = new File(getQueueDir(queueName));
        boolean isOkDir = dir.mkdirs();

        File data = new File(getQueueDataPath(queueName));
        boolean isOkData = data.mkdirs();

        File stats = new File(getQueueStatsPath(queueName));
        boolean isOkStats = stats.mkdirs();

        if (!isOkDir || !isOkData || !isOkStats) {
            throw new IOException("队列相关文件创建失败!");
        }
        log.info("队列相关文件创建成功!");
    }

    // 删除队列的目录和文件. 删除掉无用队列的全部数据
    public void destroyQueueFiles(String queueName) throws IOException {
        File dir = new File(getQueueDir(queueName));
        boolean isOkDir = dir.delete();

        File data = new File(getQueueDataPath(queueName));
        boolean isOkData = data.delete();

        File stats = new File(getQueueStatsPath(queueName));
        boolean isOkStats = stats.delete();

        if (!isOkDir || !isOkData || !isOkStats) {
            throw new IOException("队列相关文件删除失败!");
        }
        log.info("队列相关文件删除成功!");
    }

    // 检查队列的目录和文件是否存在. 后续有生产者给 broker server 生产消息了, 这个消息就可能需要记录到文件上(取决于消息是否要持久化)
    public boolean checkFilesExits(String queueName) throws IOException {
        File dir = new File(getQueueDir(queueName));
        boolean isOkDir = dir.exists();

        File data = new File(getQueueDataPath(queueName));
        boolean isOkData = data.exists();

        File stats = new File(getQueueStatsPath(queueName));
        boolean isOkStats = stats.exists();

        return isOkDir && isOkData && isOkStats;
    }

    // 这个方法用来把一个新的消息, 放到队列对应的文件中(内存和文件中同时存在).queue 表示要把消息写入的队列. message 则是要写的消息.
    public void sendMessage(QueueCore queue, Msg message) throws MqException, IOException {
        // 1. 检查一下当前要写入的队列对应的文件是否存在.
        if (!checkFilesExits(queue.getName())) {
            throw new MqException(queue.getName() + "的文件不存在!");
        }
        // 2. 把 Msg 对象, 进行序列化, 转成二进制的字节数组.
        byte[] bytes = BinTool.toBytes(message);

//        这个是为了可存储更大的 Msg 对象的思路
//        int msg_Size = 4;
//        if (MSG_BIG_SIZE.equals("int")) {
//            msg_Size = 4;
//        } else if (MSG_BIG_SIZE.equals("long")) {
//            msg_Size = 8;
//        }

        //这里对同一个 队列 加锁,避免出现多线程造成的脏读等问题(只有在操作同一个队列时才有影响)
        synchronized (queue) {
            // 3. 先获取到当前的队列数据文件的长度, 用这个来计算出该 Msg 对象的 offsetBeg 和 offsetEnd
            File data = new File(getQueueDataPath(queue.getName()));
            message.setOffsetBegin(data.length() + 4);
            message.setOffsetEnd(data.length() + 4 + bytes.length);


            // 4. 写入消息到数据文件, 注意, 是追加写入到数据文件末尾.
            try (OutputStream outputStream = new FileOutputStream(data, true);
                 DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                    dataOutputStream.writeInt(bytes.length);//先写入 当前Msg 的大小
                dataOutputStream.write(bytes);
                dataOutputStream.flush();
            }

            // 5. 更新消息统计文件
            Stats stats = readStats(queue.getName());
            stats.setTotalCount(stats.getTotalCount() + 1);
            stats.setValidCount(stats.getValidCount() + 1);
            writeStats(queue.getName(), stats);
        }
    }


}
