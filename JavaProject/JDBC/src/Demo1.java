import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/17
 * Time:22:32
 */
public class Demo1 {

    public static void main(String[] args) throws SQLException {

        //1.创建数据源
        DataSource dataSource = new MysqlDataSource();
                                                        /*
                                                        为什么不直接 MysqlDataSource mySQLDataSource = new MysqlDataSource();
                                                        而去使用 向上转型 DataSource dataSource = new MysqlDataSource(); ?
                                                        为了后期更换不同类型的 数据库 所准备的(低耦合)
                                                         */
        ((MysqlDataSource) dataSource).setURL("jdbc:mysql://127.0.0.1:3306/数据库名?characterEncoding=utf8&useSSL=true");
        ((MysqlDataSource) dataSource).setUser("root");
        ((MysqlDataSource) dataSource).setPassword("密码");

        //2.和数据库建立链接
        Connection connection = dataSource.getConnection();

        //3.预处理 SQL 语句
        String sql = "SQL语句";
        PreparedStatement statement = connection.prepareStatement(sql);//在客户端判断语法是否正确


        //4.执行 SQL 语句(模拟人工输入). 返回值就是 "这次操作影响到几行"
        int n = statement.executeUpdate();//适用于 增,删,改
        //int n = statement.executeQuery();//适用于 查
        System.out.println("n = " + n);

        //5.释放资源(先创建的 后关闭,后创建的 先关闭)
        statement.close();
        connection.close();

    }

}
