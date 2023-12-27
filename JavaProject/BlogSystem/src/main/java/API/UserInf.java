package API;

import DAO.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/18
 * Time: 21:44
 */
class Inf {
    public String userName;
    public String userGitHub;
    public int articlesCount;

    public boolean isAuthor;

    public Inf(String userName, String userGitHub, int articlesCount) {
        this.userName = userName;
        this.userGitHub = userGitHub;
        this.articlesCount = articlesCount;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }
}

@WebServlet("/userInf")
public class UserInf extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession(false) == null) {
            return;
        } else {
            try {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF8");
                UserDAO userDAO = new UserDAO();
                BlogDAO blogDAO = new BlogDAO();
                ObjectMapper objectMapper = new ObjectMapper();
                //判断访问情况
                String blogId = req.getParameter("blogId");
                if (blogId == null) {
                    //如果没有QueryString则是在访问博客列表页

                    HttpSession session = req.getSession(false);
                    String userName = (String) session.getAttribute("user");
                    User user = userDAO.getUsersByName(userName);


                    Inf inf = new Inf(user.userName, user.UserGitHub, blogDAO.getArticlesCount(user.userId));

                    String json = objectMapper.writeValueAsString(inf);
                    System.out.println(json);
                    resp.getWriter().print(json);
                    resp.getWriter().flush();
                } else {
                    //访问详情页
                    int id = new Integer(blogId);

                    Blog blog = blogDAO.getBlogById(id);
                    User user = userDAO.getUsersById(blog.userId);
                    Inf inf = new Inf(user.userName, user.UserGitHub, blogDAO.getArticlesCount(user.userId));
                    HttpSession session = req.getSession(false);
                    String userName = (String) session.getAttribute("user");
                    if (userName.equals(inf.userName)) {
                        inf.setAuthor(true);
                    }

                    String json = objectMapper.writeValueAsString(inf);
                    System.out.println(json);
                    resp.getWriter().print(json);
                    resp.getWriter().flush();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
