package ForTest;

import java.util.*;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/6
 * Time:10:10
 */

public class OJ {

    public int numJewelsInStones(String jewels, String stones) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < stones.length(); i++) {
            char cur = stones.charAt(i);
            if (map.containsKey(cur)) {
                int temp = map.get(cur);
                map.put(cur, temp + 1);
            } else {
                map.put(cur, 1);
            }
        }

        int count = 0;
        for (int i = 0; i < jewels.length(); i++) {
            char cur = jewels.charAt(i);
            if (map.containsKey(cur)) {
                count += map.get(cur);
            }
        }

        return count;
    }

    /*
    给定一个单词列表 words 和一个整数 k ,返回前 k 个出现次数最多的单词。
    返回的答案应该按单词出现频率由高到低排序。如果不同的单词有相同出现频率,按字典顺序 排序。
    来源：力扣（LeetCode） 链接：https://leetcode.cn/problems/top-k-frequent-words 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */

    public static List<String> topKFrequent(String[] words, int k) {
        //统计每个单词出现的次数
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {

            if (map.containsKey(word)) {
                //已经出现过一次的
                int val = map.get(word);
                map.put(word, val + 1);
            } else {
                //第一次出现的
                map.put(word, 1);
            }

        }

        //遍历map,找出前 k 个出现次数最多的单词

        PriorityQueue<Map.Entry<String, Integer>> priorityQueue = new PriorityQueue<>(
                new Comparator<Map.Entry<String, Integer>>() //内部类
                {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        int val1 = o1.getValue();
                        int val2 = o2.getValue();

                        if (val1 - val2 == 0) {
                            //出现频率相同的情况
                            return -(o1.getKey().compareTo(o2.getKey()));// a字母 打头应该比 b字母 打头大
                        }
                        //出现频率不同的情况
                        return val1 - val2;
                    }
                });
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        //转变为 Set ,这样才能存放在 优先级队列 里面,但是不能只存 key,因为会导致没办法比较
        priorityQueue.addAll(set);

        //将前 k 个出现次数最多的单词加入数组中
        ArrayList<String> ret = new ArrayList<>();
        while (!priorityQueue.isEmpty()) {
            Map.Entry<String, Integer> temp = priorityQueue.peek();
            if (priorityQueue.size() <= k) {
                ret.add(temp.getKey());
            }
            priorityQueue.poll();
        }

        //翻转数组(因为这个时候数组是 出现次数小 的在前面, 出现次数多 的在后面,所以需要倒置)
        Collections.reverse(ret);
        return ret;
    }


    public static void main(String[] args) {
        System.out.println(topKFrequent(new String[]{"b", "b", "a"}, 1).toString());
    }

}
