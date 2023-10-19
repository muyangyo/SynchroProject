package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/18
 * Time: 20:50
 */
public class UserDAO {
    private DBManager dbManager;

    public UserDAO() throws SQLException {
        this.dbManager = new DBManager();
    }

    public List<User> getUsers() {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbManager.getConnection();
            String sql = "select * from users";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            List<User> list = new LinkedList<>();
            while (resultSet.next()) {
                User user = new User();
                user.userId = resultSet.getInt("userId");
                user.userName = resultSet.getString("userName");
                user.password = resultSet.getString("password");
                user.UserGitHub = resultSet.getString("userGitHub");
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbManager.close(resultSet, preparedStatement, connection);

        }

    }

    public User getUsersById(int userId) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbManager.getConnection();
            String sql = "select * from users where userId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()) {
                user.userId = resultSet.getInt("userId");
                user.userName = resultSet.getString("userName");
                user.password = resultSet.getString("password");
                user.UserGitHub = resultSet.getString("userGitHub");
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbManager.close(resultSet, preparedStatement, connection);
        }
    }

    public User getUsersByName(String userName) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbManager.getConnection();
            String sql = "select * from users where userName = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);

            resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()) {
                user.userId = resultSet.getInt("userId");
                user.userName = resultSet.getString("userName");
                user.password = resultSet.getString("password");
                user.UserGitHub = resultSet.getString("userGitHub");
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbManager.close(resultSet, preparedStatement, connection);
        }
    }

}
