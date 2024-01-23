package com.muyang.mq.server.brokercore;

import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.Queue;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/22
 * Time: 17:31
 */
@Slf4j
public class Router {
    /**
     * 检查 bindingKey 的合法性
     */
    public boolean checkBindingKey(String bindingKey) {
        /*
          bindingKey 的构造规则:
              1. 数字, 字母, 下划线(_)
              2. 使用 . 分割成若干部分
              3. 允许存在 * 和 # 作为通配符. 但是通配符只能作为独立的部分
          eg:
            aaa.*.bbb  合法
            aaa.a*.bbb 非法
            a          合法
            a.b.c      合法
                       合法(直接交换机 和 扇出交互机 不需要)
            *.*.*      合法
            #          合法
         */
        if (!StringUtils.hasLength(bindingKey)) {
            return true;//routingKey为 空 时或者为 "" 时,合法,且一般用于 扇出交换机 或 直接交换机
        }
        //不为空时,一个一个遍历
        char[] chars = bindingKey.toCharArray();
        for (char temp : chars) {
            // 判定该字符是否是大写字母
            if (temp >= 'A' && temp <= 'Z') {
                continue;
            }
            // 判定该字母是否是小写字母
            if (temp >= 'a' && temp <= 'z') {
                continue;
            }
            // 判定该字母是否是阿拉伯数字
            if (temp >= '0' && temp <= '9') {
                continue;
            }
            // 判定是否是 _ 或者 .
            if (temp == '_' || temp == '.') {
                continue;
            }
            // 该字符, 不是上述任何一种合法情况, 就直接返回 false
            return false;
        }
        // 检查 * 或者 # 是否是独立的部分
        // aaa.*.bbb 合法情况;  aaa.a*.bbb 非法情况
        String[] words = bindingKey.split("\\.");
        for (String word : words) {
            // 检查 word 长度 > 1 并且包含了 * 或者 # , 就是非法的格式了.
            if (word.length() > 1 && (word.contains("*") || word.contains("#"))) {
                return false;
            }
        }
        /*特殊
            aaa.#.#.bbb  会自动变为 aaa.#.bbb
            aaa.#.*.bbb  会自动变为 aaa.#.bbb
            aaa.*.#.bbb  会自动变为 aaa.#.bbb
            这里先不进行处理,在匹配的时候再进行转换
            */
        return true;
    }

    /**
     * 检查 routingKey 的合法性
     */
    public boolean checkRoutingKey(String routingKey) {
        //由于直接交换机已经在前面提交了信息,就不会出现 直接交换机 的 routingKey(纯字符串没有.)
        /*
        routingKey 的构造规则:
            1. 数字, 字母, 下划线
            2. 使用 . 分割成若干部分
        * */
        if (!StringUtils.hasLength(routingKey)) {
            return true;//routingKey为 空 时或者为 "" 时,合法,且一般用于 扇出交换机
        }
        //不为空时,一个一个遍历
        char[] chars = routingKey.toCharArray();
        for (char temp : chars) {
            // 判定该字符是否是大写字母
            if (temp >= 'A' && temp <= 'Z') {
                continue;
            }
            // 判定该字母是否是小写字母
            if (temp >= 'a' && temp <= 'z') {
                continue;
            }
            // 判定该字母是否是阿拉伯数字
            if (temp >= '0' && temp <= '9') {
                continue;
            }
            // 判定是否是 _ 或者 .
            if (temp == '_' || temp == '.') {
                continue;
            }
            // 该字符, 不是上述任何一种合法情况, 就直接返回 false
            return false;
        }
        // 把每个字符都检查过, 没有遇到非法情况. 直接返回 true
        return true;
    }

    /**
     * 优化bindingKey的
     */
    public static String convertor(String bindingKey) {
        /*特殊
            aaa.#.#.bbb  会自动变为 aaa.#.bbb
            aaa.#.*.bbb  会自动变为 aaa.#.bbb
            aaa.*.#.bbb  会自动变为 aaa.#.bbb
         */
        //先检查下特殊情况有几个
        String[] split = bindingKey.split("\\.");
        Queue<Node> needToHandle = new LinkedList<>();//记录需要处理的特殊情况
        int index = 0;
        boolean has = false;//区间内有没有 #
        while (index < split.length) {
            Node node = new Node();

            //遇见通配符,记录起始位置
            if (split[index].contains("*") || split[index].contains("#")) {
                node.start = index;
                if (split[index].contains("#")) {
                    has = true;//看看第一个是不是 #
                }
            } else {
                index++;
                continue;
            }

            //寻找结束位置
            while (index < split.length) {
                if (!split[index].contains("*") && !split[index].contains("#")) {
                    //不包含统配符了,则跳出
                    node.end = index - 1;//index下标的前一个则必然是通配符
                    break;
                }
                if (split[index].contains("#")) {
                    has = true;//看看是不是 #
                }
                index++;
            }
            //如果最后一个也是通配符,则需要额处理下
            if (index == split.length) {
                node.end = index - 1;
            }

            //需要处理的队列追加
            if (has) {
                needToHandle.add(node);
            }
        }

        //处理特殊情况
        Node poll = needToHandle.poll();//手动提取第一个结点
        if (poll == null) {
            return bindingKey;//没有特殊情况直接返回
        }
        index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (index < split.length) {
            if (poll != null && index == poll.start) {
                //如果进去了那个区间则直接跳过这个区间,然后只要追加一个 # 即可
                index = poll.end + 1;
                poll = needToHandle.poll();
                if (index == split.length) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append("#.");
                }
                continue;
            }
            stringBuilder.append(split[index]);
            index++;
            if (index != split.length) {
                stringBuilder.append(".");
            }
        }
        String ret = stringBuilder.toString();
        log.info("已优化bindingKey!\n旧: bindingKey= " + bindingKey + "\n新: bindingKey= " + ret);
        return ret;
    }

    static class Node {
        int start = 0;
        int end = 0;
    }

    /*public static void main(String[] args) {
        convertor("aaa.#.#.bbb");
        convertor("aaa.#.*.bbb");
        convertor("aaa.*.#.bbb");
        convertor("#.#.#");
        convertor("*.*.bbb");
       convertor("a.b.bbb");
       convertor("a.#.*");
       convertor("a.#.#");
    }*/

    /**
     * 判断是否符合转发规则
     *
     * @param type    交换机类型
     * @param binding 绑定
     * @param message 消息
     * @return 如果符合 交换机类型 的 转发规则 则返回 true,否则返回 false
     */
    public boolean route(ExchangeType type, Binding binding, Msg message) throws MqException {
        if (type == ExchangeType.FANOUT) {
            // 如果是 FANOUT 类型, 则该交换机上绑定的所有队列都需要转发(一直返回true即可)
            return true;
        } else if (type == ExchangeType.TOPIC) {
            // 如果是 TOPIC 主题交换机,则需要进一步判断
            return routeTopic(binding, message);
        } else {
            // 其他情况不应该存在的.
            throw new MqException(binding.getExchangeName() + " 交换机的类型非法!类型为:" + type);
        }
    }

    private boolean routeTopic(Binding binding, Msg message) {
        // 先把这两个 key 进行切分
        String[] bindingSplit = binding.getBindingKey().split("\\.");
        String[] routingSplit = message.getBasicProperties().getRoutingKey().split("\\.");

        // 设置两个下标
        int bindingIndex = 0;
        int routingIndex = 0;
        while (bindingIndex < bindingSplit.length && routingIndex < routingSplit.length) {
            if (bindingSplit[bindingIndex].equals("*")) {
                // 如果遇到 * , 直接 双+1 进入下一轮. * 可以匹配到任意一个部分
                bindingIndex++;
                routingIndex++;
            } else if (bindingSplit[bindingIndex].equals("#")) {
                // 如果遇到 #, 需要先看看有没有下一个位置(顺带也可以指向 要找到部分 )
                bindingIndex++;
                if (bindingIndex == bindingSplit.length) {
                    //该 # 后面没东西了, 说明此时一定能匹配成功了!
                    return true;
                }
                // # 后面还有东西, 拿着这个内容, 去 routingKey 中往后找, 找到对应的位置
                String target = bindingSplit[bindingIndex];
                while (routingIndex < routingSplit.length) {
                    if (routingSplit[routingIndex].equals(target)) {
                        break;// 找到了,跳出
                    }
                    routingIndex++;
                }

                if (routingIndex == routingSplit.length) {
                    return false;// 如果是因为找不到,越界了,则直接返回 false(正常情况是 routingIndex 是处于 target 对应的字符串下标,再次+1后可能会到达末尾)
                }
                // 找到的匹配的情况, 继续往后匹配.
                bindingIndex++;
                routingIndex++;
            } else {
                //如果遇到普通字符串, 要求两个的内容是一样的.
                if (!bindingSplit[bindingIndex].equals(routingSplit[routingIndex])) {
                    return false;
                }
                bindingIndex++;
                routingIndex++;
            }
        }
        //判定是否是双方同时到达末尾 比如 aaa.bbb.ccc 和 aaa.bbb 是要匹配失败的.
        if (bindingIndex == bindingSplit.length && routingIndex == routingSplit.length) {
            return true;
        }
        return false;
    }
}
