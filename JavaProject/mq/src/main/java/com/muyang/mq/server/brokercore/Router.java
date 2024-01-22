package com.muyang.mq.server.brokercore;

import com.muyang.mq.server.brokercore.constant.ExchangeType;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/22
 * Time: 17:31
 */

public class Router {
    // TODO: 2024/1/22 明天要完成的部分
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
          特殊
            aaa.#.#.bbb  会自动变为 aaa.#.bbb
            aaa.#.*.bbb  会自动变为 aaa.#.bbb
            aaa.*.#.bbb  会自动变为 aaa.#.bbb
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
        return true;
    }


    /**
     * 判断是否符合转发规则
     *
     * @param type    交换机类型
     * @param binding 绑定
     * @param message 消息
     * @return 如果符合 交换机类型 的 转发规则 则返回 true,否则返回 false
     */
    public boolean route(ExchangeType type, Binding binding, Msg message) {
        return false;
    }
}
