package API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/20
 * Time: 21:02
 */
@WebServlet("/check")
public class Check extends HttpServlet {
    //                         小时 分钟  秒   毫秒
    static final long tenancy = 2 * 60 * 60 * 1000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        //无session的情况
        if (session == null) {
            resp.setStatus(403);
            return;
        }
        //有session的情况
        long time = (long) session.getAttribute("lastLogin");
        String user = (String) session.getAttribute("user");//用于判断登出
        if (user == null || System.currentTimeMillis() - time > Check.tenancy) {
            resp.setStatus(403);
        } else {
            return;
        }
    }
}
