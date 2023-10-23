package DAO;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/18
 * Time: 20:38
 */
public class Blog {
    public int blogId;
    public int userId;
    public String title;
    public Timestamp pDate;//由于没有 datetime (Date没有准确的时分秒)类型,而且Java的时间戳类型比MySQL的时间戳类型大,所以可以用更久
    public String dateTime;

    public String content;

    public Blog() {
    }

    public Blog(int userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", pDate=" + pDate +
                ", dateTime='" + dateTime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
