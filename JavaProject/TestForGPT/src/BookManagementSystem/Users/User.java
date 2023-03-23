package BookManagementSystem.Users;

import BookManagementSystem.Books.BookList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:48
 */
public abstract class User {
    private String UserName;

    public User(String userName) {
        UserName = userName;
    }

    String getUsername() {
        return UserName;
    }

    ;

    public abstract void showRole();

    public abstract void showMenu(BookList bookList);
}
