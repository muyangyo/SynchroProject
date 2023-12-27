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
import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/27
 * Time: 20:49
 */
@WebServlet("/modifyBlog")
public class ModifyBlog extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");

        //验证是否是作者本人
        HttpSession session = req.getSession(false);
        if (session == null) {
            return;
        }
        //获取 user 信息
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
        int blogUserId = Integer.parseInt(req.getParameter("userId"));
        if (userId != blogUserId) {
            System.out.println("非法修改");
            return;
        }

        //获取文章内容
        String title = req.getParameter("title");
        int blogId = Integer.parseInt(req.getParameter("blogId"));
        String content = req.getParameter("content");
        if (title.equals("") || content.equals("") || title.equals(" ") || content.equals(" ")) return;

        //保存到数据库中去
        try {
            Blog blog = new Blog(blogId, userId, title, content);
            BlogDAO blogDAO = new BlogDAO();
            blogDAO.update(blog);
            System.out.println(userName + " 修改了一篇博客");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.sendRedirect("blog_list.html");

    }
}
