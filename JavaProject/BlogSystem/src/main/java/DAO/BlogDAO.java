package DAO;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/18
 * Time: 21:09
 */
public class BlogDAO {
    private DBManager dbManager;

    public BlogDAO() throws SQLException {
        this.dbManager = new DBManager();
    }

    public List<Blog> getBlogs() {

        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbManager.getConnection();
            String sql = "select * from blogs order by pDate desc";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            List<Blog> list = new LinkedList<>();
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.blogId = resultSet.getInt("blogId");
                blog.userId = resultSet.getInt("userId");
                blog.title = resultSet.getString("title");
                blog.pDate = resultSet.getTimestamp("pDate");
                blog.content = resultSet.getString("content");
                blog.dateTime = setDateTime(blog.pDate);
                list.add(blog);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbManager.close(resultSet, preparedStatement, connection);
        }

    }

    private String setDateTime(Timestamp timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

    public Blog getBlogById(int blogId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = dbManager.getConnection();
            String sql = "select * from blogs where blogId = ? order by pDate desc";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, blogId);

            resultSet = preparedStatement.executeQuery();
            Blog blog = new Blog();
            while (resultSet.next()) {
                blog.blogId = resultSet.getInt("blogId");
                blog.userId = resultSet.getInt("userId");
                blog.title = resultSet.getString("title");
                blog.pDate = resultSet.getTimestamp("pDate");
                blog.content = resultSet.getString("content");
                blog.dateTime = setDateTime(blog.pDate);
            }
            return blog;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbManager.close(resultSet, preparedStatement, connection);
        }

    }

    public void insertBlog(Blog blog) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = dbManager.getConnection();
            String sql = "insert into blogs values (null,?,?,now(),?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, blog.userId);
            preparedStatement.setString(2, blog.title);
            preparedStatement.setString(3, blog.content);

            int n = preparedStatement.executeUpdate();
            System.out.println(blog.userId + " 更新一篇博客");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getArticlesCount(int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbManager.getConnection();
            String sql = "select count(blogId) from blogs where userId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt("count(blogId)");
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbManager.close(resultSet, preparedStatement, connection);
        }
    }

    /*public static void main(String[] args) throws SQLException {
        BlogDAO blogDAO = new BlogDAO();
        for (Blog blog:blogDAO.getBlogs()) {
            System.out.println(blog);
        }
    }*/
}
