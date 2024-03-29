import java.util.Random;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/2/26
 * Time: 21:09
 */
public class Matcher {
    final int number = 12;//人数
    String[] members = new String[number];

    public Matcher(String temp) {
        members = temp.split(" ");    //自动分割
        match();
    }

    public void match() {
        IntArrStruct ints = new IntArrStruct();
        Random random = new Random();

        boolean flag = true;
        for (int i = 0; i < number / 2; i++) {
            int rand1 = random.nextInt(number);
            int rand2 = random.nextInt(number);

            if (rand1 != rand2) {
                //第一次处理
                if (flag) {
                    ints.tail_add(rand1);
                    ints.tail_add(rand2);
                    flag = false;
                    System.out.print("第1组:");
                    System.out.print(members[rand1] + " ");
                    System.out.print(members[rand2] + " ");
                    System.out.println();
                    continue;
                }
                //第2次及以后
                if (ints.contains(rand1) || ints.contains(rand2)) {
                    i--;
                    continue;
                }
                System.out.print("第" + (i + 1) + "组:");
                System.out.print(members[rand1] + " ");
                System.out.print(members[rand2] + " ");
                System.out.println();
                ints.tail_add(rand1);
                ints.tail_add(rand2);
            } else {
                i--;
            }
        }
        ints.clear();
    }


}
