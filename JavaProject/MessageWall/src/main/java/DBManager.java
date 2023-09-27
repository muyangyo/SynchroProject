import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/27
 * Time: 17:30
 */
public class DBManager {
    DataSource dataSource = null;
    Connection connection = null;

    public DBManager() throws SQLException {
        //new出资源对象
        dataSource = new MysqlDataSource();
        ((MysqlDataSource) dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/messagewall?characterEncoding=utf8&useSSl=false");
        ((MysqlDataSource) dataSource).setUser("root");
        ((MysqlDataSource) dataSource).setPassword("031327");

        //建立连接
        connection = dataSource.getConnection();
    }

    public void insert(String from, String to, String msg) throws SQLException {
        //构造请求
        String sql = "insert into messages values (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, from);
        statement.setString(2, to);
        statement.setString(3, msg);
        //执行
        int n = statement.executeUpdate();
        System.out.println("已插入一条信息:");
        statement.close();
    }

    public List selectAll() throws SQLException {
        //构造请求
        String sql = "select * from messages";
        PreparedStatement statement = connection.prepareStatement(sql);

        //执行
        ResultSet resultSet = statement.executeQuery();
        List<Message> list = new LinkedList<>();
        while (resultSet.next()) {
            Message message = new Message();
            message.from = resultSet.getString("who");
            message.to = resultSet.getString("towho");
            message.msg = resultSet.getString("msg");
            list.add(message);
        }

        resultSet.close();
        statement.close();
        return list;
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
