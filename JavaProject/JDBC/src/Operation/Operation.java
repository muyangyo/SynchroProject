package Operation;

import java.sql.PreparedStatement;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/27
 * Time:21:19
 */
public interface Operation {
    void operate(PreparedStatement statement);

}
