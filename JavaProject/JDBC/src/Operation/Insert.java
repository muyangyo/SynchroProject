package Operation;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/27
 * Time:21:23
 */
public class Insert implements Operation {
    @Override
    public void operate(PreparedStatement statement) {
        try {
            int n = statement.executeUpdate();
            System.out.println("已执行!共有" + n + "条数据受到影响");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
