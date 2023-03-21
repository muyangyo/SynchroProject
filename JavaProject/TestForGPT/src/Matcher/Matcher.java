package Matcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/21
 * Time: 17:56
 */
public class Matcher {
    private List<String> names;
    private List<String[]> pairs;

    public Matcher(String input) {
        names = new ArrayList<>();
        pairs = new ArrayList<>();
        String[] arr = input.split(" ");
        for (String name : arr) {
            names.add(name);
        }
        for (int i = 0; i < names.size(); i++) {
            for (int j = i + 1; j < names.size(); j++) {
                if (!Main.Rules(names.get(i), names.get(j))) {
                    pairs.add(new String[]{names.get(i), names.get(j)});
                }
            }
        }
        Set<String> usedNames = new HashSet<>();
        int count = 0;
        Random rand = new Random();
        while (count < 6 && pairs.size() > 0) {
            int index = rand.nextInt(pairs.size());
            String[] pair = pairs.get(index);
            if (!usedNames.contains(pair[0]) && !usedNames.contains(pair[1])) {
                System.out.printf("第%d组:%s  %s\n", (count + 1), pair[0], pair[1]);
                usedNames.add(pair[0]);
                usedNames.add(pair[1]);
                count++;
            }
            pairs.remove(index);
        }
    }
}
