import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Demo3 {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        //查 的写法
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource) dataSource).setURL("jdbc:mysql://127.0.0.1:3306/Test230718?characterEncoding=utf8");
        ((MysqlDataSource) dataSource).setUser("root");
        System.out.print("请输入输入密码:");
        String password = scanner.next();
        ((MysqlDataSource) dataSource).setPassword(password);

        Connection connection = dataSource.getConnection();

        String sql = "select * from t1";//依旧可使用 占位符
        PreparedStatement statement = connection.prepareStatement(sql);

        //执行 查询 操作,返回的是一个 ResultSet 对象(一个表格)
        ResultSet resultSet = statement.executeQuery();

        //遍历表格
        while (resultSet.next()) {
            //获取该行数据的 id 列
            int id = resultSet.getInt("id");
            //获取该行数据的 name 列
            String name = resultSet.getString("name");
            System.out.println("id: " + id + " name: " + name);
        }

        resultSet.close();
        statement.close();
        connection.close();
        scanner.close();
    }
}