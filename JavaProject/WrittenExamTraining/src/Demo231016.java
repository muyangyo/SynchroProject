import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Demo231016 {
    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String command = in.nextLine();
            boolean douHao = false;
            char[] chars = command.toCharArray();
            List<String> list = new LinkedList<>();
            int start = 0;
            boolean startB = true;
            boolean endB = false;
            int end = 0;
            for (int i = 0; i < chars.length; i++) {
                char aChar = chars[i];
                if (aChar == ' ') {
                    if (!douHao) {
                        String str = command.substring(start, i - end);
                        if (end == 1) {
                            end = 0;
                        }
                        list.add(str);
                        start = i + 1;
                    }
                }
                if (aChar == '\"') {
                    douHao = !douHao;
                    if (startB) {
                        start++;
                        startB = false;
                        endB = true;
                    } else if (endB) {
                        end = 1;
                        startB = true;
                        endB = false;
                    }
                }
            }
            String str = command.substring(start, chars.length - end);
            list.add(str);
            System.out.println(list.size());
            for (String temp : list) {
                System.out.println(temp);
            }
        }
    }
}
