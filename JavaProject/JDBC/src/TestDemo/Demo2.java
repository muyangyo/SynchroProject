package TestDemo;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/18
 * Time:14:11
 */
public class Demo2 {
    public static void main(String[] args) throws SQLException {
        //增 删 改 的写法
        Scanner scanner = new Scanner(System.in);
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource) dataSource).setURL("jdbc:mysql://127.0.0.1:3306/Test230718?characterEncoding=utf8" +
                "&useSSL=false");
        ((MysqlDataSource) dataSource).setUser("root");
        System.out.print("请输入密码:");
        String password = scanner.next();
        ((MysqlDataSource) dataSource).setPassword(password);

        Connection connection = dataSource.getConnection();

        String sql = "insert into t1 values (?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        System.out.print("id:");
        int id = scanner.nextInt();
        System.out.print("name:");
        String name = scanner.next();
        statement.setInt(1, id);
        statement.setString(2, name);

        int n = statement.executeUpdate();
        System.out.println("n = " + n);

        statement.close();
        connection.close();
        scanner.close();
    }
}
