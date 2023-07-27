package Operation;

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
 * Date 2023/7/27
 * Time:21:24
 */
public class Main {
    public static void main(String[] args) {
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource) dataSource).setURL("jdbc:mysql://127.0.0.1:3306/jdbc230727?characterEncoding=utf8&useSSL=false");
        ((MysqlDataSource) dataSource).setUser("root");
        System.out.print("请输入密码:");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        ((MysqlDataSource) dataSource).setPassword(password);


        boolean flag = true;
        while (flag) {
            try {
                System.out.print("请输入你要进行的操作(增/删/查/改): " + "随意操作默认退出!");
                String str = scanner.nextLine();
                Operation operation = null;
                String sql = null;
                switch (str) {
                    case "增": {
                        operation = new Insert();
                        break;
                    }
                    case "删": {
                        operation = new Delete();
                        break;
                    }
                    case "查": {
                        operation = new Select();
                        sql = "select * from student";
                        break;
                    }
                    case "改": {
                        operation = new Update();
                        break;
                    }
                    default: {
                        System.out.println("输入有误!退出程序");
                        flag = false;
                        break;
                    }
                }
                if(!flag) break;
                Connection connection = dataSource.getConnection();
                if (sql == null) {
                    System.out.println("请输入对应SQL:");
                    sql = scanner.nextLine();
                }
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                operation.operate(preparedStatement);//执行对应的操作

                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        scanner.close();
    }
}
