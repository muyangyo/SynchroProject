import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/7
 * Time: 16:12
 */
public class Demo {
    public static void main(String[] args) {
        Long timeStamp = System.currentTimeMillis();
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        System.out.println(time);
    }
}
