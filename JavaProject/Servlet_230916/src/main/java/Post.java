import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/22
 * Time: 18:29
 */
@WebServlet("/post")
public class Post extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置"Access-Control-Allow-Origin"响应头
        // resp.setHeader("Access-Control-Allow-Origin", "http://localhost:8080/");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF8");
        resp.getWriter().println("你好!");
        resp.getWriter().flush();

        /*try (InputStream inputStream = req.getInputStream()) {
            Scanner scanner = new Scanner(inputStream, "UTF8");
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        }*/

    }
}
