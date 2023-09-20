import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/20
 * Time: 19:16
 */


@WebServlet("/Json")
public class JsonServlet extends HttpServlet {

    static class User {
        public String username;
        public String password;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        // readValue 的使用
        User user = objectMapper.readValue(req.getInputStream(), User.class);
        System.out.println("username:" + user.username + " password:" + user.password);

        // writeValueAsString 的使用
        String jsonString = objectMapper.writeValueAsString(user);
        System.out.println(jsonString);
    }

}
