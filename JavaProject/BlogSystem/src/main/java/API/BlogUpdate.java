package API;

import DAO.Blog;
import DAO.BlogDAO;
import DAO.User;
import DAO.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/22
 * Time: 13:34
 */
@WebServlet("/blogUpdate")
public class BlogUpdate extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        //获取用户信息
        HttpSession session = req.getSession(false);
        if (session == null) {
            return;
        }
        String userName = (String) session.getAttribute("user");
        System.out.println(userName);
        int userId = 0;
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUsersByName(userName);
            if (user.userId == 0) {
                return;
            }
            userId = user.userId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //获取文章内容
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        if (title.equals("") || content.equals("") || title.equals(" ") || content.equals(" ")) return;

        //保存到数据库中去
        try {
            Blog blog = new Blog(userId, title, content);
            BlogDAO blogDAO = new BlogDAO();
            blogDAO.insertBlog(blog);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //跳转页面
        resp.setContentType("text/html; charset=uft8");
        resp.sendRedirect("blog_list.html");
    }
}
