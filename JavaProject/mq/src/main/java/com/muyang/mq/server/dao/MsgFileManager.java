package com.muyang.mq.server.dao;

import com.muyang.mq.common.BinTool;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;
import com.muyang.mq.server.dao.model.Stats;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/5
 * Time: 15:25
 */
@Slf4j
//@Repository
public class MsgFileManager {

    //    private final String MSG_BIG_SIZE = "int";
    @Value("${MuYang-mq.msg-locations}")
    private String basePath;//基准路径

    public void init() {
        //目前为空方法,为了统一形式和后期拓展
    }

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

    //  写stats文件(直接覆盖写)
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
        boolean isOkData = data.createNewFile();

        File stats = new File(getQueueStatsPath(queueName));
        boolean isOkStats = stats.createNewFile();

        //初始化stats文件
        Stats initStats = new Stats();
        initStats.setTotalCount(0);
        initStats.setValidCount(0);
        writeStats(queueName, initStats);

        if (!isOkDir || !isOkData || !isOkStats) {
            throw new IOException("队列相关文件创建失败!");
        }
        log.info("队列相关文件创建成功!");
    }

    // 删除队列的目录和文件. 删除掉无用队列的全部数据
    public void destroyQueueFiles(String queueName) throws IOException {
        File data = new File(getQueueDataPath(queueName));
        boolean isOkData = data.delete();

        File stats = new File(getQueueStatsPath(queueName));
        boolean isOkStats = stats.delete();

        File dir = new File(getQueueDir(queueName));
        boolean isOkDir = dir.delete();

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
            try (OutputStream outputStream = new FileOutputStream(data, true); DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
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


    // 这个是删除消息的方法. 这里的删除是逻辑删除, 也就是把硬盘上存储的这个 Msg 里面的那个 isValid 属性设置成 0
    public void deleteMessage(QueueCore queue, Msg message) throws IOException, ClassNotFoundException {
        synchronized (queue) {
            // 1. 先把文件中的这一段数据, 读出来, 还原回 Message 对象;
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(getQueueDataPath(queue.getName()), "rw")) {
                randomAccessFile.seek(message.getOffsetBegin());//将光标设置到 Msg 的起始位置
                byte[] buffer = new byte[(int) (message.getOffsetEnd() - message.getOffsetBegin())];//读取缓存区
                randomAccessFile.read(buffer);
                Msg diskMsg = (Msg) BinTool.fromBytes(buffer);//反序列化

                // 2. 把 isValid 改成 0;
                diskMsg.setIsValid((byte) 0x00);

                // 3. 重新写回文件里
                byte[] bytes = BinTool.toBytes(diskMsg);
                randomAccessFile.seek(message.getOffsetBegin());//重新将光标设置到起始位置(读取和写入操作会移动光标)
                randomAccessFile.write(bytes);
            }
            // 4. 更新统计信息
            Stats stats = readStats(queue.getName());
            stats.setValidCount(stats.getValidCount() - 1);
            writeStats(queue.getName(), stats);
        }
    }

    /*
    使用这个方法从文件中, 读取出所有有效的消息内容, 加载到内存中(具体来说是放到一个链表里)
    由于该方法是在程序启动时调用, 此时服务器还不能处理请求,所以不涉及多线程操作文件
    */
    public LinkedList<Msg> loadAllMessageFromQueue(String queueName) throws IOException, MqException, ClassNotFoundException {
        LinkedList<Msg> ret = new LinkedList<>();
        long pos = 0;//记录指针位置
        try (InputStream inputStream = new FileInputStream(getQueueDataPath(queueName)); DataInputStream dataInputStream = new DataInputStream(inputStream)) {

            while (true) {
                // 1. 读取当前消息的长度, 这里的 readInt 可能会读到文件的末尾(EOF)然后抛出 "异常"
                //EOFException – if this input stream reaches the end before reading four bytes.
                int msgLength = dataInputStream.readInt();
                pos += 4;//指针位移

                // 2. 按照这个长度, 读取消息内容
                byte[] msgBytes = new byte[msgLength];
                int count = dataInputStream.read(msgBytes);
                if (count < msgLength) {
                    //如果出现不符合长度的信息时,则可能这条消息有数据丢失
                    throw new MqException("Msg数据格式出错!请检查消息源文件");
                }
                // 3. 把这个读到的二进制数据, 反序列化回 Msg 对象
                Msg msg = (Msg) BinTool.fromBytes(msgBytes);
                // 4. 判定一下看看这个消息对象, 是不是无效对象.
                if (msg.getIsValid() == 0x00) {
                    // 注意这里也要移动指针位置
                    pos += msgLength;
                    continue;//无效信息跳过
                }
                // 5. 有效数据, 则需要把这个 Msg 对象加入到链表中. 加入之前还需要填写 offsetBeg 和 offsetEnd
                // 由于 DataInputStream 没有获取获取当前指针位置的函数,只能借助 一个变量 来记录指针位置
                msg.setOffsetBegin(pos);
                msg.setOffsetEnd(pos + msgLength);
                pos += msgLength;
                ret.add(msg);
            }

        } catch (EOFException e) {
            log.info("{} 队列数据加载完成", queueName);
            return ret;
        }
    }

    // 检查当前是否要针对 该队列的 消息 数据文件进行 GC
    public boolean checkGC(String queueName) {
        Stats stats = readStats(queueName);
        //如果 总信息数大于2000 且 有效信息占比小于 0.5 则执行 GC
        return stats.getTotalCount() > 2000 && ((double) stats.getValidCount() / (double) stats.getTotalCount() < 0.5);
    }


    //获取 临时中转文件 路径
    private String getQueueDataTempPath(String queueName) {
        return getQueueDir(queueName) + "/queue_data_temp.txt";
    }

    // 这个方法是真正执行消息数据文件的垃圾回收操作的,利用 复制算法 先将有效消息存储在临时文件上,把原有文件删除后,重新命名为原文件
    public void gc(QueueCore queue) throws MqException, IOException, ClassNotFoundException {
        //由于GC操作的时间比较长,且对数据文件影响很大,所以在gc时,不能让其他线程操作
        synchronized (queue) {
            // 1. 创建一个新的文件
            File tempFile = new File(getQueueDataTempPath(queue.getName()));
            if (tempFile.exists()) {
                //如果文件存在,则说明上次 gc 异常中断了.
                throw new MqException("gc失败,原因是上次gc失败导致的数据错乱,需要手动删除临时文件" + tempFile.getAbsoluteFile());
            }
            boolean ok = tempFile.createNewFile();
            if (!ok) {
                throw new MqException("创建新文件失败!" + tempFile.getAbsoluteFile());
            }

            // 2. 从旧的文件中, 读取出所有的有效消息对象了.(直接调用前面的方法就行了)
            LinkedList<Msg> msgLinkedList = loadAllMessageFromQueue(queue.getName());

            // 3. 把有效消息, 写入到新的文件中.
            try (OutputStream outputStream = new FileOutputStream(tempFile);
                 DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                for (Msg temp : msgLinkedList) {
                    byte[] bytes = BinTool.toBytes(temp);
                    dataOutputStream.writeInt(bytes.length);
                    dataOutputStream.write(bytes);
                }
                dataOutputStream.flush();//避免缓冲区残余
            }

            // 4. 删除旧的数据文件, 并且把新的文件进行重命名
            File file = new File(getQueueDataPath(queue.getName()));
            ok = file.delete();
            if (!ok) {
                throw new MqException("gc失败,原因是旧数据文件被占用,无法删除!请关闭后重试!" + file.getAbsoluteFile());
            }
            ok = tempFile.renameTo(file);
            if (!ok) {
                throw new MqException("gc重命名失败");
            }

            // 5. 更新统计文件
            Stats stats = new Stats();
            stats.setValidCount(msgLinkedList.size());
            stats.setTotalCount(msgLinkedList.size());
            writeStats(queue.getName(), stats);
        }

    }
}
