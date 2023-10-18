package API;

import DAO.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public Inf(String userName, String userGitHub, int articlesCount) {
        this.userName = userName;
        this.userGitHub = userGitHub;
        this.articlesCount = articlesCount;
    }
}

@WebServlet("/userInf")
public class UserInf extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF8");
            UserDAO userDAO = new UserDAO();
            BlogDAO blogDAO = new BlogDAO();

            int id = 1;//用户id(后期要根据session的信息写)

            User user = userDAO.getUsersById(id);
            Inf inf = new Inf(user.userName, user.UserGitHub, blogDAO.getArticlesCount(id));

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(inf);
            System.out.println(json);
            resp.getWriter().print(json);
            resp.getWriter().flush();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
