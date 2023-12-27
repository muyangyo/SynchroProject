package DAO;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/18
 * Time: 20:28
 */
public class User {
    public int userId;
    public String userName;
    public String password;
    public String UserGitHub;

    public User() {
    }

    public User(int userId, String userName, String password, String userGitHub) {
        userId = userId;
        this.userName = userName;
        this.password = password;
        UserGitHub = userGitHub;
    }

    @Override
    public String   toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", UserGitHub='" + UserGitHub + '\'' +
                '}';
    }
}
